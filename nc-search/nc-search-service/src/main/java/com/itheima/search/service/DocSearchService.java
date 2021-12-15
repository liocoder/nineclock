package com.itheima.search.service;

import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.common.util.BeanHelper;
import com.itheima.common.util.JsonUtils;
import com.itheima.common.vo.PageResult;
import com.itheima.common.vo.Result;
import com.itheima.document.client.DocumentClient;
import com.itheima.document.entity.Category;
import com.itheima.document.entity.File;
import com.itheima.search.dto.SearchDTO;
import com.itheima.search.entity.Article;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocSearchService {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private DocumentClient documentClient;

    /**
     * 将查询到的文档数据转换为索引库文档对象
     */
    public Article buildArticle(File file) {
        Article article = BeanHelper.copyProperties(file, Article.class);
        article.setCreateTime(Timestamp.valueOf(file.getCreateTime()).getTime());
        return article;
    }

    public PageResult<Article> searchByKeyword(SearchDTO searchDTO) throws IOException {
        //1.参数判断
        Integer page = searchDTO.getPage();
        Integer size = searchDTO.getSize();
        //2. 创建索引请求
        SearchRequest searchRequest = new SearchRequest("nineclock");
        //3. 封装请求参数
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //3.1 指定查询方式
        //3.1.1 bool
        searchSourceBuilder.query(basicQuery(searchDTO));

        //3.2 指定分页
        searchSourceBuilder.from((page - 1) * size);
        searchSourceBuilder.size(size);
        //3.3 指定过滤字段
        searchSourceBuilder.fetchSource(new String[]{"id", "name", "content"}, null);
        //4. 将builder封装到request中
        searchRequest.source(searchSourceBuilder);
        //5. 执行检索
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //6. 处理结果
        long total = response.getHits().getTotalHits().value;
        long totalPage = total % size == 0 ? total / size : (total / size + 1);
        //6.3 封装响应数据
        List<Article> articles = Arrays.asList(response.getHits().getHits()).stream().map(hit -> {
            String sourceAsString = hit.getSourceAsString();
            //将json字符串转为Java
            return JsonUtils.jsonToPojo(sourceAsString, Article.class);
        }).collect(Collectors.toList());
        return new PageResult<>(total, totalPage, articles);
    }

    /**
     * 根据查询条件对业务按照分类进行分组聚合
     * @param searchDTO
     * @return
     */
    public Map<String, List<?>> searchFilter(SearchDTO searchDTO) throws IOException {
        Map<String, List<?>> mapResult = new HashMap<>();
        //2.创建检索请求对象：SearchRequest
        SearchRequest searchRequest = new SearchRequest("nineclock");

        //3.封装请求参数对象：SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //3.x设置各种条件
        searchSourceBuilder.query(basicQuery(searchDTO));

        //3.2 指定分页 指定请求参数中：”from" "size"
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(0);

        //3.3 设置聚合 指定请求参数中："aggs" , 聚合三要素：聚合名称 聚合类型 聚合字段
        String categoryAggname = "categoryAggname";
        searchSourceBuilder.aggregation(AggregationBuilders.terms(categoryAggname).field("categoryId"));
        //4. 封装searchSourceBuilder 到 searchRequest
        searchRequest.source(searchSourceBuilder);
        //5. 执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //6. 获取聚合结果
        Aggregations aggregations = response.getAggregations();
        Terms categoryAgg = aggregations.get(categoryAggname);
        //6.1 获取桶集合
        List<Long> ids = categoryAgg.getBuckets().stream().map(Terms.Bucket::getKeyAsNumber).map(Number::longValue).collect(Collectors.toList());
        //6.2 远程调用文档微服务获取分类集合对象
        if (!CollectionUtils.isEmpty(ids)) {
            Result<List<Category>> listResult = documentClient.queryCategoryByIds(ids);
            List<Category> data = listResult.getData();
            mapResult.put("分类", data);
        }
        return mapResult;
    }


    /**
     * 封装公用条件ES 提交参数中 "query" 部分
     * @param searchDTO
     * @return
     */
    private QueryBuilder basicQuery(SearchDTO searchDTO){
        String key = searchDTO.getKey();

        if (StringUtils.isBlank(key)) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        //3.1指定查询方式
        //3.1.1 创建外层bool查询
        BoolQueryBuilder topLevelBoolQueryBuilder = QueryBuilders.boolQuery();
        //3.1.2 设置必须满足条件 词条查询 企业ID
        topLevelBoolQueryBuilder.must(QueryBuilders.termQuery("companyId", UserHolder.getCompanyId()));
        //3.1.3 设置必须满足条件 bool查询 （两个子条件，满足其一即可，包括：文档标题，文档内容）
        BoolQueryBuilder secondLevelBoolQueryBuilder = QueryBuilders.boolQuery();
        //3.1.3.1 分词匹配查询文档标题--或者
        secondLevelBoolQueryBuilder.should(QueryBuilders.matchQuery("name", key).operator(Operator.AND));
        //3.1.3.2 分词匹配查询文档内容--或者
        secondLevelBoolQueryBuilder.should(QueryBuilders.matchQuery("content", key).operator(Operator.AND));

        topLevelBoolQueryBuilder.must(secondLevelBoolQueryBuilder);

        //如果用户选择过滤项条件
        Map<String, Long> filter = searchDTO.getFilter();
        if (!CollectionUtils.isEmpty(filter)) {
            for (Map.Entry<String, Long> entry : filter.entrySet()) {
                String filterKey = entry.getKey();
                if ("分类".equals(filterKey)) {
                    filterKey = "categoryId";
                }
                Long value = entry.getValue();
                topLevelBoolQueryBuilder.filter(QueryBuilders.termQuery(filterKey, value));
            }
        }
        return topLevelBoolQueryBuilder;
    }
}
