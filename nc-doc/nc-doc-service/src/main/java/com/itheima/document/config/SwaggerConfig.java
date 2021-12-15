package com.itheima.document.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2Mzk3MzI3MTgsInVzZXJfbmFtZSI6IntcImlkXCI6MSxcInVzZXJuYW1lXCI6XCLlsI_mmI5cIixcInJvbGVEZXNjXCI6XCLliJvlu7rogIVcIixcIm1vYmlsZVwiOlwiMTMxMDAxMzEwMDBcIixcImltYWdlVXJsXCI6XCJodHRwczovL3Rhbmh1YS1kZXYub3NzLWNuLXpoYW5namlha291LmFsaXl1bmNzLmNvbS9pbWFnZXMvdGFuaHVhL2F2YXRhcl81LmpwZ1wiLFwiZW5hYmxlXCI6dHJ1ZSxcImNvbXBhbnlJZFwiOjEsXCJkZXBhcnRtZW50SWRcIjoxMCxcImNvbXBhbnlOYW1lXCI6XCLlpKnmtKUt5Lyg5pm6XCIsXCJkZXBhcnRtZW50TmFtZVwiOlwiSmF2YeeglOWPkVwiLFwicG9zdFwiOlwi5byA5Y-RXCIsXCJ3b3JrTnVtYmVyXCI6XCIwMDFcIn0iLCJhdXRob3JpdGllcyI6WyJhdHRlX3B1bmNoIiwiYXR0ZV9xdWVyeSIsImFwcHJvdmUiLCJkb2N1bWVudCIsIm9yZ2FuaXphdGlvbiIsInNpZ24iLCJhdHRlIiwiUk9MRV9BRE1JTl9DUkVBVEVPUiIsImNvbXBhbnlfY3JlYXRlIiwiY29tcGFueV91cGRhdGUiLCJzeXMiXSwianRpIjoiNDhlNmIxOWUtYmUzZS00MWY4LWEwYzQtNmI4ZDgxNWQ5YjkyIiwiY2xpZW50X2lkIjoiYXBwX2NsaWVudCIsInNjb3BlIjpbImFsbCJdfQ.WuRcMLVu-jJsnJwiY7a66XQq0fe84Imug27gkngoUwVl0q22QV_GYx-P9KqDQdtRGWrw98odv5Yrm0qSMwJWRLPkCzw-ACa1Xs8J0OwP_yb8M_pSmox7fsS-PCYF06D-euEwj9aOphHh6f-46irpMUVQE9ymxo71xt9Fw-5JPmFlj4glJRn357HExt9DwG_KZJ8stgeOXjJtqmJHHp_qdldP6VnHLt1VP0JPVMq_yE-CWZwx4dHtgQL0Vqsdxi39NP4YH3OJjOYp8PBbpCOTQTSCtnlnTHEsznMXr9Uak2j6yFJW8TX1K0NShtgZ8QL77eEtHGg6JR-aN-1ETi-0rQ";

    @Bean
    public Docket api() {
          //=====添加Header参数start============================
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("AccessToken令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue("Bearer "+jwtToken).build();
        pars.add(tokenPar.build());
        // =========添加Header参数end===================
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost:8084")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.itheima.document.controller"))
                .paths(PathSelectors.any())
                .build()
            	.globalOperationParameters(pars);
    }

    /**
     * 当前文档信息描述
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("九点钟系统中心")
                .description("九点钟系统中心API接口文档")
                .version("1.0")
                .build();
    }
}