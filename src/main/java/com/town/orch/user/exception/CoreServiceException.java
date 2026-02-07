/*
 * Copyright 2025 com.core
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

package com.town.orch.user.exception;

import com.core.township.common.exception.model.CoreAuditData;
import com.core.township.common.exception.model.CoreServiceError;
import com.core.township.common.i18n.CoreResourceManager;
import com.core.township.common.utils.CoreJsonParserUtils;
import java.io.Serial;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom runtime exception for service-level errors.
 *
 * <p>This exception is used to represent errors that occur within the core services of the
 * application. It encapsulates an HTTP status code, a detailed error object, and optional audit
 * data for tracking.
 */
@Slf4j
@Getter
public class CoreServiceException extends RuntimeException {

  @Serial private static final long serialVersionUID = 4316082005276462834L;

  private final int httpCode;
  private final CoreServiceError error;
  private CoreAuditData auditData;

  /**
   * Constructs a new CoreServiceException with the specified HTTP status code and error code.
   *
   * @param httpCode The HTTP status code to be returned to the client.
   * @param errorCode The specific error code identifying the issue.
   */
  public CoreServiceException(int httpCode, String errorCode) {
    this.httpCode = httpCode;
    this.error = new CoreServiceError(errorCode);
  }

  /**
   * Constructs a new CoreServiceException with the specified HTTP status code, error code, and
   * audit data.
   *
   * @param httpCode The HTTP status code to be returned to the client.
   * @param errorCode The specific error code identifying the issue.
   * @param auditData Additional data for auditing or logging purposes.
   */
  public CoreServiceException(int httpCode, String errorCode, CoreAuditData auditData) {
    this.httpCode = httpCode;
    this.auditData = auditData;
    this.error = new CoreServiceError(errorCode);
  }

  /**
   * Constructs a new CoreServiceException using a message key to look up error details. The HTTP
   * code and error code are retrieved from the {@link CoreResourceManager}.
   *
   * @param messageKey The key used to look up the error message and details.
   */
  public CoreServiceException(String messageKey) {
    super(CoreResourceManager.getCode(CoreResourceManager.getMessageKey(messageKey)));
    String errorResource = CoreResourceManager.getMessageKey(messageKey);
    this.httpCode = CoreResourceManager.getHttpCode(errorResource);
    this.error = createServiceError(messageKey);
    log.debug(
        "CoreServiceException :: constructor : httpCode : {}, error : {}",
        httpCode,
        CoreJsonParserUtils.getJsonFromObject(error));
  }

  /**
   * Creates a {@link CoreServiceError} instance from a message key.
   *
   * @param messageKey The key used to look up the error code from the {@link CoreResourceManager}.
   * @return A new {@link CoreServiceError} instance.
   */
  private CoreServiceError createServiceError(String messageKey) {
    String errorResource = CoreResourceManager.getMessageKey(messageKey);
    String code = CoreResourceManager.getCode(errorResource);
    return new CoreServiceError(code);
  }
}
