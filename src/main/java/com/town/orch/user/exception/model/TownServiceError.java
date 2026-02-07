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

import com.core.township.common.exception.CoreServiceException;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Represents a detailed service error.
 *
 * <p>This class is used to encapsulate the error code associated with a {@link
 * CoreServiceException}.
 */
@Data
public class TownServiceError implements Serializable {
  @Serial private static final long serialVersionUID = 2465172041950251807L;

  private String code;

  /**
   * Constructs a new CoreServiceError with the specified error code.
   *
   * @param code The error code identifying the issue.
   */
  public TownServiceError(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "CoreServiceError{" + "code='" + getCode() + '\'' + '}';
  }
}
