package com.town.orch.user.repository;

import com.town.orch.user.entities.TownUserProfile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownUserProfileRepository extends ReactiveCrudRepository<TownUserProfile, String> {}
