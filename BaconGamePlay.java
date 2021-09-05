import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

/**
 Plays Bacon Game Based on key pressed
 Commands are from PSet 4 webpage

 @Author Thomas Rogers, Dartmouth CS10, Spring 2020
 */

public class BaconGamePlay {

    /**
     * play method handles the key presses and begins the run of the game
     */
    public static void play() {
        String centerOfUniverse = "Kevin Bacon";
        String destination;

        Graph<String, Set<String>> graph = newGraph();
        Scanner in = new Scanner(System.in);
        System.out.println("Commands:\n" +
                "c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\n" +
                "d <low> <high>: list actors sorted by degree, with degree between low and high\n" +
                "i: list actors with infinite separation from the current center\n" +
                "p <name>: find path from <name> to current center of the universe\n" +
                "s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\n" +
                "u <name>: make <name> the center of the universe\n" +
                "q: quit game");

        //while program is sunning
        while (true) {
            System.out.println("Type your command:");
            String line = in.nextLine();
            char command = line.charAt(0);

            //sets center of universe and tells connects and average separation
            if (command == ('u')) {
                centerOfUniverse = line.substring(2);
                if (!graph.hasVertex(centerOfUniverse)) {
                    System.out.println("Name is not recognized, please try another");
                    continue;
                }

                Graph<String, Set<String>> pathTree = GraphLib.bfs(graph, centerOfUniverse);
                double averageSeparation = GraphLib.averageSeparation(pathTree, centerOfUniverse);
                int graphSize = pathTree.numVertices();
                System.out.println(centerOfUniverse + " is connected to " + ((graphSize)) + "/" + (graph.numVertices()) + " other actors with an average separation of " + averageSeparation);
            }

            //sets new destination and begins the game
            if (command == ('p')) {
                destination = line.substring(2);
                if (!graph.hasVertex(destination)) {
                    System.out.println("Name is not recognized, please try another");
                    continue;
                }
                runGame(graph, centerOfUniverse, destination);
            }

            //shows all actors not connected to center
            if (command == ('i')) {
                listNotConnected(graph, centerOfUniverse);
            }

            //finds best or worst centers
            if (command == ('c')) {
                String num = line.substring(2);
                int numInt = Integer.parseInt(num);
                bestCenters(graph, numInt);
            }

            //lists actors with wanted degrees
            if (command == ('d')) {
                String low = line.substring(2, 3);
                String high = line.substring(4);
                sortByDegree(graph, high, low);
            }

            //finds wanted actors with a certain separation
            if (command == ('s')) {
                String low = line.substring(2,3);
                String high = line.substring(4);
                sortBySeparation(graph, centerOfUniverse, high, low);
            }

            //quits game
            if (command == ('q')) {
                System.out.println("Thanks for playing! See you next time!");
                System.exit(0);
            }
            else if (command != 'u' && command != 'p' && command != 'i' && command != 'c' && command != 'd' && command != 's'){
                System.out.println("invalid input");
            }

        }
    }

    //creates a new graph with all actors connected and returns it
    public static Graph<String, Set<String>> newGraph() {
        ArrayList<String> movieTest = BaconGameLib.lineList("BaconText/moviesTest.txt");

        ArrayList<String> actorTest = BaconGameLib.lineList("BaconText/actorsTest.txt");

        ArrayList<String> movieactorsTest = BaconGameLib.lineList("BaconText/movie-actorsTest.txt");


        Graph<String, Set<String>> graph = BaconGameLib.createGraph(BaconGameLib.createIDMap(actorTest), BaconGameLib.createIDMap(movieTest), BaconGameLib.createTogetherMap(movieactorsTest));

        return graph;
    }

    //takes a destination and plays game by running bfs and find path between the two
    public static void runGame(Graph<String, Set<String>>graph, String centerOfUniverse, String destination) {

        Graph<String, Set<String>> pathTree = GraphLib.bfs(graph, centerOfUniverse);

        if (pathTree != null) {
            ArrayList<String> path = GraphLib.getPath(pathTree, destination);
            for (int i = 0; i < path.size() - 1; i++) {
                System.out.println(path.get(i) + " appeared in " + pathTree.getLabel(path.get(i), path.get(i+1)) + " with " + path.get(i+1));
            }
            System.out.println(destination + "'s number is " + (path.size() - 1));
        }


    }

    //method to find the actors not connected to the center of the universe
    public static void listNotConnected(Graph<String, Set<String>> graph, String centerOfUniverse) {


        Graph<String, Set<String>> pathTree = GraphLib.bfs(graph, centerOfUniverse);

        Set<String> notIncluded = GraphLib.missingVertices(graph, pathTree);

        System.out.println("Actors not connected to " + centerOfUniverse + ":");

        //list all actors
        for (String name : notIncluded) {
            System.out.println(name);
        }

    }

    //method for key press c
    public static void bestCenters(Graph<String, Set<String>> graph, int num) {

        ArrayList<ActorS> list = new ArrayList<>();

        //find average separation for all actors
        for (String actor : graph.vertices()) {
            Graph<String, Set<String>> pathTree = GraphLib.bfs(graph, actor);
            if (pathTree.hasVertex("Kevin Bacon")) {
                ActorS newActor = new ActorS(actor, GraphLib.averageSeparation(pathTree, actor));
                list.add(newActor);
            }
        }

        //take first or last num
        System.out.println(num);
        list.sort(new ActorSComparator());
        System.out.println("The centers you asked for are: ");
        if (num > 0) {
            for (int i = list.size() - 1; i > list.size() - num - 1; i--) {
                ActorS actor = list.get(i);
                System.out.println(actor.getName() + " has an average separation of " + actor.getSeparation());
            }
        }
        else {
            for (int i = 0; i < -num; i++) {
                ActorS actor = list.get(i);
                System.out.println(actor.getName() + " has an average separation of " + actor.getSeparation());
            }
        }
    }

    //sorts and returns actors between two numbers by the direct number of connections it has
    public static void sortByDegree(Graph<String, Set<String>> graph, String high, String low) {
        int lowN = Integer.parseInt(low);
        int highN = Integer.parseInt(high);
        ArrayList<ActorS> list = new ArrayList<>();
        for (String actor : graph.vertices()) {
            double degree = graph.outDegree(actor);
            ActorS newActor = new ActorS(actor, degree);
            list.add(newActor);
        }
        list.sort(new ActorSComparator());
        System.out.println("These actors have a degree between " + highN + " and " + lowN);
        for (int i =0; i < list.size() - 1; i++) {
            ActorS actor = list.get(i);
            if ((actor.getSeparation() <= highN) && (actor.getSeparation() >= lowN)) {
                System.out.println(actor.getName() + " has a degree of " + actor.getSeparation());
            }
        }
    }


    //sorts and return the actors within two numbers which are a certain degree from the center
    public static void sortBySeparation(Graph<String, Set<String>> graph, String centerOfUniverse, String high, String low) {
        int highN = Integer.parseInt(high);
        int lowN = Integer.parseInt(low);
        ArrayList<ActorS> list = new ArrayList<>();
        Graph<String, Set<String>> pathTree = GraphLib.bfs(graph, centerOfUniverse);
        if (pathTree != null) {
            for (String actor : pathTree.vertices()) {
                ArrayList<String> path = GraphLib.getPath(pathTree, actor);
                int pathLength = path.size();
                ActorS newactor = new ActorS(actor, pathLength);
                list.add(newactor);
            }
        }
        list.sort(new ActorSComparator());
        System.out.println("These actors have an separation between " + highN + " and " + lowN);
        for (int i =0; i < list.size() - 1; i++) {
            ActorS actor = list.get(i);
            if ((actor.getSeparation()-1 <= highN) && (actor.getSeparation() -1 >= lowN)) {
                System.out.println(actor.getName() + " has a separation of " + (actor.getSeparation()-1));
            }
        }
    }

    //calls play
    public static void main(String [] args) {
        play();
    }

}
