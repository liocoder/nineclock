import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author itheima
 * 九点钟项目 逆向工程-生成单表CURD基础代码
 */
public class MybatisPlusAndGenerator {


    public static void main(String[] args) {

        //数据库名称
        //String dataBaseName = "nc_sys";
        String dataBaseName = "nc_doc";
        String username = "root";
        String password = "root";

        //生成代码包路径
        String packageName = "com.itheima.document";

        //生成代码磁盘路径
        String dir = "D:\\generator";

        //生成指定数据库中哪些表
        //List<String> tables = Arrays.asList("sys_common_industry", "sys_company", "sys_address_book_config", "sys_company_user", "sys_department", "sys_function", "sys_role");
        List<String> tables = Arrays.asList("doc_file", "doc_folder", "doc_file_history", "doc_collaborations", "doc_category");
        //List<String> tables = Arrays.asList("attend_group", "attend_group_part", "attend_punch");
        //List<String> tables = Arrays.asList("sign", "sign_picture");
        //List<String> tables = Arrays.asList("approve_definition", "approve_definition_his", "approve_definition_template", "approve_inst", "approve_definition_template", "approve_inst", "approve_inst_no_ctrl", "approve_inst_node", "approve_inst_record");
        //创建AutoGenerator对象
        File dirFile = new File(dir);
        if (dirFile.exists()) {
            dirFile.delete();
        }

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //设置输出的路径 项目的绝对路径地址
        gc.setOutputDir(dir);
        //设置作者
        gc.setAuthor("itheima");
        gc.setOpen(false);
        //生成列
        gc.setBaseColumnList(true);
        //生成result map集合
        gc.setBaseResultMap(true);
        //实体属性 Swagger2 注解
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);


        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        //连接的url地址
        //系统微服务数据库DB
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/" + dataBaseName + "?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=UTC");
        // dsc.setSchemaName("public");
        //设置驱动的名称
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        //设置mysql的用户名
        dsc.setUsername(username);
        //设置mysql的密码
        dsc.setPassword(password);
        //设置自动生成器的数据源
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        //设置包名
        pc.setParent(packageName);
        //设置自动生成器的包
        mpg.setPackageInfo(pc);

        //生成策略的配置
        StrategyConfig strategyConfig = new StrategyConfig();
        //生成指定表
        strategyConfig.setInclude(tables.toArray(new String[]{}));
        //去除表 前缀 例如：表名 tb_user  最终生成类为 User UserMapper UserService
        strategyConfig.setTablePrefix(new String[]{"sys_", "doc_"});
        //驼峰命名
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        //字段驼峰命名
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //设置实体Bean的lombok
        strategyConfig.setEntityLombokModel(true);
        //设置生成策略
        mpg.setStrategy(strategyConfig);

        //执行自动生成器
        mpg.execute();

        try {
            //打开windows资源管理器-指定目录
            Desktop.getDesktop().open(dirFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}