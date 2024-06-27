import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    public MolecularData parseData(String species, Document doc) {

        ArrayList<Molecule> molecules = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName(species + "MolecularData").item(0).getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                ArrayList<String> bonds = new ArrayList<>();
                NodeList bondList = element.getElementsByTagName("Bonds").item(0).getChildNodes();
                for (int j = 0; j < bondList.getLength(); j++) {

                    Node bond = bondList.item(j);

                    if(bond.getNodeType() == Node.ELEMENT_NODE){
                        Element bondElement = (Element) bond;

                        bonds.add(bondElement.getTextContent());
                    }
                }

                molecules.add(new Molecule(
                        element.getElementsByTagName("ID").item(0).getTextContent(),
                        Integer.parseInt(element.getElementsByTagName("BondStrength").item(0).getTextContent()),
                        bonds
                ));
            }
        }

        return new MolecularData(molecules);
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {
        try {
            File file = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            molecularDataHuman = parseData("Human", doc);
            molecularDataVitales = parseData("Vitales", doc);

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
