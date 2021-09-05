import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map;

/**
 Library of methods for BaconGame

 @Author Thomas Rogers, Dartmouth CS10, Spring 2020
 */

public class BaconGameLib {

    /**
     * This method takes the input files and sort them into lists by lines
     */
    public static ArrayList<String> lineList(String fileName) {
        ArrayList<String> lineList = new ArrayList<>();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = input.readLine()) != null) {
                lineList.add(line);
            }
        } catch (IOException e) {
            e.getMessage();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }
        return lineList;
    }

    //takes lists created and parses at | and creates a map
    public static Map createIDMap(ArrayList<String> lineList) {
        int i;
        Map<String, String> map = new HashMap<>();
        for (i = 0; i < lineList.size(); i++) {
            String[] line = lineList.get(i).split("\\|");
            map.put(line[0], line[1]);
        }
        return map;
    }

    //makes a map from the list of IDs
    public static Map createTogetherMap(ArrayList<String> lineList) {
        int i;
        Map<String, ArrayList<String>> map = new HashMap<>();
        for (i = 0; i < lineList.size(); i++) {
            String[] line = lineList.get(i).split("\\|");
            ArrayList<String> movies = new ArrayList<>();
            if (!map.containsKey(line[0])) {
                movies.add(line[1]);
                map.put(line[0], movies);
            } else {
                ArrayList<String> m = map.get(line[0]);
                m.add(line[1]);
            }
        }
        return map;
    }

    //creates the graph out of the 3 maps made before
    public static Graph<String, Set<String>> createGraph(Map<String, String> actorID, Map<String, String> movieID, Map<String, ArrayList<String>> movieactorsID) {
        Graph<String, Set<String>> graph = new AdjacencyMapGraph<>();

        for (String actor : actorID.keySet()) {
            graph.insertVertex(actorID.get(actor));
        }

        for (String IDmovie : movieactorsID.keySet()) {
            String movieName = movieID.get(IDmovie);
            ArrayList<String> movieList = movieactorsID.get(IDmovie);
            for (int i = 0; i < movieList.size() - 1; i++) {
                for (int j = i + 1; j < movieList.size(); j++) {
                    String actor1 = actorID.get(movieList.get(i));
                    String actor2 = actorID.get(movieList.get(j));
                    if (!graph.hasEdge(actor1, actor2)) {
                        graph.insertUndirected(actor1, actor2, new HashSet<>());
                    }
                    Set<String> set = graph.getLabel(actor1, actor2);
                    set.add(movieName);

                }
            }
        }
        return graph;
    }
}




