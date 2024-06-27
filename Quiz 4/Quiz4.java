import java.io.*;
import java.util.*;


class TrieNode implements Comparable<TrieNode> {
    char charVal;
    long result;
    ArrayList<TrieNode> children;

    public TrieNode() {
        this.charVal = 0;
        result = Long.MAX_VALUE;
        children = new ArrayList<>();
    }

    public TrieNode(char charVal, long result){
        this();
        this.charVal = charVal;
        this.result = result;
    }

    @Override
    public int compareTo(TrieNode o) {
        return Long.compare(this.result, o.result);
    }

    public void addString(char[] val, long result) {
        TrieNode child = children
                .stream()
                .filter(trieNode -> trieNode.charVal == val[0])
                .findFirst()
                .orElse(null);


        if(child == null) {
            child = new TrieNode(val[0], Long.MAX_VALUE);
            children.add(child);
        }

        if (val.length != 1) {
            child.addString(Arrays.copyOfRange(val, 1, val.length), result);

        }
        else {
            child.children.add(new TrieNode((char) 0, result));
        }
    }

    public void addToListInOrder(ArrayList<Long> src) {
        if (children.isEmpty()) src.add(result);
        else {
            for (TrieNode child : children) {
                child.addToListInOrder(src);
            }
        }
    }

    public TrieNode find(char[] match) {

        TrieNode child = children
                .stream()
                .filter(trieNode -> trieNode.charVal == match[0])
                .findFirst()
                .orElse(null);

        if (child == null) return null;
        if (match.length == 1) return child;
        else return child.find(Arrays.copyOfRange(match, 1, match.length));
    }
}


public class Quiz4 {

    public static void main(String[] args) throws IOException {
        TrieNode root = new TrieNode();
        HashMap<Long, String> map = new HashMap<>();

        Scanner scanner = new Scanner(new File(args[0]));
        int dataLength = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < dataLength; i++) {
            String[] line = scanner.nextLine().split("\\t+");
            long number = Long.parseLong(line[0]);
            String prefix = line[1].toLowerCase();
            root.addString(prefix.toCharArray(), number);
            map.put(number, prefix);
        }

        scanner = new Scanner(new File(args[1]));

        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split("\\t+");
            int count = Integer.parseInt(line[1]);

            ArrayList<Long> res = new ArrayList<>();
            TrieNode target = root.find(line[0].toLowerCase().toCharArray());
            target.addToListInOrder(res);
            res.sort(Comparator.reverseOrder());

            System.out.printf("Query received: \"%s\" with limit %s. Showing results:\n", line[0].toLowerCase(), count);
            for (int i = 0; i < count; i++) {
                if(i < res.size())
                    System.out.printf("- %d %s\n", res.get(i), map.get(res.get(i)));
            }
            if (count == 0 || res.isEmpty()) System.out.println("No results.");


        }

    }
}
