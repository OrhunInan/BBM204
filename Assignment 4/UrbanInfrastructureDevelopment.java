import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        for (Project p : projectList) p.printSchedule(p.getEarliestSchedule());
    }

    public ArrayList<Project> parseXML(NodeList projectList) {
        ArrayList<Project> result = new ArrayList<>();


        for (int i = 0; i < projectList.getLength(); i++) {
            Node project = projectList.item(i);

            if (project.getNodeType() == Node.ELEMENT_NODE) {
                Element projectElement = (Element) project;
                ArrayList<Task> tasks = new ArrayList<>();
                NodeList taskList = projectElement.getElementsByTagName("Tasks").item(0).getChildNodes();

                for (int j = 0; j < taskList.getLength(); j++) {
                    Node task = taskList.item(j);

                    if(task.getNodeType() == Node.ELEMENT_NODE){
                        Element taskElement = (Element) task;
                        ArrayList<Integer> dependencies = new ArrayList<>();
                        NodeList dependencyList =
                                taskElement.getElementsByTagName("Dependencies").item(0).getChildNodes();

                        for (int k = 0; k < dependencyList.getLength(); k++) {
                            Node dependency = dependencyList.item(k);

                            if(dependency.getNodeType() == Node.ELEMENT_NODE)
                                dependencies.add(Integer.parseInt(dependency.getTextContent()));
                        }

                        tasks.add(new Task(
                                Integer.parseInt(
                                        taskElement.getElementsByTagName("TaskID").item(0).getTextContent()
                                ),
                                taskElement.getElementsByTagName("Description").item(0).getTextContent(),
                                Integer.parseInt(
                                        taskElement.getElementsByTagName("Duration").item(0).getTextContent()
                                ),
                                dependencies
                        ));
                    }
                }

                result.add(new Project(
                        projectElement.getElementsByTagName("Name").item(0).getTextContent(),
                        tasks
                        ));
            }
        }

        return result;
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = null;

        try {
            File file = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList projectNodeList = doc.getElementsByTagName("Projects").item(0).getChildNodes();
            projectList = parseXML(projectNodeList);

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return projectList;
    }
}
