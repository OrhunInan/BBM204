import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static int getSum(ArrayList<Integer> src){
        int sum = 0;
        for(Integer i : src) sum += i;
        return sum;
    }

    public static void main(String[] args) throws IOException {
        //Input reading
        File file1 = new File(args[0]);
        Scanner scanner1 = new Scanner(file1);
        int[] demandScheduleAsIntArr =
                Arrays.stream(scanner1.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();

        //converting input array to arrayList
        ArrayList<Integer> demandSchedule = new ArrayList<>();
        for (int i : demandScheduleAsIntArr) demandSchedule.add(i);

        //calculations
        OptimalPowerGridSolution optimalPowerGridSolution = new OptimalPowerGridSolution(demandSchedule);

        //output
        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        System.out.println("The total number of demanded gigawatts: " + getSum(demandSchedule));
        System.out.println(
                "Maximum number of satisfied gigawatts: " +
                        optimalPowerGridSolution.getmaxNumberOfSatisfiedDemands()
        );
        System.out.print("Hours at which the battery bank should be discharged: ");
        for (int i = 0; i < optimalPowerGridSolution.getHoursToDischargeBatteriesForMaxEfficiency().size(); i++) {
            System.out.print(optimalPowerGridSolution.getHoursToDischargeBatteriesForMaxEfficiency().get(i));
            if (i != optimalPowerGridSolution.getHoursToDischargeBatteriesForMaxEfficiency().size() - 1)
                System.out.print(", ");
            else System.out.println();
        }
        System.out.println(
                "The number of unsatisfied gigawatts: " +
                        (getSum(demandSchedule) -
                                optimalPowerGridSolution.getmaxNumberOfSatisfiedDemands())
        );
        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        //Input reading
        File file2 = new File(args[1]);
        Scanner scanner2 = new Scanner(file2);
        int[] firstLine = Arrays.stream(scanner2.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        int[] secondLine =
                        Arrays.stream(scanner2.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();

        //Converting input array to arrayList
        ArrayList<Integer> maintenanceTaskEnergyDemands = new ArrayList<>();
        for (int i : secondLine) maintenanceTaskEnergyDemands.add(i);

        //calculations
        OptimalESVDeploymentGP optimalESVDeploymentGP = new OptimalESVDeploymentGP(maintenanceTaskEnergyDemands);
        int res = optimalESVDeploymentGP.getMinNumESVsToDeploy(firstLine[0], firstLine[1]);

        //output
        System.out.println("##MISSION ECO-MAINTENANCE##");
        if (res == -1) System.out.println("Warning: Mission Eco-Maintenance Failed.");
        else {
            System.out.println("The minimum number of ESVs to deploy: " + res);
            for (int i = 0; i < res; i++) {
                System.out.println(
                        "ESV " + (i + 1) + " tasks: " + optimalESVDeploymentGP.getMaintenanceTasksAssignedToESVs().get(i)
                );
            }
        }
        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
}
