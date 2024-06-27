import java.util.*;
import java.io.*;

class Station {
    public int x,y;

    public Station(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


class Edge implements Comparable<Edge> {
    public final int v, w;
    public final double weight;

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }
}


class Subsets {
    public static class Subset {
        int parent, rank;

        public Subset(int parent, int rank)
        {
            this.parent = parent;
            this.rank = rank;
        }
    }

    Subset[] src;

    public Subsets(int V) {
        this.src = new Subset[V];

        for (int i = 0; i < V; i++) {
            src[i] = new Subset(i, 0);
        }
    }

    public int findRoot(int i) {
        if (src[i].parent != i) src[i].parent = findRoot(src[i].parent);
        return src[i].parent;
    }

    public void union(int x, int y) {
        int rootX = findRoot(x);
        int rootY = findRoot(y);

        if (src[rootY].rank < src[rootX].rank) {
            src[rootY].parent = rootX;
        }
        else if (src[rootX].rank < src[rootY].rank) {
            src[rootX].parent = rootY;
        }
        else {
            src[rootY].parent = rootX;
            src[rootX].rank++;
        }
    }
}


public class Quiz3 {

    public static Edge[] mst(int V, ArrayList<Edge> edges) {
        int noOfEdges = 0;
        Subsets subsets = new Subsets(V);
        Edge[] results = new Edge[V - 1];

        for (int i = 0; noOfEdges < results.length; i++) {
            Edge nextEdge = edges.get(i);
            int x = subsets.findRoot(nextEdge.v);
            int y = subsets.findRoot(nextEdge.w);

            if (x != y) {
                results[noOfEdges] = nextEdge;
                subsets.union(x, y);
                noOfEdges++;
            }
        }

        return results;
    }

    public static double calculateWeight(Station x, Station y){

        return Math.sqrt(Math.pow(x.x - y.x, 2) + Math.pow(x.y - y.y, 2));
    }




    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        Scanner scanner = new Scanner(file);

        int testCount= Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < testCount; i++) {
            ArrayList<Station> stations = new ArrayList<>();
            ArrayList<Edge> edges = new ArrayList<>();
            int[] testInfo =
                    Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();

            for (int j = 0; j < testInfo[1]; j++) {
                int[] coords = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
                stations.add(new Station(coords[0], coords[1]));
            }

            for (int j = 0; j < stations.size(); j++) {
                for (int k = j+1; k < stations.size(); k++) {
                    edges.add(new Edge(j,k,calculateWeight(stations.get(j), stations.get(k))));
                }
            }
            edges.sort(Comparator.naturalOrder());

            Edge[] results = mst(testInfo[1], edges);

            System.out.printf("%.2f%n", results[results.length - testInfo[0]].weight);
        }
    }
}
