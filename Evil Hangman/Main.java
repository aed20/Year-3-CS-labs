package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Created by aed20 on 9/21/17.
 */

public class Main {

    public static void main(String[] args) throws IOException {
        //create the winning boolean
        boolean hasWon = false;
        //get the arguments for the game
        String dictionaryFileName = args[0];
        String wordLengthString = args[1];
        String numGuessesString = args[2];
        //convert the input to ints
        int wordLength = Integer.parseInt(wordLengthString);
        int numGuesses = Integer.parseInt(numGuessesString);
        //create a game object
        EvilHangmanGame game = new EvilHangmanGame();
        //create a file object
        File name = new File(dictionaryFileName);
        //call starGame on the game object, passing in the required info
        game.startGame(name, wordLength);
        //check the number of guesses
        if(numGuesses < 1){
            System.out.println("THE NUMBER OF GUESSES IS TOO LOW!");
            return;
        }
        //check if the word was long enough
        if(wordLength < 2){
            System.out.println("THE WORD LENGTH IS TOO LOW!");
            return;
        }

        //the game has been started; begin the game loop
        while (numGuesses != 0 && !hasWon) {
            //print out the number of guesses left
            System.out.println("You have " + numGuesses + " guesses left");
            //the list of used letters
            System.out.printf("Used letters: ");

            //create a counter for guessed letters
            int count = 0;
            //if there are no guessed letters, only print a newLine
            if (game.guessedLetters.size() == 0) {
                System.out.println();
            }
            //there are guessed letters: print them out
            else {
                for (String letter : game.guessedLetters) {
                    //print the last letter using newLine
                    if (count == game.guessedLetters.size() - 1) {
                        System.out.println(letter);
                    }
                    else {
                        //prints with a space
                        System.out.printf(letter);
                        System.out.printf(" ");
                        //increment count
                        count++;
                    }
                }
            }

            System.out.printf("Word: ");
            //print out the current key
            System.out.println(game.currentWord);

            //the "Enter guess" line
            System.out.printf("Enter guess: ");
            //receive the input from the user
            Scanner s = new Scanner(System.in);
            String guess = s.nextLine();
            //convert the string to lower case
            guess = guess.toLowerCase();
            //check if the string has more than 1 character
            if (guess.length() != 1) {
                System.out.println("INVALID INPUT: TOO MANY LETTERS");
                continue;
            }
            //convert to a char
            char letter = guess.charAt(0);
            //make sure it's a valid char
            //create a counter to measure the number of loops
            int counter = 0;
            for (int i = 0; i < 26; i++) {
                int check = letter - 'a';
                if (check < 0 || check >= 27) {
                    System.out.println("INVALID INPUT: NOT A LETTER");
                    break;
                }
                counter++;
            }
            //if the loop was broken before completing, reprompt
            if(counter != 26) continue;
            String oldPattern = game.currentWord;

            //call the makeGuess method on the game object
            try {
                game.makeGuess(letter);
            } catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                System.out.println("YOU ALREADY GUESSED THAT LETTER!");
                continue;
            }
            //if the current word has no hyphens, they won
            int numHyphens = 0;
            int numLetters = 0;
            for(int i = 0; i < game.currentWord.length(); i++){
                if(game.currentWord.charAt(i) == '-'){
                    numHyphens++;
                }
                if(game.currentWord.charAt(i) == guess.charAt(0)){
                    numLetters++;
                }
            }
            //check if they've won
            if(numHyphens == 0) hasWon = true;

            if(numHyphens!= 0){
                //see if they made a wrong guess: the pattern won't have changed
                if(oldPattern.equals(game.currentWord)){
                    numGuesses--;
                    System.out.printf("Sorry, there are no ");
                    System.out.printf(guess);
                    System.out.println("'s");
                }
                else{
                    if(numLetters == 1){
                        System.out.printf("Yes, there is ");
                        System.out.print(numLetters);
                        System.out.print(" ");
                        System.out.println(guess);

                    }
                    else{
                        System.out.printf("Yes, there are ");
                        System.out.print(numLetters);
                        System.out.print(" ");
                        System.out.print(guess);
                        System.out.println("'s");
                    }
                }
                System.out.println();
            }

        }
        if(hasWon){
            System.out.printf("You won! The word was: ");
            System.out.println(game.currentWord);
            return;
        }
        if(!hasWon && numGuesses == 0){
            System.out.printf("You lose! The word was: ");
            System.out.println(game.pattern.get(game.pattern.firstKey()).first());
        }
    }

}