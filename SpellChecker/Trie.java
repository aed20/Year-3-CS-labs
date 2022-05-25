package spell;

/**
 * Created by aed20 on 9/18/17.
 */

public class Trie implements ITrie {

    private Node root = new Node();
    public int NodeCount = 1;
    public int wordCount;
    public int index;

    class Node implements INode{

        private Node[] children;
        private int frequency;

        Node(){
            children = new Node[26];
            NodeCount++;
        }

        public int getValue(){
            return frequency;
        }

        public void incrementFreq(){ frequency++; }
    }

    @Override
    public void add(String word) {
        //call the recursive function, using the root and index at 0
        index = 0;
        recurseAdd(word, root, index);
    }

    public void recurseAdd(String word, Node n, int index){
        //get the char
        char c = word.charAt(index);
        //check if the child exists
        if(n.children[c-'a'] == null){
            //if not, create it
            n.children[c-'a'] = new Node();
        }

        //base case: at the end of the word to add
        if(index == word.length()-1) {
            //increment wordcount and frequency count
            wordCount++;
            n.children[c - 'a'].incrementFreq();
        }
        //if not at the end of the word, increment index and look at the next let
        else {
            index++;
            recurseAdd(word, n.children[c-'a'], index);
        }
    }

    @Override
    public ITrie.INode find(String word) {
        //reset the index
        index = 0;
        return recurseFind(word, root, index);
    }

    public ITrie.INode recurseFind(String word, Node n, int index){
        //get the char
        char c = word.charAt(index);
        //check if the child exists; if it doesn't, return null
        if(n.children[c-'a'] == null) return null;

        //you've reached the end of the word; check if the child letter is the same
        if(index == word.length()-1){
            //check if there is no node here
            if(n == null) return null;
            //check if this node has a child at that node; if not, return null
            if(n.children[c-'a'] == null) return null;
            //if there is a node, check its frequency; if 0, return null
            if(n.children[c-'a'].getValue() == 0) return null;
            //return the node
            else return n.children[c-'a'];
        }
        else {//increment index
            index++;
            //recursively call the child to continue looking
            return recurseFind(word, n.children[c - 'a'], index);
        }
    }

    @Override
    public int getWordCount() {
        /**
         * Returns the number of unique words in the trie
         *
         * @return The number of unique words in the trie
         */
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        /**
         * Returns the number of nodes in the trie
         *
         * @return The number of nodes in the trie
         */
        return NodeCount;
    }

    @Override
    public String toString(){
        StringBuilder dictionary = new StringBuilder();
        StringBuilder word = new StringBuilder();
        recurseToString(root, dictionary, word);
        return dictionary.toString();
    }

    public void recurseToString(Node n, StringBuilder dictionary, StringBuilder word){
        //check if the node has any children
        if(n.getValue() > 0){
            dictionary.append(word.toString());
            dictionary.append('\n');
            //reset the word after each word is added
        }
        for(int i = 0; i < 26; i++){
            if(n.children[i] != null) {
                //append the char to the word
                word.append((char) (i + 'a'));
                //recurse
                recurseToString(n.children[i], dictionary, word);
            }
        }
        if(word.length() != 0) {
            word.deleteCharAt(word.length() - 1);
        }
    }

    @Override
    public int hashCode(){
        return 29*NodeCount + 31*wordCount;
    }

    @Override
    public boolean equals(Object o){
        //if the object is null, return false
        if(o == null) return false;
        //if the object can't be cast as a trie, return false
        if(!(o instanceof Trie)) return false;
        //cast it as a trie
        Trie that = (Trie)o;
        //check if the classes are the same
        if(this.getClass() != that.getClass()) return false;
        //if they point to the same object, return true
        if(this == that) return true;
        //compare word count, node count, and the children
        if(this.getWordCount() == that.getWordCount()){
            if(this.getNodeCount() == that.getNodeCount()){
                //recursively check the children
                //will return false if something isn't correct
                return equals(this.root, that.root);
            }
        }
        //something isn't the same; return false;
        return false;
    }

    public boolean equals(Node n1, Node n2){
        //if one of the nodes is null and the other isn't, return false.
        if(n1 != null && n2 == null) return false;
        if(n1 == null && n2 != null) return false;
        //compare the frequency count at each node in the Tries
        if(n1.getValue() == n2.getValue()){
            //if they are the same, continue to all their children
            for(int i = 0; i < 26; i++){
                //if both children are null, skip them; don't do anything
                if(n1.children[i] == null && n2.children[i] == null){}
                else equals(n1.children[1], n2.children[i]);
            }
            //reaching here means all the children are equal
            return true;
        }
        //they were not the same frequency; return false
        return false;
    }
    //end of Trie
}