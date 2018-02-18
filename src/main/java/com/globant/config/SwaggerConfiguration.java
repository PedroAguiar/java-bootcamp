package com.globant.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;


@Configuration
@EnableSwagger2
@EnableConfigurationProperties(value = SwaggerProperties.class)
public class SwaggerConfiguration {


    @Autowired
    private SwaggerProperties swaggerProperties;


    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.globant.controller"))
                .paths(paths())
                .paths(not(regex("/clients.*" )))
                .paths(not(regex("/orders.*" )))
                .paths(not(regex("/payments.*" )))
                .paths(not(regex("/items.*" )))
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .termsOfServiceUrl(swaggerProperties.getTermsPath())
                .contact(new Contact("", "", swaggerProperties.getEmail()))
                .license(swaggerProperties.getLicenceType())
                .licenseUrl(swaggerProperties.getLicencePath())
                .version(swaggerProperties.getVersion())
                .build();
    }

    /**
     * @return the predicate used to check if the path should be included or not.
     */
    private Predicate<String> paths() {
        return or(regex("/v1.*"),
                regex("/client.*"),
                regex("/item.*"),
                regex("/order.*"),
                regex("/payment.*"));
    }

}
