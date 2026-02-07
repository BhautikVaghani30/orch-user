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

package com.town.orch.user.i18n;

import com.core.township.common.model.response.CoreMessage;
import java.util.regex.Pattern;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.ObjectUtils;

/**
 * Class provides capabilities to support internationalization messages by reading them from locale
 * specific resource bundles
 */
@Slf4j
public final class CoreResourceManager {

  private static final int CHK_LEN = 2;
  private static final int INDEX_0 = 0;
  private static final int INDEX_1 = 1;
  private static final Logger LOG = LoggerFactory.getLogger(CoreResourceManager.class);
  private static final Pattern DELIMITER = Pattern.compile("\\|");
  @Setter private static ResourceBundleMessageSource messageSource;

  private CoreResourceManager() {}

  /**
   * Method that returns locale specific message based on the provided message key
   *
   * @param msgKey error code.
   * @return message
   */
  public static String getMessageKey(String msgKey) {
    try {
      if (null == messageSource || ObjectUtils.isEmpty(msgKey)) {
        return msgKey;
      }
      return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
    } catch (NoSuchMessageException e) {
      LOG.error("getMessageKey, msgKey :: {}, error : {}", msgKey, e.getMessage());
      return msgKey;
    }
  }

  /**
   * Method that returns http status code by splitting message string by | delimiter. message Looks
   * like - 200|CoreCart-CartNotFound-1004|Cart does not exist in the system. this method would
   * return 200
   *
   * @param key http code.
   * @return httpStatusCode
   */
  public static int getHttpCode(String key) {
    log.debug("CoreResourceManager : getHttpCode :: key : {}", key);
    String httpStatus = getValue(key, INDEX_0);
    if (ObjectUtils.isEmpty(httpStatus)) {
      LOG.warn("httpStatus is not defined for key :: {}, key)", key);
      return 0;
    }
    return Integer.parseInt(httpStatus);
  }

  /**
   * Method that returns error/message code by splitting message string by | delimiter. message
   * Looks like - 200|CoreCart-CartNotFound-1004|Cart does not exist in the system. this method
   * would return CoreCart-CartNotFound-1004
   *
   * @param key messageCode
   * @return messageCode
   */
  public static String getCode(String key) {
    String messageCode = getValue(key, INDEX_1);
    if (ObjectUtils.isEmpty(messageCode)) {
      LOG.warn("getMessageCode :: message code is not defined for key :: {}", key);
      return null;
    }
    return messageCode;
  }

  /**
   * Private helper method for split message key by delimiter
   *
   * @param key error code
   * @param index 200|error_code.
   * @return value of index.
   */
  private static String getValue(String key, Integer index) {
    if (ObjectUtils.isEmpty(key)) {
      LOG.warn("getValue :: Key is null");
      return null;
    }
    String[] values = key.split(DELIMITER.pattern());
    if (values.length >= CHK_LEN) {
      return values[index];
    }
    return null;
  }

  /**
   * helper method for populating message code
   *
   * @param message error message.
   * @return coreMessage
   */
  public static CoreMessage prepareValidationMessageCode(String message) {
    String code = messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    return CoreMessage.builder().type("error").code(code).build();
  }
}
