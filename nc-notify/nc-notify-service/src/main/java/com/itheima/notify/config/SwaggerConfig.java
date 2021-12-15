package com.itheima.notify.config;

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

    private static final String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2Mzg3OTg4NDEsInVzZXJfbmFtZSI6IntcImlkXCI6MixcInVzZXJuYW1lXCI6XCLlpKfmmI5cIixcInJvbGVEZXNjXCI6XCLogYzlkZgs57O757uf566h55CG5ZGYXCIsXCJtb2JpbGVcIjpcIjEzMjAwMTMyMDAwXCIsXCJlbmFibGVcIjp0cnVlLFwiY29tcGFueUlkXCI6MSxcImRlcGFydG1lbnRJZFwiOjEwLFwiY29tcGFueU5hbWVcIjpcIuWkqea0pS3kvKDmmbpcIixcImRlcGFydG1lbnROYW1lXCI6XCJKYXZh56CU5Y-RXCIsXCJwb3N0XCI6XCLlvIDlj5FcIixcIndvcmtOdW1iZXJcIjpcIjAwMlwifSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU5fU1lTIiwiY29tcGFueV9xdWVyeV9rZXl3b3JkIiwiYXR0ZV9wdW5jaCIsImNvbXBhbnlfYWRkX2FkbWluIiwiY29tcGFueV9nZXQiLCJjb21wYW55X3F1ZXJ5X2FkbWluIiwiY29tcGFueV91cGxvYWQiLCJkb2N1bWVudCIsImF0dGVfbWVtYmVycyIsInNpZ24iLCJhdHRlIiwic3lzIiwiYXR0ZV9kZXRhaWwiLCJhdHRlX2V4cG9ydCIsImF0dGVfcXVlcnkiLCJST0xFX1VTRVIiLCJhcHByb3ZlIiwiY29tcGFueV9xdWVyeV9saXN0IiwiY29tcGFueV9zZXRhZG1pbiJdLCJqdGkiOiJhOTQzM2U3Ni00ZTIzLTQ4ODgtYTFlOS0wODk3MjFkYTYzNmQiLCJjbGllbnRfaWQiOiJhcHBfY2xpZW50Iiwic2NvcGUiOlsiYWxsIl19.FYDht70LE1mFoXh3JQj1M0dMLPj40L_VKqCN55VjYALBA7t6cxuZOK-SQEzxkNaUdK9ZV9PDzvsUu_zlqG9jnb6SLXwQhGwi7PeQkKJQz8O7XuAhuXbP37cKeyctxFUzZ7WWj9OIyrgrlS3-L80kwufRMO_zU4-TVejUPeOCkHABcyXj92hJW9zae4vkaeww_zmCZyiDKSeq_n708EpbhSTIuWhA_DIZgcx3OT1I7YW9wFNUKi21OPHMB_Snc0IRaqbK0FxfKPoBaySzRzLOTmdXjYfgXgxtu8iW9xmqXM5Xuh1TTUBeGjTDGWmy0NmtNLpzeiV7g9jOw2xWsgMDZA";
    @Bean
    public Docket api() {
          //=====添加Header参数start============================
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("AccessToken令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).defaultValue("Bearer "+jwtToken).build();
        pars.add(tokenPar.build());
        // =========添加Header参数end===================
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost:8083")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.itheima.notify.controller"))
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
                .title("九点钟推送中心")
                .description("九点钟推送中心API接口文档")
                .version("1.0")
                .build();
    }
}