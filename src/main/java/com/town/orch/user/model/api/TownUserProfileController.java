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

package com.town.orch.user.model.api;

import com.core.township.common.model.response.CoreResponse;
import com.town.orch.user.constants.TownApiConstants;
import com.town.orch.user.constants.TownUserSwaggerPathConstant;
import com.town.orch.user.dto.TownUserProfileRequestDto;
import com.town.orch.user.entities.TownUserProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@RequestMapping(TownApiConstants.API_BASE_URL)
@Tag(
    name = "Town User Authentication API",
    description = "API for user authentication, authorization, and session management.")
public interface TownUserProfileController {
  /**
   * Creates a new user profile within the Township ecosystem.
   *
   * <p>This endpoint accepts a validated request DTO, performs necessary business checks, and
   * persists the new user profile to the database.
   *
   * @param townUserProfile The DTO containing the details of the user to be created.
   * @return A {@link Mono} emitting a {@link CoreResponse} containing the created {@link
   *     TownUserProfile}.
   */
  @PostMapping(
      path = TownApiConstants.API_PROFILE_DETAILS,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Create a new user profile",
      description =
          "Accepts a user profile request payload, validates the data, and creates a new user record in the database.",
      responses = {
        @ApiResponse(
            description = "Successful Response",
            responseCode = "200",
            content = {
              @Content(
                  examples = {
                    @ExampleObject(
                        name = "HappyPath",
                        ref =
                            TownUserSwaggerPathConstant.CREATE_USER_FLOW_EXAMPLES
                                + TownUserSwaggerPathConstant.CREATE_USER),
                  },
                  mediaType = "application/json",
                  schema = @Schema(implementation = CoreResponse.class))
            })
      })
  Mono<CoreResponse<TownUserProfile>> createUser(
      @RequestHeader(name = "X-Auth-User-Id", required = true) String userId,
      @RequestHeader(name = "X-Auth-Session-Id", required = true) String sessionId,
      @Valid @RequestBody TownUserProfileRequestDto townUserProfile);

  /**
   * Retrieves the detailed profile information for a specific user based on their unique
   * identifier.
   *
   * @return A {@link Mono} emitting a {@link CoreResponse} containing the {@link TownUserProfile}
   *     if found.
   */
  @GetMapping(
      path = TownApiConstants.API_PROFILE_DETAILS,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get user profile details",
      description =
          "Fetches the complete profile data for a user identified by the provided query parameter 'id'.",
      responses = {
        @ApiResponse(
            description = "Successful Response",
            responseCode = "200",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CoreResponse.class))
            })
      })
  Mono<CoreResponse<TownUserProfile>> getUserProfileById(
      @RequestHeader(name = "X-Auth-User-Id", required = true) String userId,
      @RequestHeader(name = "X-Auth-Session-Id", required = true) String sessionId);
}
