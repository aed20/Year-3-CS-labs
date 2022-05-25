package spell;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Created by aed20 on 9/18/17.
 */

public class SpellCorrector implements ISpellCorrector {

    Trie dictionary;
    String bestWord;
    int bestFreq = 0;

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        dictionary = new Trie();
        File name = new File(dictionaryFileName);
        Scanner s = new Scanner(name);
        while (s.hasNext()){
            dictionary.add(s.next().toLowerCase());
        }
        s.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
      //  System.out.println("Number of Nodes: " + dictionary.getNodeCount());
        //check if given an empty string
        if(inputWord == "") return null;
        //convert to lower-case
        inputWord = inputWord.toLowerCase();
        //if inputWord is already a word, return it
        if(dictionary.find(inputWord) != null) return inputWord;

        //create a set to store all the possible suggested words
        TreeSet<String> wordsToCheck = new TreeSet<>();

        //attempt 1 to edit word; also checks and replaces bestWord
        Insertion(inputWord, wordsToCheck);
        Deletion(inputWord, wordsToCheck);
        Alteration(inputWord, wordsToCheck);
        Transposition(inputWord, wordsToCheck);

        //if bestWord is not null, then a suggested word was found and assigned
        if(bestWord != null) return bestWord;

        //if bestWord is still null, must do round 2 of edits
        //create a new TreeSet for the second round to fill
        TreeSet<String> round2 = new TreeSet<String>();

        //check all the strings in the old TreeSet
        for(String s: wordsToCheck) {
            Transposition(s, round2);
            Insertion(s, round2);
            Deletion(s, round2);
            Alteration(s, round2);
        }

        //after round 2 of edits, check the bestWord;
        //if not null, there is a similar word to suggest; return it
        if(bestWord != null) return bestWord;
        else return null;
    }

    public void checkIfBestWord(ITrie.INode n, String enter){
        //if not null, the word exists in the trie
        if(n != null){
            //compare the frequencies to the bestWord's frequency
            int possibleWordFreq = n.getValue();
            //if the possible word has a higher frequency than bestWord, replace it and its frequency
            if(possibleWordFreq > bestFreq){
                bestWord = enter;
                bestFreq = n.getValue();
            }
            //if the frequencies are the same, compare them alphabetically
            if(possibleWordFreq == bestFreq){
                //compare the two strings: positive means enter is before best.
                if(bestWord.compareTo(enter) > 0){
                    bestWord = enter;
                    bestFreq = possibleWordFreq;
                }
            }
            //reaching here means the word was in the dictionary, but not the best word. Do nothing
        }
        //the word was null; not in the dictionary. Do nothing
    }

    public void Insertion(String inputWord, TreeSet<String> wordsToCheck){
        //attempt to add every letter at every position
        for(int i = 0; i < inputWord.length() + 1; i++){
            for(char c = 'a'; c <= 'z'; c++ ){
                StringBuilder newWord = new StringBuilder(inputWord);
                newWord.insert(i, c);
                String enter = newWord.toString();

                //create a node to store the one in the trie; either null or not
                ITrie.INode n = dictionary.find(enter);
                //check if it's the best word
                checkIfBestWord(n, enter);
                wordsToCheck.add(enter);
            }
        }
        return;
    }

    public void Deletion(String inputWord, TreeSet<String> wordsToCheck){
        //attempt to delete each letter from the word
        //check for 1-letter words and empty strings
        if(inputWord.length() == 1 || inputWord.length() == 0){
            return;
        }
        for(int i = 0; i < inputWord.length(); i++){
            StringBuilder newWord = new StringBuilder(inputWord);
            newWord.deleteCharAt(i);
            String enter = newWord.toString();

            //check if the newly generated word is in the trie; create a node, either null or not
            ITrie.INode n = dictionary.find(enter);
            checkIfBestWord(n, enter);
            //the word was not in the trie; add it to the list of words to check for round 2
            //if it was in the dictionary, we won't even have to use the set for round 2
            wordsToCheck.add(enter);
        }
        return;
    }

    public void Alteration(String inputWord, TreeSet<String> wordsToCheck){
        //attempt to change every letter to every other letter
        for(int i = 0; i < inputWord.length(); i++){
            for(char c = 'a'; c <= 'z'; c++){
                StringBuilder newWord = new StringBuilder(inputWord);
                newWord.setCharAt(i, c);
                String enter = newWord.toString();

                //create a node to store the one in the trie; either null or not
                ITrie.INode n = dictionary.find(enter);
                //check if it's the best word
                checkIfBestWord(n, enter);
                wordsToCheck.add(enter);
            }
        }
        return;
    }

    public void Transposition(String inputWord, TreeSet<String> wordsToCheck){
        //attempt to swap each adjacent letter in the word
        //1-letter words can't transpose
        if(inputWord.length() == 1 || inputWord.length() == 0){
            wordsToCheck.add(inputWord);
            return;
        }
        for(int i = 0; i < inputWord.length() - 1; i++){
            StringBuilder newWord = new StringBuilder(inputWord);
            //get the first char
            char c1 = newWord.charAt(i);
            //get the char next to it
            char c2 = newWord.charAt(i+1);
            //switch the two
            String swap = new String(""+c2+c1);
            //delete the two chars at the index
            newWord.deleteCharAt(i);
            newWord.deleteCharAt(i);
            //insert the switch at the first char
            newWord.insert(i, swap);
            //change it into a string
            String enter = newWord.toString();

            //create a node to store the one in the trie; either null or not
            ITrie.INode n = dictionary.find(enter);
            //check if it's the best word
            checkIfBestWord(n, enter);
            //add the word to the set
            wordsToCheck.add(enter);
        }
        return;
    }
}
