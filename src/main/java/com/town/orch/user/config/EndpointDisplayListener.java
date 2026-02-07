package com.town.orch.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

@Component
@Slf4j
public class EndpointDisplayListener {

  @EventListener
  public void handleContextRefresh(ContextRefreshedEvent event) {
    ApplicationContext context = event.getApplicationContext();

    // 1. Find Annotated Controllers (@RestController)
    context
        .getBeansOfType(RequestMappingHandlerMapping.class)
        .forEach(
            (name, mapping) -> {
              mapping
                  .getHandlerMethods()
                  .forEach(
                      (key, value) -> {
                        if (log.isDebugEnabled()) {
                          log.debug(
                              "EndpointDisplayListener : handleContextRefresh :: Mapped Controller: {}",
                              key);
                        }
                      });
            });

    // 2. Find Functional Routes (RouterFunction)
    context
        .getBeansOfType(RouterFunctionMapping.class)
        .forEach(
            (name, mapping) -> {
              // Note: Functional routes are harder to 'pretty print' directly
              // but the mapping object will log them if debug is enabled.
              if (log.isDebugEnabled()) {
                log.debug(
                    "EndpointDisplayListener : handleContextRefresh :: Router Function Mapping found: {}",
                    name);
              }
            });
  }
}
