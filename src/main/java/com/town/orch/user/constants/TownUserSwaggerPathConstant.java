package com.town.orch.user.constants;

public class TownUserSwaggerPathConstant {
  // This must be the path to the file relative to the documentation root (usually the URL)
  // Since Springdoc is looking for "v3/components/examples/town_create_user_flow.yaml"
  // your reference should use the full relative path to the file, and then the JSON pointer '#'
  public static final String CREATE_USER_FLOW_EXAMPLES =
      "oas/components/examples/town_create_user_flow.yaml#"; // <-- REMOVE THE TRAILING POINTER

  // This is the pointer *inside* the YAML file to the specific example definition
  public static final String CREATE_USER = "/components/examples/CreateUserSuccessResponse";
}
