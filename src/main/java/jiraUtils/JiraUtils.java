package jiraUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class JiraUtils {

    public static void createJiraIssue(String summary, String description) {

        final String JIRA_EMAIL = System.getenv("JIRA_EMAIL");
        final String JIRA_AUTH_TOKEN = System.getenv("JIRA_AUTH_TOKEN");
        final String JIRA_BASE_URL = System.getenv("JIRA_BASE_URL");
        final String JIRA_PROJECT_KEY = System.getenv("JIRA_PROJECT_KEY");

        String url = JIRA_BASE_URL + "/rest/api/3/issue";

        // Build ADF description
        Map<String, Object> adfDescription = buildADFDescription(description);

        // Prepare the payload
        Map<String, Object> project = Map.of("key", JIRA_PROJECT_KEY);
        Map<String, Object> issueType = Map.of("name", "Bug");

        Map<String, Object> fields = Map.of(
                "project", project,
                "issuetype", issueType,
                "summary", summary,
                "description", adfDescription
        );

        Map<String, Object> payload = Map.of("fields", fields);

        // Send POST request
        Response response = RestAssured
                .given()
                .auth()
                .preemptive()
                .basic(JIRA_EMAIL, JIRA_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(payload)
                .post(url);

        // Handle the response
        if (response.getStatusCode() == 201) {
            System.out.println("Jira issue created successfully: " + response.jsonPath().getString("key"));
        } else {
            System.out.println("Failed to create Jira issue. Status Code: " + response.getStatusCode());
            System.out.println("Response: " + response.prettyPrint());
        }
    }

    private static Map<String, Object> buildADFDescription(String text) {
        return Map.of(
                "type", "doc",
                "version", 1,
                "content", List.of(
                        Map.of(
                                "type", "paragraph",
                                "content", List.of(
                                        Map.of("type", "text", "text", text)
                                )
                        )
                )
        );
    }
}
