/*
 * Copyright 2025 com.core.township
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.town.orch.user.config;

import com.core.township.common.service.CoreAuditService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central configuration class for common beans used throughout the user service.
 *
 * <p>This class consolidates the creation of shared components, such as services from the common
 * library and security-related beans.
 */
@Configuration
public class TownCommonConfig {
  /**
   * Provides the central audit service bean from the common library.
   *
   * @return An instance of {@link CoreAuditService}.
   */
  @Bean
  public CoreAuditService coreAuditService() {
    return new CoreAuditService();
  }
}
