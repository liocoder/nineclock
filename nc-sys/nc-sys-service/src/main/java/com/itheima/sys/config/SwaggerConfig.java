package com.itheima.sys.config;

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

    private static final String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2Mzg2MTc4MjAsInVzZXJfbmFtZSI6IntcImlkXCI6MSxcInVzZXJuYW1lXCI6XCLlsI_mmI5cIixcInJvbGVEZXNjXCI6XCLliJvlu7rogIVcIixcIm1vYmlsZVwiOlwiMTMxMTExMTExMTFcIixcImltYWdlVXJsXCI6XCJodHRwczovL3Rhbmh1YS1kZXYub3NzLWNuLXpoYW5namlha291LmFsaXl1bmNzLmNvbS9pbWFnZXMvdGFuaHVhL2F2YXRhcl81LmpwZ1wiLFwiZW5hYmxlXCI6dHJ1ZSxcImNvbXBhbnlJZFwiOjEsXCJkZXBhcnRtZW50SWRcIjoxMCxcImNvbXBhbnlOYW1lXCI6XCLlpKnmtKUt5Lyg5pm6XCIsXCJkZXBhcnRtZW50TmFtZVwiOlwiSmF2YeeglOWPkVwiLFwicG9zdFwiOlwi5byA5Y-RXCIsXCJ3b3JrTnVtYmVyXCI6XCIwMDFcIn0iLCJhdXRob3JpdGllcyI6WyJhdHRlX3B1bmNoIiwiYXR0ZV9xdWVyeSIsImFwcHJvdmUiLCJkb2N1bWVudCIsIm9yZ2FuaXphdGlvbiIsInNpZ24iLCJhdHRlIiwiUk9MRV9BRE1JTl9DUkVBVEVPUiIsImNvbXBhbnlfY3JlYXRlIiwiY29tcGFueV91cGRhdGUiLCJzeXMiXSwianRpIjoiMzJjZThhODEtZWY4Yi00MTA4LWI4ODAtNDAxYmQ5MjllNmFmIiwiY2xpZW50X2lkIjoicGNfY2xpZW50Iiwic2NvcGUiOlsiYWxsIl19.W0xr0m7F1qzdiVJlascW6C0nNUIit8XACSnmb1iQ8RnxQ05BHe5swsRQ4AdvpWIjj74BuBgpJ5SmYTxpb8hFQ_8yl7QszNyKCCY8lsI2DhUvb82J2f3HzXSMy9lzfID6LGe-miFtUY_Cmh6mLuEBLeR9NuyAFhLRkAPbxb-hIeSErc9CvKn7No5mefMMOu6jOTtx6REWh8PhoPawuOgptauXhEmXMW5RqqhdqSGFdLgi-HkiMk7GNtRBr51F0m9qNyie5AazXjUFRkmVn6fPM8O66w2zwU86TtUji0gJ_ku9R0P30f7pxodtHT68PDm1pgROK2i17PK-HyjIvS4SvA";

    @Bean
    public Docket api() {
          //=====添加Header参数start============================
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("AccessToken令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue("Bearer "+jwtToken).build();
        pars.add(tokenPar.build());
        // =========添加Header参数end===================
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost:8081")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.itheima.sys.controller"))
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