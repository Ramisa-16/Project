import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class QuestionBank {
    public static void main(String[] args) throws IOException {
        JSONArray questionsArray = new JSONArray();
        questionsArray.add(createQuestion(1, "Which of the following is a typical test objective?",
                new String[]{"Preventing defects", "Repairing defects", "Comparing actual results to expected results", "Analyzing the cause of failure"}, 1));
        questionsArray.add(createQuestion(2, "Suppose, you visit the HSC result publishing site to see the result of your younger sibling. But you see that site is down. What is the reason?",
                new String[]{"The fault", "The failure", "The error", "The defect"}, 2));
        questionsArray.add(createQuestion(3, "What is missing requirement?",
                new String[]{"Error", "Bug", "Fault", "Failure"}, 3));
        questionsArray.add(createQuestion(4, "Test data planning is included when?",
                new String[]{"Equivalent partitioning", "Boundary value analysis", "While doing automation", "Test procedure planning"}, 4));
        questionsArray.add(createQuestion(5, "Which of the following is an example of a task that can be carried out as part of the test process?",
                new String[]{"Requirement Analysis", "Assign task to QA team", "Writing Testcase", "Writing user manual"}, 3));
        questionsArray.add(createQuestion(6, "Developer fixed an issue and told you to test the feature. What is the testing type?",
                new String[]{"Feature Test", "Integration Test", "Re-Test", "Regression Test"}, 3));
        questionsArray.add(createQuestion(7, "Dynamic testing is mainly?",
                new String[]{"Validation Techniques", "Verification Techniques", "Both of the above", "None of the above"}, 1));
        questionsArray.add(createQuestion(8, "What is not the blackbox testing strategy?",
                new String[]{"Equivalence Partitioning", "Boundary Value Testing", "Branch Testing", "State Transition"}, 3));
        questionsArray.add(createQuestion(9, "Which is not in fundamental test process?",
                new String[]{"Test planning and control", "Requirement Analysis", "Test implementation and execution", "Evaluating exit criteria"}, 2));
        questionsArray.add(createQuestion(10, "Which of the following is not part of the Test document?",
                new String[]{"Test Case", "Requirements Traceability Matrix [RTM]", "Test strategy", "Project Initiation Note [PIN]"}, 4));
        questionsArray.add(createQuestion(11, "The _______ testing should include operational tests of the new environment as well as of the changed software.",
                new String[]{"System Testing", "Integration testing", "Component testing", "Maintenance testing"}, 4));
        try (FileWriter file = new FileWriter("./src/main/resources/quiz.json")) {
            file.write(questionsArray.toJSONString());
            System.out.println("SQA Question is ready");
        }
    }
    private static JSONObject createQuestion(int id, String questionText, String[] options, int answerKey) {
        JSONObject question = new JSONObject();
        question.put("question", questionText);
        for (int i = 0; i < options.length; i++) {
            question.put("option " + (i + 1), options[i]);
        }
        question.put("answerkey", answerKey);
        return question;
    }
}
