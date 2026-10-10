import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public enum AnsTier {
        SNAKE("A VAMPIRE! "), FAIR_WEATHER("A GHOST! "), GOOD_FRIEND("A ZOMBIE!"), RIDE_OR_DIE("A WITCH'S FAMILIAR!");

        private final String label;

        AnsTier(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static AnsTier calculateTier(double score) {
            if (score >= 85.0) return RIDE_OR_DIE;
            if (score >= 60.0) return GOOD_FRIEND;
            if (score >= 35.0) return FAIR_WEATHER;
            return SNAKE;
        }
    }

    public record Option(String label, int score) {
    }

    public record Question(String prompt, float weight, Map<String, Option> options) {
        public int getMinScore() {
            return options.values().stream().mapToInt(Option::score).min().orElse(0);
        }

        public int getMaxScore() {
            return options.values().stream().mapToInt(Option::score).max().orElse(0);
        }
    }

    public record QuizResult(double percentageScore, AnsTier tier) {
    }

    public static QuizResult evaluate(List<Question> questions, List<String> userChoices) {
        double rawScore = 0.0;
        double minPossible = 0.0;
        double maxPossible = 0.0;
        for (int index = 0; index < questions.size(); index++) {
            Question question = questions.get(index);
            String ChoiceKey = userChoices.get(index);
            Option option = question.options.get(ChoiceKey);


            rawScore += question.weight() * option.score();
            minPossible += question.weight() * question.getMinScore();
            maxPossible += question.weight() * question.getMaxScore();
        }

        double percentage = (maxPossible == minPossible) ? 100.0 : ((rawScore - minPossible) / (maxPossible - minPossible)) * 100.0;

        return new QuizResult(percentage, AnsTier.calculateTier(percentage));
    }

    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        try (java.io.InputStream is = Main.class.getResourceAsStream("/questions.txt")) {
            if (is != null) {
                try (BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty() || line.startsWith("#")) continue;

                        String[] parts = line.split("\\|");
                        if (parts.length < 3) continue;

                        String prompt = parts[0].trim();
                        float weight = Float.parseFloat(parts[1].trim());

                        Map<String, Option> opts = new LinkedHashMap<>();
                        for (int i = 2; i < parts.length; i++) {
                            String[] optData = parts[i].split(":");
                            if (optData.length == 3) {
                                opts.put(optData[0].trim().toUpperCase(), new Option(optData[1].trim(), Integer.parseInt(optData[2].trim())));
                            }
                        }
                        questions.add(new Question(prompt, weight, opts));
                    }
                    if (!questions.isEmpty()) return questions;
                }
            } else {
                System.out.println("questions.txt not found inside the JAR.\n");
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Issue reading Questions. Exiting Program...\n");
        }

        return questions;
    }

    public static void printSpookyBootloader() {
        String purple = "\u001B[35m";
        String reset = "\u001B[0m";

        String[] sprite = {
                "    █████",
                "  ███████",
                "███░░█░░█",
                "███░▓█░▓█",
                "███░░█░░█",
                "█████████",
                "█████████",
                "██ ███ ██",
                "█   █   █"
        };

        System.out.println("INITIALIZING C.R.T. TERMINAL...");

        try {
            Thread.sleep(800);
            System.out.println("LOADING ENTITY...");
            Thread.sleep(800);
            System.out.println();

            for (String line : sprite) {
                System.out.println(purple + line + reset);
                Thread.sleep(200);
            }

            System.out.println();
            Thread.sleep(600);
            System.out.println("CONNECTION ESTABLISHED.\n");
            Thread.sleep(600);

        } catch (InterruptedException e) {
            // ignore if interrupted
        }
    }

    // MAIN CODE RUNNING BLOCK ( PLEASE DON'T MODIFY IT UNLESS EXTREME NECESSARY EVERYTHING IS GOING TO START FROM THIS BLOCK)
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Question> questions = getQuestions();
        List<String> userChoices = new ArrayList<>();
        printSpookyBootloader();
        System.out.println("        IS YOUR BRO A SNAKE          ");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println("Q" + (i + 1) + ": " + q.prompt());

            for (Map.Entry<String, Option> entry : q.options().entrySet()) {
                System.out.println("  [" + entry.getKey() + "] " + entry.getValue().label());
            }

            String choice;
            while (true) {
                System.out.print("Your choice: ");
                choice = scanner.nextLine().trim().toUpperCase();

                if (q.options().containsKey(choice)) {
                    break;
                }
                System.out.println("Invalid input. Please choose from: " + q.options().keySet());
            }

            userChoices.add(choice);
            System.out.println();
        }

        QuizResult result = evaluate(questions, userChoices);

        System.out.printf("Final Score: %.1f%%\n", result.percentageScore());
        System.out.println("Verdict:     " + result.tier().getLabel());
        System.out.println("Tier Rank:   " + result.tier().name());
    }
}