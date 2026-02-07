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

package com.town.orch.user.service;

import com.core.township.common.utils.CoreJsonParserUtils;
import com.town.orch.user.constants.TownUserErrorMessages;
import com.town.orch.user.dto.TownUserProfileRequestDto;
import com.town.orch.user.entities.TownUserProfile;
import com.town.orch.user.exception.CoreServiceException;
import com.town.orch.user.mapper.TownUserProfileMapper;
import com.town.orch.user.model.service.TownUserProfileService;
import com.town.orch.user.repository.TownUserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service implementation for managing user profiles.
 *
 * <p>This class contains the business logic for user profile operations, such as creation and
 * retrieval. It interacts with the {@link TownUserProfileRepository} for data persistence and uses
 * {@link TownUserProfileMapper} for converting between DTOs and entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TownUserProfileServiceImpl implements TownUserProfileService {

  /**
   * The repository for performing CRUD operations on {@link TownUserProfile} entities. Injected via
   * constructor by Lombok's {@code @RequiredArgsConstructor}.
   */
  private final TownUserProfileRepository townUserProfileRepository;

  /**
   * The mapper for converting between {@link TownUserProfileRequestDto} and {@link
   * TownUserProfile}. Injected via constructor by Lombok's {@code @RequiredArgsConstructor}.
   */
  private final TownUserProfileMapper townUserProfileMapper;

  /**
   * Creates a new user profile based on the provided request data.
   *
   * <p>This method first checks if a user with the given ID already exists. If so, it returns a
   * {@link CoreServiceException}. Otherwise, it maps the DTO to an entity, saves it to the
   * database, and returns the newly created profile.
   *
   * @param requestDto The DTO containing the data for the new user profile.
   * @return A {@link Mono} emitting the saved {@link TownUserProfile} on success.
   * @throws CoreServiceException if a user with the same ID already exists.
   */
  @Override
  public Mono<TownUserProfile> createUserProfile(TownUserProfileRequestDto requestDto) {

    if (log.isDebugEnabled()) {
      log.debug(
          "TownUserProfileServiceImpl : createUserProfile :: Request to create user profile: {}",
          CoreJsonParserUtils.getJsonFromObject(requestDto));
    }
    return townUserProfileRepository
        .existsById(requestDto.getId())
        .flatMap(
            exists -> {
              if (Boolean.TRUE.equals(exists)) {
                if (log.isDebugEnabled()) {
                  log.debug(
                      "TownUserProfileServiceImpl : createUserProfile :: User already exists with ID: {}",
                      requestDto.getId());
                }
                return Mono.error(
                    new CoreServiceException(TownUserErrorMessages.USER_ALREADY_EXISTS));
              }
              return createAndSaveUserProfile(requestDto);
            })
        .doOnError(
            error ->
                log.error(
                    "TownUserProfileServiceImpl : createUserProfile :: Error creating user profile: {}",
                    error.getMessage()));
  }

  /**
   * A private helper method to map a DTO to an entity and persist it.
   *
   * @param requestDto The request DTO to convert and save.
   * @return A {@link Mono} emitting the saved entity.
   */
  private Mono<TownUserProfile> createAndSaveUserProfile(TownUserProfileRequestDto requestDto) {
    return Mono.fromCallable(() -> townUserProfileMapper.toEntity(requestDto))
        .flatMap(townUserProfileRepository::save)
        .doOnSuccess(
            result -> {
              if (log.isDebugEnabled()) {
                log.debug(
                    "TownUserProfileServiceImpl : createUserProfile :: User profile saved: {}",
                    CoreJsonParserUtils.getJsonFromObject(result));
              }
            });
  }

  /**
   * Retrieves a user profile by its unique user ID.
   *
   * @param userId The unique identifier of the user.
   * @return A {@link Mono} emitting the {@link TownUserProfile} if found.
   * @throws CoreServiceException if no user is found with the given ID.
   */
  @Override
  public Mono<TownUserProfile> getUserProfileByUserId(String userId) {
    if (log.isDebugEnabled()) {

      log.debug(
          "TownUserProfileServiceImpl : createUserProfile :: Fetching user profile for ID: {}",
          userId);
    }
    return townUserProfileRepository
        .findById(userId)
        .doOnError(
            error ->
                log.error(
                    "TownUserProfileServiceImpl : createUserProfile :: Error retrieving user profile data: {}",
                    error.getMessage()))
        .switchIfEmpty(
            Mono.defer(
                () -> {
                  if (log.isDebugEnabled()) {
                    log.debug(
                        "TownUserProfileServiceImpl : createUserProfile :: User profile not found for ID: {}",
                        userId);
                  }
                  return Mono.error(new CoreServiceException(TownUserErrorMessages.USER_NOT_FOUND));
                }));
  }
}
