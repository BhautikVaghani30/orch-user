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

package com.town.orch.user.exception.model;

import com.core.township.common.utils.CoreJsonParserUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the audit data for payment details from the backend. This class is used for capturing
 * audit trails of payment details.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TownAuditData {

  /** The ID of the cart. This field is ignored during JSON serialization. */
  @JsonIgnore private String cartId;

  /** The ID of the bag. */
  private String bagId;

  /** The ID of the user. This field is ignored during JSON serialization. */
  @JsonIgnore private String userId;

  /** The ID of the session. This field is ignored during JSON serialization. */
  @JsonIgnore private String sessionId;

  /** The error code associated with the audit data. */
  private String errorCode;

  /** The result code associated with the audit data. */
  private String resultCode;

  /** The request payload. */
  @JsonProperty("updateRequest")
  private String request;

  /** The response payload. */
  @JsonProperty("updateResponse")
  private String response;

  /** The ID of the order. */
  private String orderId;

  /** The creation date of the audit data. */
  @JsonProperty("created")
  private String createdDate;

  /** The device from which the request originated. */
  private String device;

  /** The channel through which the request was made. */
  private String channel;

  /** The status of the operation. */
  private String status;

  /** The type of the payment. */
  @JsonProperty("pType")
  private String type;

  /** The audit log. This field is ignored during JSON serialization. */
  @JsonIgnore private String auditLog;

  /**
   * Returns a JSON representation of the {@link TownAuditData} object.
   *
   * @return A JSON string representing the object.
   */
  @Override
  public String toString() {
    return CoreJsonParserUtils.getJsonFromObject(this);
  }
}
