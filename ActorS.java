/**
 * class to help hold both an actor name as well as some value
 */
public class ActorS {
    public String actor;
    public double separation;

    /*
    new class to hold both the frequency and character
     */
    public ActorS(String actor, double separation) {
        this.actor = actor;
        this.separation = separation;
    }

    //getter method for character
    public String getName() {
        return actor;
    }

    //getter method for frequency
    public double getSeparation() {
        return separation;
    }

    public String toString() {
        return "String " + actor + " separation " + separation;
    }
}