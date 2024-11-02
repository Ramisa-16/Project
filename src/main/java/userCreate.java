import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
public class userCreate {
    public static void main(String[] args) throws IOException {
        try {
            JSONObject admin = createUser("admin", "1234", "admin");
            JSONArray userArray = new JSONArray();
            userArray.add(createUser("nidhi", "1234", "student"));
            userArray.add(createUser("ramisa", "1245", "student"));
            writeToFile(userArray, "./src/main/resources/user.json");
            System.out.println("User creation successful");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    private static JSONObject createUser(String username, String password, String role) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("password", password);
        user.put("role", role);
        return user;
    }

    private static void writeToFile(JSONArray userArray, String filePath) throws IOException {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(userArray.toJSONString());
        }
    }
}
