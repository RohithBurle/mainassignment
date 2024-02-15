package com.bt.ms.im.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Config file to enable Swagger UI for the application
 * 
 * @author Suman Mandal
 * 
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		ParameterBuilder aParameterBuilder = new ParameterBuilder();
		aParameterBuilder.name("Accept").modelRef(new ModelRef("string")).parameterType("header").required(false)
				.build();
		ParameterBuilder e2edata = new ParameterBuilder();
		e2edata.name("E2EDATA").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		List<Parameter> aParameters = new ArrayList<>();
		aParameters.add(aParameterBuilder.build());
		aParameters.add(e2edata.build());
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.bt.ms.im")).build().apiInfo(metaData())
				.globalOperationParameters(aParameters);
	}

	private ApiInfo metaData() {
		@SuppressWarnings("rawtypes")
		List<VendorExtension> vendorExtensions = new ArrayList<>();
		return new ApiInfo("Network Porting Service  API", "Network Porting Service Common API for  EE and BT", "1.0",
				"Terms of service",
				new Contact(":Suman Mandal", "https://bt.com.uk/licenses/LICENSE-2.0", "suman.mandal@bt.com"),
				"BT  Enterprise Licence Version 1.0", "https://bt.com.uk/licenses/LICENSE-2.0", vendorExtensions);
	}
}
