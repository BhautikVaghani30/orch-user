package com.town.orch.user.model.service;

import com.town.orch.user.dto.TownUserProfileRequestDto;
import com.town.orch.user.entities.TownUserProfile;
import reactor.core.publisher.Mono;

public interface TownUserProfileService {
  Mono<TownUserProfile> createUserProfile(TownUserProfileRequestDto townUserProfileRequestDto);

  Mono<TownUserProfile> getUserProfileByUserId(String userId);
}
