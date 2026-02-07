package com.town.orch.user.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.town.orch.user.constants.TableConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Represents a user entity in the system. This class models the `users` table in the database and
 * contains all user-related information.
 */
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = TableConstants.TABLE_USERS_PROFILE)
public class TownUserProfile implements Persistable<String> {

  /** Primary Key, stored as BINARY(16) in the database. */
  @Id
  @Column(TableConstants.COLUMN_ID)
  private String id;

  /** The user's first name. */
  @Column(TableConstants.COLUMN_FIRST_NAME)
  private String firstName;

  /** The user's last name. */
  @Column(TableConstants.COLUMN_LAST_NAME)
  private String lastName;

  /** The user's email address. This should be unique across all users. */
  @Column(TableConstants.COLUMN_EMAIL)
  private String email;

  /** The user's phone number. */
  @Column(TableConstants.COLUMN_PHONE)
  private String phone;

  /** The user's phone number. */
  @Column(TableConstants.COUNTRY_CODE)
  private String countryCode;

  /** The user's phone number. */
  @Column(TableConstants.STATE)
  private String stateCode;

  /** The user's phone number. */
  @Column(TableConstants.CITY)
  private String city;

  /** The user's phone number. */
  @Column(TableConstants.LANDMARK)
  private String landmark;

  /**
   * The timestamp when the user account was created. This is typically managed by the database or
   * application logic upon creation.
   */
  @Column(TableConstants.COLUMN_CREATED_AT)
  @CreatedDate
  private LocalDateTime createdAt;

  /**
   * The timestamp when the user account was last updated. This is updated whenever a change is made
   * to the user's record.
   */
  @Column(TableConstants.COLUMN_UPDATED_AT)
  @LastModifiedDate
  private LocalDateTime updatedAt;

  /**
   * Checks if the entity is new.
   * If 'createdAt' is null, it means the record hasn't been audited/saved yet,
   * so we treat it as a new record (INSERT).
   */
  @Override
  @Transient
  @JsonIgnore
  public boolean isNew() {
    return id == null || createdAt == null;
  }
}
