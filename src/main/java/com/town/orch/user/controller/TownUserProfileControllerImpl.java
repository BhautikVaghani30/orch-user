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

package com.town.orch.user.controller;

import com.core.township.common.enums.CoreApiStatus;
import com.core.township.common.model.response.CoreMessage;
import com.core.township.common.model.response.CoreMeta;
import com.core.township.common.model.response.CoreResponse;
import com.core.township.common.utils.CoreJsonParserUtils;
import com.town.orch.user.constants.TownUserErrorMessages;
import com.town.orch.user.dto.TownUserProfileRequestDto;
import com.town.orch.user.entities.TownUserProfile;
import com.town.orch.user.model.api.TownUserProfileController;
import com.town.orch.user.model.service.TownUserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * Implementation of the {@link TownUserProfileController} interface.
 *
 * <p>This controller handles incoming HTTP requests for user profile operations, delegates business
 * logic to the {@link TownUserProfileService}, and constructs standardized {@link CoreResponse}
 * objects.
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class TownUserProfileControllerImpl implements TownUserProfileController {

  private final TownUserProfileService townUserProfileService;

  /**
   * Handles the creation of a new user profile.
   *
   * @param townUserProfile The DTO containing the user profile data.
   * @return A {@link Mono} wrapping the standardized response with the created profile.
   */
  @Override
  public Mono<CoreResponse<TownUserProfile>> createUser(
      String userId, String sessionId, TownUserProfileRequestDto townUserProfile) {
    if (log.isDebugEnabled()) {
      log.debug(
          "TownUserProfileControllerImpl : createUser :: Request to create user profile: {}, userId: {}, sessionId: {}",
          CoreJsonParserUtils.getJsonFromObject(townUserProfile),
          userId,
          sessionId);
    }

    return townUserProfileService
        .createUserProfile(townUserProfile)
        .map(
            profile ->
                buildSuccessResponse(
                    profile, TownUserErrorMessages.USER_PROFILE_CREATED_SUCCESSFULLY))
        .doOnSuccess(
            result -> {
              if (log.isDebugEnabled()) {
                log.debug(
                    "TownUserProfileControllerImpl : createUser :: User profile created successfully. ID: {}, Data: {}",
                    result.getData().getId(),
                    CoreJsonParserUtils.getJsonFromObject(result));
              }
            });
  }

  /**
   * Handles the retrieval of a user profile by ID.
   *
   * @param userId The unique identifier of the user.
   * @return A {@link Mono} wrapping the standardized response with the fetched profile.
   */
  @Override
  public Mono<CoreResponse<TownUserProfile>> getUserProfileById(String userId, String sessionId) {
    log.debug(
        "TownUserProfileControllerImpl : createUser :: Request to fetch user profile by ID: {}, sessionId: {}",
        userId,
        sessionId);

    return townUserProfileService
        .getUserProfileByUserId(userId)
        .map(profile -> buildSuccessResponse(profile, TownUserErrorMessages.USER_PROFILE_FOUND))
        .doOnSuccess(
            result -> {
              if (log.isDebugEnabled()) {
                log.debug(
                    "TownUserProfileControllerImpl : createUser :: User profile fetched successfully. ID: {}, Data: {}",
                    userId,
                    CoreJsonParserUtils.getJsonFromObject(result));
              }
            });
  }

  /**
   * Builds a standardized successful response.
   *
   * @param data The payload data (e.g., TownUserProfile).
   * @param messageCode The success message code to include in the response.
   * @return A {@link CoreResponse} object containing the data and success metadata.
   */
  private CoreResponse<TownUserProfile> buildSuccessResponse(
      TownUserProfile data, String messageCode) {
    CoreResponse<TownUserProfile> response = new CoreResponse<>();
    response.setMeta(CoreMeta.builder().status(CoreApiStatus.SUCCESS.getValue()).build());
    response.setData(data);
    response.setMessages(
        Collections.singletonList(CoreMessage.builder().code(messageCode).build()));
    return response;
  }
}
