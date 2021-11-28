import java.io.File;
import java.io.IOException;
import java.util.*;

//A node class to represent node of the Trie structure
class Node {
    public Map<String , Node> children;
    public String value; //the value at the node
    public long frequency; // the number of times the node is appearing
    public Node() {
        //Create root
        value = null;
        children = new HashMap<>();
    }
    //Constructor that init. value
    public Node(String value) {
        this.value = value;
    }
}


public class Spelling {
    //Root of the trie
    public Node root;

    //Constructor. Initializes the root node to an empty string
    public Spelling() {
        root = new Node("");
    }

    //Tries to locate the node with the given key and returns the string value at the node,
    // IF NOT found, the function returns a null
    public String find(Node node, String key) {
        if (key.equals(node.value)) {
            //if the key is the node
            return node.value;
        }else if (node.children.containsKey(key)) {
            //Find it in all the children
            return node.children.get(key).value;
        }else {
            //No such key found
            return null;
        }
    }


    //Function to insert a node with key and value to the trie
    public Node insert(Node node, String key, String value) {
        //Insert each charter of the key to the Tre
        for (Character c : key.toCharArray()) {
            if (!node.children.containsKey(Character.toString(c))) {
                node.children.put(c.toString(), new Node( value));
            }
        }
        //return the node
        return node;
    }

    //Deletes a node with the specified key from the Trie, starting from the specified root node
    //The function makes use of the private delete helper method
    // Returns a boolean whether the operation is successful
    public boolean delete(Node root, String key) {
        return delete(root, key, 0);
    }

    //Delete helper method
    // Deletes the all the children recursively, and sets to null the value. ret boolean whether the operation is successful
    private boolean delete(Node node, String key, int d) {
        if (d == key.length()) {
            node.value = null;
        }else  {
            char c = key.charAt(d);
            if (node.children.containsKey(Character.toString(c)) && delete(node.children.get(Character.toString(c)), key, d+1)) {
                node.children.remove(Character.toString(c));
            }
        }
        return node.value == null && node.children.isEmpty();
    }

    //Lists all the suggestions
    public List<String> autocomplete(Node root, String prefix) {
        List<String> result = new ArrayList<>();
        Node x = getNode(root, prefix);
        return collect(x, prefix, result);
    }

    //finds the node w the given key, starting from the specified root
    private Node getNode(Node root, String key) {
        for (char c: key.toCharArray()) {
            if (root.children.containsKey(Character.toString(c))) {
                return root.children.get(Character.toString(c));
            }
        }
        return null;
    }

    //recursive helper method that is used the autocomplete method. returns all the suggestions from node x
    private List<String> collect(Node x, String prefix, List<String> result) {
        if (x != null) {
            if (x.value != null) {
                result.add(prefix);
            }
            StringBuilder prefixBuilder = new StringBuilder(prefix);
            for (String c: x.children.keySet()) {
                prefixBuilder.append(c);
                prefix = prefixBuilder.toString();
                result = collect(x.children.get(c), prefix, result);
                prefixBuilder.deleteCharAt(prefixBuilder.length() - 1);
            }

        }
        return result;
    }

    //reads the csv and uses the values to build & return Trie
    private static Spelling parseCSV(String path) throws IOException {
        Spelling trie = new Spelling();
        Scanner sc = new Scanner(new File(path));
        while(sc.hasNextLine()) {
            String[] arr = sc.nextLine().split(",");

            trie.root = trie.insert(trie.root, arr[0], arr[0]);
        }
        return null;
    }
    public static void main(String[] args) throws IOException {

//         String filename = "unigram_freq.csv";
        //Get the name of the file passed as an argument to the program
        String filename = args[0];
         Spelling trie = parseCSV(filename);

    }
}
