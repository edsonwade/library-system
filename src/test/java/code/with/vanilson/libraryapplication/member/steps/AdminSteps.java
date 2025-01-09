package code.with.vanilson.libraryapplication.member.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * code.with.vanilson.libraryapplication.member.steps.AdminSteps
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-07
 */
public class AdminSteps {
    @When("I request to retrieve all admins")
    public void iRequestToRetrieveAllAdmins() {
    }

    @Then("the response should have status {int}")
    public void theResponseShouldHaveStatus(int arg0) {
    }

    @And("the response should match response.json")
    public void theResponseShouldMatchResponseJson() {
    }

    @Given("the admin with ID {int} exists")
    public void theAdminWithIDExists(int arg0) {
    }

    @When("I request to retrieve the admin by ID {int}")
    public void iRequestToRetrieveTheAdminByID(int arg0) {
    }

    @Given("no admin with ID {int} exists")
    public void noAdminWithIDExists(int arg0) {
    }

    @And("the response should contain a message like {string}")
    public void theResponseShouldContainAMessageLike(String arg0) {
    }

    @Given("I have the admin data in request.json")
    public void iHaveTheAdminDataInRequestJson() {
    }

    @When("I send a POST request to create an admin with the data from request.json")
    public void iSendAPOSTRequestToCreateAnAdminWithTheDataFromRequestJson() {
    }

    @Given("I have incomplete admin data in request.json \\(missing {string})")
    public void iHaveIncompleteAdminDataInRequestJsonMissing(String arg0) {
    }

    @When("I send a POST request to create an admin with the incomplete data")
    public void iSendAPOSTRequestToCreateAnAdminWithTheIncompleteData() {
    }

    @And("the response should contain an error message indicating the missing field\\(s)")
    public void theResponseShouldContainAnErrorMessageIndicatingTheMissingFieldS() {
    }

    @And("I have the updated admin data in request.json")
    public void iHaveTheUpdatedAdminDataInRequestJson() {
    }

    @When("I send a PUT request to update the admin with ID {int} using the data from request.json")
    public void iSendAPUTRequestToUpdateTheAdminWithIDUsingTheDataFromRequestJson(int arg0) {
    }

    @Given("the admin with ID {int} does not exist")
    public void theAdminWithIDDoesNotExist(int arg0) {
    }

    @When("I send a PUT request to update the admin with ID {int}")
    public void iSendAPUTRequestToUpdateTheAdminWithID(int arg0) {
    }

    @When("I send a DELETE request to delete the admin with ID {int}")
    public void iSendADELETERequestToDeleteTheAdminWithID(int arg0) {
    }

    @And("the response should contain:")
    public void theResponseShouldContain() {
    }

}