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

import com.core.township.common.enums.CoreApiStatus;
import com.core.township.common.exception.model.CoreAuditData;
import com.core.township.common.model.response.CoreMessage;
import com.core.township.common.model.response.CoreMeta;
import com.core.township.common.model.response.CoreResponse;
import com.core.township.common.service.CoreAuditService;
import com.core.township.common.utils.CoreJsonParserUtils;
import com.town.orch.user.i18n.CoreResourceManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * Global exception handler for the application. This class is annotated with {@link
 * ControllerAdvice} to centralize exception handling logic across all {@code @RestController}
 * instances. It transforms exceptions into a consistent, client-friendly {@link CoreResponse}
 * format.
 */
@ControllerAdvice
@RestController
@Slf4j
public class TownGlobalExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(TownGlobalExceptionHandler.class);
  private static final String TECHNICAL_ERROR_CODE = "TECHNICAL_ERROR";

  private final CoreAuditService auditService;

  /**
   * Constructs a new CoreGlobalExceptionHandler.
   *
   * @param auditService The service responsible for handling audit logging. It is used here to log
   *     specific, auditable errors.
   */
  public TownGlobalExceptionHandler(final CoreAuditService auditService) {
    this.auditService = auditService;
  }

  /**
   * Handles custom {@link com.core.township.common.exception.CoreServiceException} instances thrown
   * within the application. It creates a structured {@link CoreResponse} with details from the
   * exception and performs audit logging if the exception contains audit data and is configured for
   * auditing.
   *
   * @param ex The {@link com.core.township.common.exception.CoreServiceException} that was thrown.
   * @return A {@link ResponseEntity} containing the standardized error response.
   */
  @ExceptionHandler(CoreServiceException.class)
  public final ResponseEntity<CoreResponse<?>> handleCoreServiceException(
      @NonNull final CoreServiceException ex) {
    log.debug(
        "TownGlobalExceptionHandler : handleCoreServiceException :: exception : {}, and code : {}",
        CoreJsonParserUtils.getJsonFromObject(ex),
        CoreJsonParserUtils.getJsonFromObject(ex.getError().getCode()));
    final CoreResponse<?> coreResponse =
        CoreResponse.create(
            resource ->
                resource
                    .withMeta(createErrorMeta())
                    .withData(null)
                    .withMessages(
                        Collections.singletonList(
                            CoreMessage.builder()
                                .code(ex.getError().getCode())
                                .type(CoreMessage.CoreMessageType.ERROR.toString())
                                .build())));

    handleAuditing(ex);
    log.debug(
        "TownGlobalExceptionHandler : handleCoreServiceException :: exception occurred : {}",
        CoreJsonParserUtils.getJsonFromObject(coreResponse));
    return new ResponseEntity<>(coreResponse, HttpStatus.valueOf(ex.getHttpCode()));
  }

  /**
   * Handles bean validation failures from {@link MethodArgumentNotValidException}. This typically
   * occurs when a {@code @Valid} annotated request body fails validation. It extracts all
   * field-specific errors and formats them into the response.
   *
   * @param ex The {@link MethodArgumentNotValidException} that was thrown.
   * @return A {@link ResponseEntity} containing the validation error details.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<CoreResponse<?>> handleMethodArgumentNotValidException(
      @NonNull final MethodArgumentNotValidException ex) {
    LOG.error("Bean validation error occurred.", ex);

    final CoreResponse<?> coreResponse =
        CoreResponse.create(
            resource -> {
              final List<CoreMessage> validationMessages =
                  ex.getBindingResult().getFieldErrors().stream()
                      .map(
                          fieldError ->
                              CoreMessage.builder()
                                  .code(fieldError.getDefaultMessage())
                                  .type(CoreMessage.CoreMessageType.ERROR.toString())
                                  .build())
                      .collect(Collectors.toList());

              resource.withMeta(createErrorMeta()).withData(null).withMessages(validationMessages);
            });

    return new ResponseEntity<>(coreResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * A catch-all handler for any unhandled {@link Exception}. This method prevents sensitive stack
   * traces from being exposed to the client by returning a generic "TECHNICAL_ERROR" message. The
   * full exception is logged for debugging purposes.
   *
   * @param ex The {@link Exception} that was thrown.
   * @return A {@link ResponseEntity} with a generic internal server error response.
   */
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<CoreResponse<?>> handleGenericException(@NonNull final Exception ex) {
    LOG.error(
        "TownGlobalExceptionHandler : handleGenericException :: An unexpected error occurred.", ex);

    final CoreResponse<?> coreResponse =
        CoreResponse.create(
            resource ->
                resource
                    .withMeta(createErrorMeta())
                    .withData(null)
                    .withMessages(
                        Collections.singletonList(
                            CoreMessage.builder()
                                .code(TECHNICAL_ERROR_CODE)
                                .type(CoreMessage.CoreMessageType.ERROR.toString())
                                .build())));
    log.debug(
        "TownGlobalExceptionHandler : handleGenericException :: exception occurred : {}",
        CoreJsonParserUtils.getJsonFromObject(coreResponse));
    return new ResponseEntity<>(coreResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Performs audit logging for a {@link com.core.township.common.exception.CoreServiceException} if
   * applicable. Auditing is triggered if the exception contains {@link CoreAuditData} and the error
   * code corresponds to a message key configured for auditing in the {@link CoreAuditService}.
   *
   * @param ex The exception to audit.
   */
  private void handleAuditing(@NonNull final CoreServiceException ex) {
    if (ex.getAuditData() == null) {
      return;
    }

    // Replaced CollectionUtils.isEmpty to address deprecation warnings and be more explicit.
    final List<String> configuredErrorMessages =
        (auditService != null) ? auditService.getErrorMessages() : null;
    if (configuredErrorMessages == null || configuredErrorMessages.isEmpty()) {
      LOG.debug(
          "TownGlobalExceptionHandler : handleAuditing :: Audit service is not available or has no error messages configured; skipping audit log for error code: {}",
          ex.getError().getCode());
      return;
    }

    // The logic attempts to find the original message key that produced the error code.
    // This is inefficient and depends on CoreResourceManager. It would be more robust
    // if CoreServiceException stored the original messageKey.
    configuredErrorMessages.stream()
        .filter(
            messageKey ->
                Objects.equals(
                    CoreResourceManager.getCode(CoreResourceManager.getMessageKey(messageKey)),
                    ex.getError().getCode()))
        .findFirst()
        .ifPresent(
            matchingMessageKey -> {
              final CoreAuditData auditData = ex.getAuditData();
              auditData.setErrorCode(matchingMessageKey); // Set the original key for auditing
              auditService.printErrorLog(auditData);
            });
  }

  /**
   * Creates a standard {@link CoreMeta} object for error responses.
   *
   * @return A new {@link CoreMeta} instance.
   */
  private CoreMeta createErrorMeta() {
    return CoreMeta.builder()
        .timestamp(Instant.now().toString())
        .status(CoreApiStatus.FAIL.getValue())
        .build();
  }

  @ExceptionHandler({WebExchangeBindException.class})
  public final ResponseEntity<CoreResponse<?>> handleWebExchangeBindException(
      WebExchangeBindException ex) {
    String methodName = "handleWebExchangeBindException";
    log.error(
        "TownGlobalExceptionHandler :: handleWebExchangeBindException : Validation failed. Reason: {}",
        ex.getMessage());
    List<CoreMessage> validationMessages =
        (List)
            ex.getBindingResult().getFieldErrors().stream()
                .map(
                    (fieldError) ->
                        CoreMessage.builder()
                            .code(fieldError.getDefaultMessage())
                            .type(CoreMessage.CoreMessageType.ERROR.toString())
                            .build())
                .collect(Collectors.toList());
    CoreResponse<?> coreResponse =
        CoreResponse.create(
            (resource) ->
                resource
                    .withMeta(this.createErrorMeta())
                    .withData(new ArrayList())
                    .withMessages(validationMessages));
    return new ResponseEntity(coreResponse, HttpStatus.BAD_REQUEST);
  }
}
