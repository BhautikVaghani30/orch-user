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

-- This script creates the 'user_account' table required by the CoreUser entity.
-- It is designed to be compatible with MySQL.

CREATE SCHEMA IF NOT EXISTS `township_user`;
USE
`township_user`;

CREATE TABLE IF NOT EXISTS town_user_profile (
    id VARCHAR(36) NOT NULL COMMENT 'Primary Key',
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    country_code VARCHAR(10),
    state_code VARCHAR(100),
    city VARCHAR(100),
    landmark VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uq_town_user_email UNIQUE (email)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

