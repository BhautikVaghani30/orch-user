package com.town.orch.user.constants;

/**
 * Contains error message keys for User validation. These keys map to messages defined in
 * application.properties.
 */
public final class TownUserErrorMessages {

  private TownUserErrorMessages() {}

  // User DTO Messages
  public static final String USER_ID_REQUIRED =   "O_U_E_1001_Town_User_First_Name_Required";
  public static final String FIRST_NAME_REQUIRED =   "O_U_E_1002_Town_User_First_Name_Required";
  public static final String LAST_NAME_REQUIRED = "O_U_E_1003_Town_User_Last_Name_Required";
  public static final String EMAIL_REQUIRED = "O_U_E_1004_Town_User_Email_Required";
  public static final String INVALID_EMAIL_FORMAT = "O_U_E_1005_Town_User_Invalid_Email_Format";
  public static final String PHONE_FORMAT = "O_U_E_1006_Town_User_Invalid_Phone_Format";

  // user profile error messages
  public static final String USER_NOT_FOUND = "O_U_E_1007_Town_User_Not_Found";
  public static final String USER_ALREADY_EXISTS = "O_U_E_1008_Town_User_Profile_Already_Exists";
  public static final String USER_PROFILE_CREATED_SUCCESSFULLY = "O_U_S_1009_Town_User_Profile_Created_Successfully";
  public static final String USER_PROFILE_FOUND = "O_U_S_1010_Town_User_Profile_Found_Successfully";
}
