import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public enum AnsTier {
        SNAKE("A SNAKE DETECTED!"),
        FAIR_WEATHER("Fair Weather Friend!"),
        GOOD_FRIEND("Good Friend!"),
        RIDE_OR_DIE("A Rare RIDE-OR-DIE COMBO!");

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

        double percentage = (maxPossible == minPossible)
                ? 100.0
                : ((rawScore - minPossible) / (maxPossible - minPossible)) * 100.0;

        return new QuizResult(percentage, AnsTier.calculateTier(percentage));
    }

    public static List<Question> getQuestions() throws FileNotFoundException {
        List<Question> questions = new ArrayList<>();
        File file = new File("questions.txt");
        // This portion Has been Stolen from somewhere from Internet HOPE IT WORKS !
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
            } catch (IOException | NumberFormatException e) {
                System.out.println("Issue reading file. Exiting Program...\n");
            }
        }
        return questions;
    }
}