/*
 * Copyright 2025 com.core.township
 */

package com.town.orch.user.mapper;

import com.town.orch.user.dto.TownUserProfileRequestDto;
import com.town.orch.user.entities.TownUserProfile;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting between Town User Profile Request DTOs and Entities. All fields are
 * explicitly mapped to protect against future field name changes.
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TownUserProfileMapper {

  @Mapping(target = "id", source = "request.id")
  @Mapping(target = "firstName", source = "request.firstName")
  @Mapping(target = "lastName", source = "request.lastName")
  @Mapping(
      target = "email",
      expression =
          "java(request.getEmail() != null ? request.getEmail().toLowerCase().trim() : null)")
  @Mapping(target = "phone", source = "request.phone")
  @Mapping(target = "countryCode", source = "request.countryCode")
  @Mapping(target = "stateCode", source = "request.stateCode")
  @Mapping(target = "city", source = "request.city")
  @Mapping(target = "landmark", source = "request.landmark")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  TownUserProfile toEntity(TownUserProfileRequestDto request);

  /**
   * Maps Entity back to Request DTO. Useful for returning data to the frontend in the same format
   * it was sent.
   */
  @Mapping(target = "id", source = "entity.id")
  @Mapping(target = "firstName", source = "entity.firstName")
  @Mapping(target = "lastName", source = "entity.lastName")
  @Mapping(target = "email", source = "entity.email")
  @Mapping(target = "phone", source = "entity.phone")
  @Mapping(target = "countryCode", source = "entity.countryCode")
  @Mapping(target = "stateCode", source = "entity.stateCode")
  @Mapping(target = "city", source = "entity.city")
  @Mapping(target = "landmark", source = "entity.landmark")
  @Mapping(target = "createdAt", source = "entity.createdAt")
  @Mapping(target = "updatedAt", source = "entity.updatedAt")
  TownUserProfileRequestDto toRequest(TownUserProfile entity);
}
