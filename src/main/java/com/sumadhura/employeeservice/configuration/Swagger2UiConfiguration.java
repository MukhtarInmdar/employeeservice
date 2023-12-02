package com.sumadhura.employeeservice.configuration;

/*import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
 */
/*@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan*/
public class Swagger2UiConfiguration /*extends WebMvcConfigurerAdapter*/
{
   /* @Bean*/
	 /*
    public Docket api() {
   	return new Docket(DocumentationType.SWAGGER_2).select()
	            .apis(RequestHandlerSelectors
	                .basePackage("com.sumadhura.employeeservice"))
	            .paths(PathSelectors.regex("/.*"))
	            .build().apiInfo(apiEndPointsInfo());
    }
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("APACHE CXF REST API")
            .description("Employee Service REST API")
            .contact(new Contact("Venkat Koniki", "http://amaravadhis.com/", "kvenkat@amaravadhis.com"))
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .version("1.0.0")
            .build();
    }*/
 
  /*  @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        //enabling swagger-ui part for visual documentation
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }*/
}
