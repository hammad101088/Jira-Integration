package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestJira {

    @Test(description = "Test user info")
    public void TestUserInfo() {

        Response response = given()
                .baseUri("https://reqres.in/api")
                .when()
                .get("/users/2");

        ITestResult result = org.testng.Reporter.getCurrentTestResult();
        result.setAttribute("apiResponse", response);

        Assert.assertEquals(response.jsonPath().getString(
                        "data.first_name"),
                "wrongName",
                "first name mismatch");
    }
}
