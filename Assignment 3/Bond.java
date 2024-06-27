
// Class representing a bond between two molecules
public class Bond implements Comparable<Bond>{
    
    // Private fields
    private final Molecule to; // Molecule to which the bond is directed
    private final Molecule from; // Molecule from which the bond originates
    private final Double weight; // Weight or strength of the bond

    // Constructor
    public Bond(Molecule to, Molecule from, Double weight) {
        this.to = to;
        this.from = from;
        this.weight = weight;
    }

    // Getter for 'to' molecule
    public Molecule getTo() {
        return to;
    }

    // Getter for 'from' molecule
    public Molecule getFrom() {
        return from;
    }

    public Molecule either(){return to;}

    public Molecule other(Molecule m) {return m == from ? to : from;}

    // Getter for bond weight
    public Double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Bond o) {
        return Double.compare(this.weight, o.weight);
    }
}
