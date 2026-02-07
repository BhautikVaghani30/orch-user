package com.town.orch.user.dto;

import com.town.orch.user.constants.TownUserErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** DTO for creating or updating a Town User Profile. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TownUserProfileRequestDto {

  @NotBlank(message = TownUserErrorMessages.USER_ID_REQUIRED)
  private String id;

  @NotBlank(message = TownUserErrorMessages.FIRST_NAME_REQUIRED)
  private String firstName;

  @NotBlank(message = TownUserErrorMessages.LAST_NAME_REQUIRED)
  private String lastName;

  @NotBlank(message = TownUserErrorMessages.EMAIL_REQUIRED)
  @Email(message = TownUserErrorMessages.INVALID_EMAIL_FORMAT)
  private String email;

  /** Phone number validation. Accepts optional '+' followed by 7 to 25 digits/spaces/dashes. */
  private String phone;

  private String countryCode;

  // Maps to 'stateCode' in Entity
  private String stateCode;

  private String city;

  private String landmark;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
