Feature: Validate Account via Mobile Number

  As a user of the system
  I want to validate my account using my mobile number
  So that I can verify my identity and proceed with authentication

  Background:
    Given I am using the AccessAccountService

  Scenario: Validate an existing account using a valid mobile number
    Given I provide a valid mobile number that exists in the system
    When I call the ValidateAccount RPC
    Then I should receive a response indicating the account exists
    And I should receive an OTP on the mobile number
    And I should get a message saying "OTP Sent to the Mobile Number"

  Scenario: Try to validate an account using a non-existing mobile number
    Given I provide a mobile number that does not exist in the system
    When I call the ValidateAccount RPC
    Then I should receive a response indicating the account does not exist
    And I should get a message saying "Account doesn't exists. Please Create your Account."

  Scenario: Validate an account without providing a mobile number
    Given I provide no mobile number
    When I call the ValidateAccount RPC
    Then I should receive an error response indicating invalid input

  Scenario: Validate an account using an invalid mobile number format
    Given I provide an invalid mobile number format
    When I call the ValidateAccount RPC
    Then I should receive an error response indicating invalid input

