package com.atguigu.srb.sms.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    
    @Bean
    public Docket smsCoreConfig(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("sms")
                .apiInfo(getApinInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
        return docket;
    }

    public ApiInfo getApinInfo() {
        return new ApiInfoBuilder()
                .title("尚融宝短信系统-API文档")
                .description("本文档描述了尚融宝短信系统接口")
                .version("1.0")
                .contact(new Contact("Mr.xu", "http://atguigu.com", "55317332@qq.com"))
                .build();
    }
}
