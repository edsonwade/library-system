Feature: Admin Management

  # Scenario to retrieve all admins
  # This scenario tests the ability to retrieve all admins in the system.
  # If no admins exist, it should return an empty list.
  # If admins are present, it should return a list of admin objects.
  @ignore
  Scenario: Retrieve all admins
    When I request to retrieve all admins
    Then the response should have status 200
    And the response should match response.json

  # Scenario to retrieve a specific admin by ID
  # This tests retrieving an admin by their unique ID.
  # If the admin exists, the correct admin data should be returned.
  # If the admin does not exist, a 404 status and error message should be returned.
  @ignore
  Scenario: Retrieve admin by ID
    Given the admin with ID 1 exists
    When I request to retrieve the admin by ID 1
    Then the response should have status 200
    And the response should match response.json

  # Scenario to retrieve a specific admin by ID when the admin does not exist
  # This tests the case where an admin with a given ID does not exist.
  # A 404 error and message "Admin not found" should be returned.
  @ignore
  Scenario: Retrieve admin by ID when admin does not exist
    Given no admin with ID 999 exists
    When I request to retrieve the admin by ID 999
    Then the response should have status 404
    And the response should contain a message like "Admin not found"

  # Scenario to create a new admin
  # This tests the creation of a new admin with the data in request.json.
  # It verifies that the response is successful (201 Created) and matches the expected response.
  Scenario: Create a new admin
    Given I have the admin data in request.json
    When I send a POST request to create an admin with the data from request.json
    Then the response should have status 201
    And the response should match response.json

  # Scenario to create a new admin with missing required fields
  # This tests the scenario where required fields are missing (e.g., missing 'name').
  # It should return a 400 Bad Request error and provide a clear message about the missing fields.
  @ignore
  Scenario: Create a new admin with missing required fields
    Given I have incomplete admin data in request.json (missing "name")
    When I send a POST request to create an admin with the incomplete data
    Then the response should have status 400
    And the response should contain an error message indicating the missing field(s)

  # Scenario to update an existing admin
  # This tests the case where an existing admin's details are updated.
  # It ensures the PUT request to update the admin with ID 1 works correctly and returns a 200 status.
  @ignore
  Scenario: Update an existing admin
    Given the admin with ID 1 exists
    And I have the updated admin data in request.json
    When I send a PUT request to update the admin with ID 1 using the data from request.json
    Then the response should have status 200
    And the response should match response.json

  # Scenario to update an admin when the admin does not exist
  # This tests the case where the admin with a given ID does not exist.
  # It should return a 404 Not Found status and indicate that the admin was not found.
  @ignore
  Scenario: Update an admin when the admin does not exist
    Given the admin with ID 999 does not exist
    And I have the updated admin data in request.json
    When I send a PUT request to update the admin with ID 999
    Then the response should have status 404
    And the response should contain a message like "Admin not found"

  # Scenario to delete an existing admin
  # This tests the case where an admin is deleted by ID.
  # The system should successfully delete the admin and return a success message.
  @ignore
  Scenario: Delete an existing admin
    Given the admin with ID 1 exists
    When I send a DELETE request to delete the admin with ID 1
    Then the response should have status 200
    And the response should contain:
      """
      "Admin with ID 1 has been successfully deleted."
      """

  # Scenario to attempt deleting a non-existing admin
  # This tests the case where an attempt is made to delete an admin who does not exist.
  # A 404 error should be returned, along with a message saying the admin was not found.
  @ignore
  Scenario: Delete a non-existing admin
    Given the admin with ID 999 does not exist
    When I send a DELETE request to delete the admin with ID 999
    Then the response should have status 404
    And the response should contain:
      """
      "Admin with ID 999 not found."
      """
