### Species Checker ( Previously IsSnake)

So This project works on three phase System
- Bootloader
- Evaulation and Caluclation
- Display & Result Status

On the Bootloader Section it is just a fancy way of showing the Program starting so it gives an spooky vibes and Intentionally I have used the Dark fantasy Characters for the Metaphor of personality!

The bootloader just loads the ASCII art and Disclaimer with Some text for fun ( custom message BTW ! ) then It will run The main program!

The program will just start after the Disclaimer, It will throw you to answer the Question ( I have make sure for fallback if you pressed wrong key) , Just choose the options like MCQ (The system will auto evaulate the values and it is not based on standard metric rather it is based on weighted system! each answers have different weight! for the same question and the final points are heavily affected by your answer in each question)

LOGIC FOR OUTPUT : `((rawScore - minPossible) / (maxPossible - minPossible)) * 100.0`

On the Display & Result Status Section, The program will take that Final Percentage and Print your exact Tier Rank on the Screen! Based on how many points you got, It will map you to one of these Dark fantasy Characters :
- Wizard ( Top Scorer! Perfectly organized and healthy schedule )
- Vampire ( Night owls that hide from sun and code at midnight! )
- Zombie ( Chaos! Surviving on zero sleep and energy drinks )
- Ghost ( Lowest Scorer! Disappears mysteriously and ignores messages )

To add more Questions or modify the Questions you don't even need to touch the Main Java code! You just have to open the `questions.txt` file and add your own Question !

Make sure to follow this exact format for each line so the System can parse it correctly:
`Prompt | Weight | Key:Label:Score | Key:Label:Score`

Like this!
`[SCHEDULE] What does your daily timeline look like? | 1.0 | A:Sleep at 10 PM:100 | B:Active from 2 AM:70 | C:Sleep is a myth:40 | D:No set schedule:10`

Just remember to separate the Main parts with `|` and the Options with `:` and give different Scores for each options to keep the weighted system working properly!