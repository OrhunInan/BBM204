import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    public List<Station> createGraph(HyperloopTrainNetwork network) {
        List<Station> graph = new ArrayList<>();

        graph.add(network.startPoint);
        for (TrainLine l : network.lines) graph.addAll(l.trainLineStations);
        graph.add(network.destinationPoint);

        return graph;
    }

    public List<RouteDirection> createEdgeList(HyperloopTrainNetwork network, List<Station> graph) {
        List<RouteDirection> edges = new ArrayList<>();


        //creates all paths as walking paths
        for (int i = 0; i < graph.size(); i++) {
            for (int j = i+1; j < graph.size(); j++) {
                double duration = Math.sqrt(
                        Math.pow(graph.get(i).coordinates.x - graph.get(j).coordinates.x, 2)
                                +
                                Math.pow(graph.get(i).coordinates.y - graph.get(j).coordinates.y, 2)
                ) / network.averageWalkingSpeed;

                edges.add(new RouteDirection(
                        graph.get(i).description,
                        graph.get(j).description,
                        duration,
                        false
                ));
                edges.add(new RouteDirection(
                        graph.get(j).description,
                        graph.get(i).description,
                        duration,
                        false
                ));
            }
        }
        //Updates paths which ara train rides
        for (TrainLine l : network.lines){
            for (int i = 1; i < l.trainLineStations.size(); i++) {
                double duration = Math.sqrt(
                        Math.pow(l.trainLineStations.get(i).coordinates.x
                                - l.trainLineStations.get(i-1).coordinates.x, 2)
                                + Math.pow(l.trainLineStations.get(i).coordinates.y
                                - l.trainLineStations.get(i-1).coordinates.y, 2)
                ) / network.averageTrainSpeed;

                int temp = i;

                edges.stream().filter(route ->
                        route.startStationName.equals(l.trainLineStations.get(temp).description)
                                && route.endStationName.equals(l.trainLineStations.get(temp-1).description))
                        .forEach(route -> {
                            route.duration = duration;
                            route.trainRide = true;
                        });

                edges.stream().filter(route ->
                                route.startStationName.equals(l.trainLineStations.get(temp-1).description)
                                        && route.endStationName.equals(l.trainLineStations.get(temp).description))
                        .forEach(route -> {
                            route.duration = duration;
                            route.trainRide = true;
                        });
            }
        }
        Collections.sort(edges);

        return edges;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        List<Station> graph = createGraph(network);
        List<RouteDirection> edges = createEdgeList(network, graph);

        Queue<Integer> next = new LinkedList<>();
        int current = 0;
        boolean[] visited = new boolean[graph.size()];
        double[] distance = new double[graph.size()];
        Station[] shortest = new Station[graph.size()];
        shortest[0] = graph.get(0);
        Arrays.fill(visited, false);
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[0] = 0;
        next.add(current);

        //dijkstra's algorithm for finding the shortest path from start node to other nodes
        while (!next.isEmpty()) {
            current = next.poll();

            if(!visited[current]) {
                int currIndex = current;
                edges.stream()
                        .filter(route -> route.startStationName.equals(graph.get(currIndex).description))
                        .forEach(route -> {
                            {
                                int index = graph.stream()
                                        .map(station -> station.description)
                                        .collect(Collectors.toList())
                                        .indexOf(route.endStationName);

                                double temp = distance[currIndex] + route.duration;

                                if (distance[index] > temp) {

                                    distance[index] = temp;
                                    shortest[index] = graph.get(currIndex);
                                }

                                next.add(index);
                            }
                        });

                visited[current] = true;
            }
        }

        //getting and formatting the shortest path from the start node to end node
        Station s = graph.get(graph.size()-1);
        Station c;
        while (s != graph.get(0)) {

            int sIndex = graph.indexOf(s);

            c = shortest[sIndex];
            String cDesc = c.description, sDesc = s.description;

            routeDirections.add(edges.stream().filter(route ->
                    route.startStationName.equals(cDesc)
                            && route.endStationName.equals(sDesc)).findFirst().orElse(null));

            s = c;
        }
        Collections.reverse(routeDirections);

        return routeDirections;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        double result = directions.stream().mapToDouble(routeDirection -> routeDirection.duration).sum();
        System.out.printf("The fastest route takes %d minute(s).\nDirections\n----------\n", Math.round(result));

        for (int i = 0; i < directions.size(); i++) {
            System.out.printf(
                    "%d. %s from \"%s\" to \"%s\" for %.2f minutes.\n",
                    i+1,
                    directions.get(i).trainRide ? "Get on the train" : "Walk",
                    directions.get(i).startStationName,
                    directions.get(i).endStationName,
                    directions.get(i).duration
                    );
        }
    }
}