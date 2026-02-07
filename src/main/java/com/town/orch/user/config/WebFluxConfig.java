package com.town.orch.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {
  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    // Increases memory limit to 1MB for incoming data (JSON, File uploads)
    configurer.defaultCodecs().maxInMemorySize(1024 * 1024);
  }
}
