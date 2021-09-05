import java.util.Comparator;

/**
 Compares the separations or other values for actors

 @Author Thomas Rogers, Dartmouth CS10, Spring 2020
 */

public class ActorSComparator implements Comparator<ActorS> {
    public int compare(ActorS a1, ActorS a2) {
        return Double.compare(a1.getSeparation(), a2.getSeparation());
    }
}