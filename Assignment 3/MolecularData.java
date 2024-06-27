import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    public static boolean listOfStructuresContainsMolecule(ArrayList<MolecularStructure> structures, String id) {
        for (MolecularStructure s : structures) if (s.hasMolecule(id)) return true;

        return false;
    }

    public void addMoleculeToStructure(MolecularStructure structure, Molecule molecule) {

        if(!structure.hasMolecule(molecule.getId())) {
            structure.addMolecule(molecule);

            for (String bondId : molecule.getBonds()) {
                if (!structure.hasMolecule(bondId))
                    addMoleculeToStructure(
                            structure,
                            molecules.stream().filter(m -> m.getId().equals(bondId)).findFirst().orElse(null)
                    );
            }
        }
    }

    public MolecularStructure createStructure(Molecule m) {
        MolecularStructure structure = new MolecularStructure();

        addMoleculeToStructure(structure, m);

        return structure;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        ArrayList<MolecularStructure> structures = new ArrayList<>();

        for (Molecule m : molecules) {
            if (!listOfStructuresContainsMolecule(structures, m.getId())) {
                boolean foundStructure = false;
                for (String bondId: m.getBonds()) {
                    for (MolecularStructure s : structures) {
                        if (s.hasMolecule(bondId)) {
                            s.addMolecule(m);
                            foundStructure = true;
                            break;
                        }
                    }
                    if (foundStructure) break;
                }

                if (!foundStructure) structures.add(createStructure(m));
            }
        }

        for (MolecularStructure s : structures) {
            Collections.sort(s.getMolecules());
        }

        return structures;
    }

    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        
        System.out.printf( "%d molecular structures have been discovered in %s.\n",
                molecularStructures.size(), species);
        for (int i = 0; i < molecularStructures.size(); i++) {

            System.out.printf("Molecules in Molecular Structure %d: %s\n", i+1, molecularStructures.get(i));
        }

    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targeStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();

            boolean didMatch;
            for (MolecularStructure target : targeStructures) {
                didMatch = false;
                for (MolecularStructure source : sourceStructures) {
                    if (target.equals(source)) {
                        didMatch = true;
                        break;
                    }
                }
                if (!didMatch) anomalyList.add(target);
            }

        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure ms: molecularStructures) {
            System.out.println(ms);
        }

    }
}
