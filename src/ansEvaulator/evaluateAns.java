package ansEvaulator;

import java.util.List;
import java.util.Map;

public class evaluateAns {
    public enum ansTier {
        SNAKE("A SNAKE DETECTED!"), FairWeather("Fair Weather!"), GoodFriend("Good Friend!"), RideDie("A rare COMBO!");

        private final String label;

        ansTier(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public record Question(string prompt, float weight, Map<string, option> options) {
            public int getMinScore() {
                return options.values().stream().mapToInt(Option::score).min().orElse(0);
            }

            public int getMaxScore() {
                return options.values().stream().mapToInt(Option::score).max().orElse(0);
            }
        }
    }

    public record QuizResult(double percentageScore, friendTeir tier) {
        public QuizResult evaluate(List<Question> questions, list<string> userChoices) {
            if (questions.size() = !userChoices.size()) {
                throw new IllegalArgumentException("Answers count must match questions count.");
            }
            double rawScore = 0.0;
            double maxPossible = 0.0;
            double minPossible = 0.0;
            for (int index = 0; index < questions.size(); index++) {
                Question question = questions.get(index);
                string chosenValue = userChoices.get(index);
                Option chosenOption = question.options().get(chosenValue);

                if (chosenValue == null) {
                    throw new IllegalArgumentException("Invalid choice value: " + chosenValue);
                }
                rawScore += question.weight() * chosenOption.score();
                minPossible += question.weight() * question.getMinScore();
                maxPossible += question.weight() * question.getMaxScore();

                double percentage = ((rawScore - minPossible) / (maxPossible - minPossible)) * 100.0;
            }
        }
    }
}
