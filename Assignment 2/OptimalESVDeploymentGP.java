import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This class accomplishes Mission Eco-Maintenance
 */
public class OptimalESVDeploymentGP
{
    private ArrayList<Integer> maintenanceTaskEnergyDemands;
    private ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs = new ArrayList<>();

    ArrayList<ArrayList<Integer>> getMaintenanceTasksAssignedToESVs() {
        return maintenanceTasksAssignedToESVs;
    }

    public OptimalESVDeploymentGP(ArrayList<Integer> maintenanceTaskEnergyDemands) {
        this.maintenanceTaskEnergyDemands = maintenanceTaskEnergyDemands;
    }

    public ArrayList<Integer> getMaintenanceTaskEnergyDemands() {
        return maintenanceTaskEnergyDemands;
    }

    /**
     *
     * @param maxNumberOfAvailableESVs the maximum number of available ESVs to be deployed
     * @param maxESVCapacity the maximum capacity of ESVs
     * @return the minimum number of ESVs required using first fit approach over reversely sorted items.
     * Must return -1 if all tasks can't be satisfied by the available ESVs
     */
    public int getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity)
    {
        maintenanceTaskEnergyDemands.sort(Comparator.reverseOrder());
        int[] sums = new int[maxNumberOfAvailableESVs]; //for calculating sums of arrayLists dynamically.
        Arrays.fill(sums, 0);
        int j;

        //greedy algorithm given at assignment pdf
        for (int i : maintenanceTaskEnergyDemands) {
            if (i > maxESVCapacity) return -1;

            for (j = 0; j < maintenanceTasksAssignedToESVs.size(); j++) {
                if(sums[j] + i <= maxESVCapacity) {
                    maintenanceTasksAssignedToESVs.get(j).add(i);
                    sums[j] += i;

                    break;
                }
            }
            if (j == maintenanceTasksAssignedToESVs.size()) {
                if (maintenanceTasksAssignedToESVs.size() < maxNumberOfAvailableESVs) {
                    maintenanceTasksAssignedToESVs.add(new ArrayList<>());
                    maintenanceTasksAssignedToESVs.get(maintenanceTasksAssignedToESVs.size() - 1).add(i);
                    sums[maintenanceTasksAssignedToESVs.size() - 1] = i;
                }
                else return -1;
            }
        }

        return maintenanceTasksAssignedToESVs.size();
    }
}
