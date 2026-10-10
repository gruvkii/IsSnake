package ansEvaulator;

import java.util.List;
import java.util.Map;

public class EvaluateAns {

    public enum AnsTier {
        SNAKE("A SNAKE DETECTED!"),
        FAIR_WEATHER("Fair Weather!"),
        GOOD_FRIEND("Good Friend!"),
        RIDE_OR_DIE("A rare COMBO!");

        private final String label;

        AnsTier(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static AnsTier fromPercentage(double score) {
            if (score >= 85.0) return RIDE_OR_DIE;
            if (score >= 65.0) return GOOD_FRIEND;
            if (score >= 40.0) return FAIR_WEATHER;
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

        public static QuizResult evaluate(List<Question> questions, List<String> userChoices) {
            if (questions.size() != userChoices.size()) {
                throw new IllegalArgumentException("Answers count must match questions count.");
            }

            double rawScore = 0.0;
            double maxPossible = 0.0;
            double minPossible = 0.0;

            for (int index = 0; index < questions.size(); index++) {
                Question question = questions.get(index);
                String chosenValue = userChoices.get(index);

                if (chosenValue == null) {
                    throw new IllegalArgumentException("Chosen choice key cannot be null at index " + index);
                }

                Option chosenOption = question.options().get(chosenValue);
                if (chosenOption == null) {
                    throw new IllegalArgumentException("Invalid choice value: " + chosenValue);
                }

                rawScore += question.weight() * chosenOption.score();
                minPossible += question.weight() * question.getMinScore();
                maxPossible += question.weight() * question.getMaxScore();
            }

            double percentage = (maxPossible == minPossible)
                    ? 100.0
                    : ((rawScore - minPossible) / (maxPossible - minPossible)) * 100.0;

            return new QuizResult(percentage, AnsTier.fromPercentage(percentage));
        }
    }
}