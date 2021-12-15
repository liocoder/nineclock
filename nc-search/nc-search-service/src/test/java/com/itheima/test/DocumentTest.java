package com.itheima.test;

import com.itheima.common.util.JsonUtils;
import com.itheima.common.vo.PageResult;
import com.itheima.document.client.DocumentClient;
import com.itheima.document.entity.File;
import com.itheima.search.service.DocSearchService;
import com.itheima.search.entity.Article;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DocumentTest {
    @Autowired
    private DocumentClient documentClient;
    @Autowired
    private DocSearchService docSearchService;
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void batchImportData2ES() throws IOException {
        int page = 1;
        int pageSize = 10;
        while (true) {
            PageResult<File> filePageResult = documentClient.queryByPage(page, pageSize);
            System.out.println(filePageResult);
            List<Article> articles = filePageResult.getRows().stream().map(file -> {
                return docSearchService.buildArticle(file);
            }).collect(Collectors.toList());

            //调用es客户端批量保存数据
            BulkRequest request = new BulkRequest();
            for (Article article : articles) {
                request.add(new IndexRequest("nineclock").id(article.getId().toString()).source(JsonUtils.toJsonStr(article),XContentType.JSON));
            }

            BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
            System.out.println(response);
            page++;
            if (filePageResult.getRows().size() < pageSize) {
                break;
            }
        }
    }
}
