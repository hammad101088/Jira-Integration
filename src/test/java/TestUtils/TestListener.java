package TestUtils;

import io.restassured.response.Response;
import jiraUtils.JiraUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        // Capture test details
        String testName = result.getName();
        String failureReason = result.getThrowable() != null
                ? result.getThrowable().getMessage()
                : "Unknown reason";

        // Capture the API response if available
        Object responseObject = result.getAttribute("apiResponse");
        String apiResponseDetails = responseObject instanceof Response
                ? ((Response) responseObject).prettyPrint()
                : "No API response captured.";

        try {
            // Report the issue to Jira with the API response included
            JiraUtils.createJiraIssue(
                    "[Test Automation Failure] " + testName,
                    "[Test Case] " + testName + "\n" +
                            "[Failure Reason] " + failureReason + "\n\n" +
                            "[API Response]\n" + apiResponseDetails
            );
        } catch (Exception e) {
            System.err.println("Failed to report issue to Jira: " + e.getMessage());
        }
    }
}
