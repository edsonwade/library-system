Feature: Member management

  Scenario: Retrieve all members
    Given the members exist
    When I request to retrieve all members
    Then I should receive a list of members with status 200 OK

  Scenario: Retrieve a member by ID
    Given the member with ID 1 exists
    When I request to retrieve the member by ID 1
    Then I should receive the member details with status 200 OK
  @ignore
  Scenario: Create a new member
    Given a valid member request
    When I request to create a new member
    Then the member should be created with status 201 CREATED
  @ignore
  Scenario: Update a member
    Given the member with ID 1 exists
    And a valid update request
    When I request to update the member by ID 1
    Then the member should be updated with status 200 OK

  Scenario: Delete a member
    Given the member with ID 1 exists
    When I request to delete the member by ID 1
    Then the member should be deleted with status 200 OK

  @ignore
  Scenario: Delete a member with non-existing ID
    Given the member with ID 999 does not exist
    When I request to delete the member by ID 999
    Then I should receive a 404 Not Found status