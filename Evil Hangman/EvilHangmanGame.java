package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame{
    //because both methods alter these sets, make them global variables
    private TreeSet<String> possibleWords;
    TreeSet<String> guessedLetters;
    TreeMap<String, TreeSet<String>> pattern;
    String bestKey;
    int largestSize;
    String currentWord;

    @Override
    public void startGame(File fileName, int wordLength){
        //create a TreeSet to fill up for the first dictionary
        TreeSet<String> beginningDictionary = new TreeSet<>();
        //initialize the sets, string and map for each new game
        possibleWords = new TreeSet<>();
        guessedLetters = new TreeSet<>();
        pattern = new TreeMap<>();
        //create a scanner to scan in the words from the file
        Scanner s = null;

        //try/catch to handle errors
        try {
            s = new Scanner(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("THE DICTIONARY FILE WAS NOT FOUND!");
            return;
        }
        //add the words from the file to the set. and convert to lower case
        while(s.hasNext()){
            beginningDictionary.add(s.next().toLowerCase());
        }
        //now take all the words with the correct length and make a new working set
        for(String word: beginningDictionary){
            if(word.length() == wordLength){
                possibleWords.add(word);
            }
        }
        //initialize the bestKey
        StringBuilder k = new StringBuilder("");
        for(int i = 0; i < wordLength; i++){
            k.append('-');
        }
        bestKey = k.toString();
        currentWord = k.toString();
        //close the scanner
        s.close();
    }


    private void tiebreaker(String current, String possible, char guess){
        int countcur = 0;
        int countpos = 0;
        for(int i = 0; i < current.length(); i++){
            if(current.charAt(i) != '-'){
                countcur++;
            }
            if(possible.charAt(i) != '-'){
                countpos++;
            }
        }
        //the counters keep track of the number of guessed letters in each pattern
        //if the possible pattern has fewer guessed letters, replace it
        if(countpos < countcur){
            bestKey = possible;
            largestSize = pattern.get(possible).size();
            return;
            //if the current pattern has fewer letters, keep it and do nothing
        }
        //if they are still tied and have the same amount of letters, find the rightmost letter
        if(countcur == countpos){
            //compare each letter in each word
            for(int i = current.length()-1; i >= 0; i--){
                //if the possible word has the rightmost guessed letter, keep the current one
                if(current.charAt(i) == guess && possible.charAt(i) != guess){
                    return;
                }
                //if the possible word doesn't the rightmost letter, change it to that word
                if(current.charAt(i) != guess && possible.charAt(i) == guess){
                    bestKey = possible;
                    largestSize = pattern.get(possible).size();
                    return;
                }
            }
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        //convert the char to a String
        String letter = String.valueOf(guess);
        //check if the letter has already been guessed
        for(String l: guessedLetters){
            if(letter.equals(l)){
                throw new GuessAlreadyMadeException();
            }
        }
        //the letter guessed was not in the set: add it
        guessedLetters.add(letter);

        //begin partitions
        String key = "";
        pattern = new TreeMap<>();
        //use getPattern to create patterns, and put each string in a set in the pattern map
        for(String s: possibleWords) {
            //create the pattern key
            key = getPattern(guess, s);
            //check if the set at the key is null
            if(pattern.get(key) == null){
                //create a new set
                TreeSet<String> temp = new TreeSet<>();
                //add the word to the set
                temp.add(s);
                //put the key and set into the map
                pattern.put(key, temp);
            }
            //add the string to its corresponding set
            pattern.get(key).add(s);
        }
        //reset the largestSize to 0 for each new guess
        largestSize = 0;

        //determine which set is the correct one to use
        //check all the sets of words corresponding to each pattern
        for(String k: pattern.keySet()) {
            //check the sizes of the sets for each pattern
            if (pattern.get(k).size() > largestSize) {
                //if it's larger, assign it to be the new bestKey
                bestKey = k;
                largestSize = pattern.get(k).size();
                //move on to the next word
                continue;
            }
            //if the sizes are equal
            if (pattern.get(k).size() == pattern.get(bestKey).size()) {
                //call the tiebreaker function
                tiebreaker(bestKey, k, guess);
            }
        }
        //merge bestKey with currentWord
        StringBuilder t = new StringBuilder("");
        for(int i = 0; i < bestKey.length(); i++){
            //if they have the same character, append it
            if(bestKey.charAt(i) == currentWord.charAt(i)){
                t.append(bestKey.charAt(i));
            }
            //if the characters are different, add the letter
            if(bestKey.charAt(i) == '-' && currentWord.charAt(i) != '-'){
                t.append(currentWord.charAt(i));
            }
            if(currentWord.charAt(i) == '-' && bestKey.charAt(i) != '-'){
                t.append(bestKey.charAt(i));
            }
        }
        possibleWords = pattern.get(bestKey);

        currentWord = t.toString();
        return pattern.get(bestKey);
    }

    private String getPattern(char guess, String word){
        //create the key stringBuilder
        StringBuilder key = new StringBuilder("");
        //check each character in the given word
        for (int i = 0; i < word.length(); i++){
            //if the character is in the guessed word, append it
            if (word.charAt(i) == guess){
                key.append(guess);
            }
            //if the character is already in the best word, append it
            else if(word.charAt(i) == bestKey.charAt(i)){
                key.append(word.charAt(i));
            }
            //the character was not at this location: append a hyphen
            else key.append('-');
        }
        return key.toString();
    }


}
