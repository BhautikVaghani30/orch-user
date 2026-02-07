/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * All rights reserved
 */
package com.town.orch.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration that will be common across all APIs */
@Configuration
public class TownOpenApiConfig {

  /**
   * Custom open api open api.
   *
   * @return the open api
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Township User Service API")
                .version("1.0.0")
                .description("API for managing users within the Township ecosystem.")
                .contact(new Contact().name("Township Support").email("support@township.com"))
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
  }
}
