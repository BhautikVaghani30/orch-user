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

package com.town.orch.user.constants;

/**
 * This final class holds all the table and column names used in the user service. Using a constants
 * class prevents hardcoding strings and reduces the risk of typos.
 */
public final class TableConstants {

  /** Private constructor to prevent instantiation of this utility class. */
  private TableConstants() {}

  public static final String TABLE_USERS_PROFILE = "town_user_profile";

  public static final String COLUMN_ID = "id";
  public static final String COLUMN_FIRST_NAME = "first_name";
  public static final String COLUMN_LAST_NAME = "last_name";
  public static final String COLUMN_EMAIL = "email";
  public static final String COLUMN_PHONE = "phone_number";
  public static final String COUNTRY_CODE = "country_code";
  public static final String STATE = "state_code";
  public static final String CITY = "city";
  public static final String LANDMARK = "landmark";
  public static final String COLUMN_CREATED_AT = "created_at";
  public static final String COLUMN_UPDATED_AT = "updated_at";
}
