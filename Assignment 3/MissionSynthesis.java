import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    public static boolean hasCycleRec(Molecule molecule, HashMap<Molecule, ArrayList<Molecule>> adj,
                                      HashMap<Molecule, Boolean> visited, Molecule parent) {

        visited.put(molecule, true);


        for (Molecule m : adj.get(molecule)) {

            if (!visited.get(m)) {
                if (hasCycleRec(m, adj, visited, molecule))
                    return true;
            }
            else if (m != parent) return true;
        }

        return false;
    }

    public static boolean hasCycle(List<Bond> serum){
        HashMap<Molecule, ArrayList<Molecule>> adj = new HashMap<>();
        HashMap<Molecule, Boolean> visited = new HashMap<>();

        for (Bond b : serum) {
            if (!adj.containsKey(b.getTo())) adj.put(b.getTo(), new ArrayList<>());
            adj.get(b.getTo()).add(b.getFrom());

            if (!adj.containsKey(b.getFrom())) adj.put(b.getFrom(), new ArrayList<>());
            adj.get((b.getFrom())).add(b.getTo());
        }

        for(Map.Entry<Molecule, ArrayList<Molecule>> e : adj.entrySet()) {
            visited.put(e.getKey(), false);
        }

        for (Map.Entry<Molecule, ArrayList<Molecule>> e : adj.entrySet()) {

            if (!visited.get(e.getKey())) {
                if (hasCycleRec(e.getKey(), adj, visited, null))
                    return true;
            }
        }

        return false;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>(), bonds = new ArrayList<>();

        ArrayList<Molecule> molecules = new ArrayList<>();
        for (MolecularStructure h : humanStructures) {
            molecules.add(h.getMoleculeWithWeakestBondStrength());
        }
        for (MolecularStructure v : diffStructures) {
            molecules.add(v.getMoleculeWithWeakestBondStrength());
        }

        for (Molecule m1 : molecules) {
            for (Molecule m2 : molecules) {
                if (m1 != m2 )

                    if (m1.compareTo(m2) > 0)
                        bonds.add(
                            new Bond(
                                    m2,
                                    m1,
                                    (double)(m1.getBondStrength() + m2.getBondStrength())/2
                            )
                        );
                    else
                        bonds.add(
                                new Bond(
                                        m1,
                                        m2,
                                        (double)(m1.getBondStrength() + m2.getBondStrength())/2
                                )
                        );
            }
        }
        
        Collections.sort(bonds);

        for(Bond b : bonds) {
            serum.add(b);
            if (hasCycle(serum)) serum.remove(serum.size()-1);
        }

        return serum;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {

        ArrayList humans = new ArrayList<>(), vitales = new ArrayList<>();

        for (MolecularStructure h : humanStructures) {
            humans.add(h.getMoleculeWithWeakestBondStrength());
        }
        for (MolecularStructure v : diffStructures) {
            vitales.add(v.getMoleculeWithWeakestBondStrength());
        }

        double total = 0;
        System.out.println("Typical human molecules selected for synthesis: " + humans);
        System.out.println("Vitales molecules selected for synthesis: " + vitales);
        System.out.println("Synthesizing the serum...");
        for (Bond b : serum) {
            System.out.printf(
                    "Forming a bond between %s - %s with strength %.2f\n",
                    b.getTo(),
                    b.getFrom(),
                    b.getWeight()
            );
            total += b.getWeight();

        }
        System.out.println("The total serum bond strength is " + total);


    }
}
