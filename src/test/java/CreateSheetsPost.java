import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CreateSheetsPost  {
    String accesToken = "ya29.a0Adw1xeUB0xcHzE7ajWlnrbIU7U-axb1RNbotv2-YFl0RonJfE-6teaud5e4XpnNTOr0tTtYY7YT9SAa8MRoKlXSwU7fVubDNlBVBpeF504tiqUa17UDTBheCGGsAsJQBSg9xjfBZNXM3g5gcFZq9lnwWNzdegXcwfcJ4";
    //accesToken через час прекращает своё действие, необходимо обновить (POST запрос), см в документацию "Информация о системе п.6" и заменить значение переменное accesToken
    String postEndpoint = "https://docs.googleapis.com/v1/documents?key=AIzaSyB7gLhtxFEU9lVMLz9p70ncHJmKTK2fG90";


    @Test
    public void createSheetsValidTitle() throws IOException, InterruptedException {
        String inputJson = "{ \"title\":\"2342\" }";
        var request = HttpRequest.newBuilder()
                .uri(URI.create(postEndpoint))
                .header("Authorization", "Bearer " + accesToken)
                .POST(HttpRequest.BodyPublishers.ofString(inputJson))
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(response.body().contains(" \"title\": \"2342\","));
    }

    @Test
    public void createSheetsEmptyJson() throws IOException, InterruptedException {
        String inputJson = "{}";
        var request = HttpRequest.newBuilder()
                .uri(URI.create(postEndpoint))
                .header("Authorization", "Bearer " + accesToken)
                .POST(HttpRequest.BodyPublishers.ofString(inputJson))
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(response.body().contains(" \"title\": \"Новый документ\","));
    }

    @Test
    public void postRequestInvalidApiKey() throws IOException, InterruptedException {
        String inputJson = "{}";
        var request = HttpRequest.newBuilder()
                .uri(URI.create(postEndpoint + "1"))
                .header("Authorization", "Bearer " + accesToken)
                .POST(HttpRequest.BodyPublishers.ofString(inputJson))
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(400, response.statusCode());
    }

    @Test
    public void postRequestInvalidAuthToken() throws IOException, InterruptedException {
        String inputJson = "{}";
        var request = HttpRequest.newBuilder()
                .uri(URI.create(postEndpoint))
                .header("Authorization", "Bearer " + accesToken + "1")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson))
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(401, response.statusCode());
    }

    @Test
    public void PostRequestInvalidTitle() throws IOException, InterruptedException {
        String inputJson = "{ \"title\":2342 }";
        var request = HttpRequest.newBuilder()
                .uri(URI.create(postEndpoint))
                .header("Authorization", "Bearer " + accesToken)
                .POST(HttpRequest.BodyPublishers.ofString(inputJson))
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(400, response.statusCode());
        Assert.assertTrue(response.body().contains("\"message\": \"Invalid value at 'title' (TYPE_STRING), 2342\","));

    }
}