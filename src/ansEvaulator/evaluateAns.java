package ansEvaulator;
import java.util.List;
import java.util.Map;

public class evaluateAns {
    public  enum ansTier{
        SNAKE ("A SNAKE DETECTED!"),
        FairWeather("Fair Weather!"),
        GoodFriend("Good Friend!"),
        RideDie("A rare COMBO!");

        private final String label;

        ansTier(String label) {
            this.label = label;
        }

        public  String getLabel() {return  label;}

        public record Question(string prompt, float weight, Map<string, option> options){
            public  int getMinScore(){}
            public  int getMaxScore(){}

        }
    }
}
