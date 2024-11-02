import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class TakeQuiz {
    private static final String USERS_FILE_PATH = "./src/main/resources/user.json";
    private static final String QUIZ_FILE_PATH = "./src/main/resources/quiz.json";
    private static final int NUM_QUESTIONS = 16;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JSONObject user = authenticate(scanner);
        if (user == null) return;

        String role = (String) user.get("role");
        if ("admin".equals(role)) {
            handleAdmin(scanner);
        } else if ("student".equals(role)) {
            handleStudent(scanner, user);
        } else {
            System.out.println("Unknown role. Exiting...");
        }
    }

    private static JSONObject authenticate(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        JSONObject user = authenticateUser(username, password);
        if (user == null) {
            System.out.println("Invalid username or password. Exiting...");
        }
        return user;
    }

    private static JSONObject authenticateUser(String username, String password) {
        try (FileReader reader = new FileReader(USERS_FILE_PATH)) {
            JSONParser parser = new JSONParser();
            JSONArray users = (JSONArray) parser.parse(reader);

            for (Object obj : users) {
                JSONObject user = (JSONObject) obj;
                if (username.equals(user.get("username")) && password.equals(user.get("password"))) {
                    return user;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void handleAdmin(Scanner scanner) {
        System.out.println("Welcome admin! Please create new questions.");
        addQuestions(scanner);
    }

    private static void handleStudent(Scanner scanner, JSONObject user) {
        System.out.println("Welcome " + user.get("username") + "! Press 's' to start the quiz.");
        String input = scanner.nextLine();
        if ("s".equals(input)) {
            List<JSONObject> quizQuestions = getQuizQuestions(NUM_QUESTIONS);
            if (!quizQuestions.isEmpty()) {
                int score = startQuiz(scanner, quizQuestions);
                displayResult(score);
            }
        }
    }

    private static void addQuestions(Scanner scanner) {
        JSONArray questions = loadExistingQuestions();
        while (true) {
            JSONObject question = createQuestion(scanner);
            questions.add(question);
            System.out.println("System:> Saved successfully! Do you want to add more questions? (press 's' for start and 'q' for quit)");
            String input = scanner.nextLine();
            if ("q".equals(input)) {
                break;
            }
        }
        saveQuestions(questions);
    }

    private static JSONArray loadExistingQuestions() {
        JSONParser parser = new JSONParser();
        try {
            return (JSONArray) parser.parse(new FileReader(QUIZ_FILE_PATH));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private static JSONObject createQuestion(Scanner scanner) {
        JSONObject question = new JSONObject();
        System.out.println("System:> Input your question");
        String questionText = scanner.nextLine();
        question.put("question", questionText);
        for (int i = 1; i <= 4; i++) {
            System.out.println("Admin:> Input option " + i + ":");
            question.put("option " + i, scanner.nextLine());
        }
        System.out.println("System:> What is the answer key?");
        question.put("answerkey", scanner.nextInt());
        scanner.nextLine();
        return question;
    }

    private static void saveQuestions(JSONArray questions) {
        try (FileWriter fileWriter = new FileWriter(QUIZ_FILE_PATH)) {
            fileWriter.write(questions.toJSONString());
            System.out.println("System:> Questions saved to " + QUIZ_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<JSONObject> getQuizQuestions(int numQuestions) {
        List<JSONObject> quizQuestions = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            JSONArray questions = (JSONArray) parser.parse(new FileReader(QUIZ_FILE_PATH));
            int totalQuestions = questions.size();

            if (totalQuestions < numQuestions) {
                System.out.println("System:> Insufficient questions in the quiz bank. Please add more questions.");
                return quizQuestions;
            }

            Set<Integer> selectedQuestionIndices = new HashSet<>();
            Random random = new Random();

            while (selectedQuestionIndices.size() < numQuestions) {
                int randomIndex = random.nextInt(totalQuestions);
                if (selectedQuestionIndices.add(randomIndex)) {
                    quizQuestions.add((JSONObject) questions.get(randomIndex));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return quizQuestions;
    }

    private static int startQuiz(Scanner scanner, List<JSONObject> quizQuestions) {
        int score = 0;

        for (int questionNum = 0; questionNum < quizQuestions.size(); questionNum++) {
            JSONObject question = quizQuestions.get(questionNum);
            displayQuestion(question, questionNum);
            int userAnswer = scanner.nextInt();
            int answerKey = ((Long) question.get("answerkey")).intValue();
            if (userAnswer == answerKey) {
                score++;
            }
        }

        return score;
    }

    private static void displayQuestion(JSONObject question, int questionNum) {
        System.out.println("[Question " + (questionNum + 1) + "] " + question.get("question"));
        for (int i = 1; i <= 4; i++) {
            System.out.println(i + ". " + question.get("option " + i));
        }
        System.out.print("Student:> ");
    }

    private static void displayResult(int score) {
        System.out.println("Quiz has been completed successfully!");
        String feedback = getFeedback(score);
        System.out.println(feedback + score + " out of " + NUM_QUESTIONS);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to start again? Press 's' for start or 'q' for quit");
        String input = scanner.nextLine();
        if ("s".equals(input)) {
            System.out.println();
            main(null);
        }
    }

    private static String getFeedback(int score) {
        if (score >= 8) {
            return "Excellent! You have scored ";
        } else if (score >= 5) {
            return "Good. You have scored ";
        } else if (score >= 2) {
            return "Very poor! You have scored ";
        } else {
            return "Very sorry you are failed. You have scored ";
        }
    }
}
