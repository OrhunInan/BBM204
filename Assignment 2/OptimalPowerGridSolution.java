import java.util.ArrayList;

/**
 * A class that represents the optimal solution for the Power Grid optimization scenario (Dynamic Programming)
 */

public class OptimalPowerGridSolution {
    private int maxNumberOfSatisfiedDemands;
    private ArrayList<Integer> hoursToDischargeBatteriesForMaxEfficiency;

    public OptimalPowerGridSolution(int maxNumberOfSatisfiedDemands, ArrayList<Integer> hoursToDischargeBatteriesForMaxEfficiency) {
        this.maxNumberOfSatisfiedDemands = maxNumberOfSatisfiedDemands;
        this.hoursToDischargeBatteriesForMaxEfficiency = hoursToDischargeBatteriesForMaxEfficiency;
    }

    /**
     * Custom constructor which calculates maxNumberOfSatisfiedDemands and hoursToDischargeBatteriesForMaxEfficiency
     * dynamically
     * @param demandSchedule demandSchedule from input file as arrayList
     */
    public OptimalPowerGridSolution(ArrayList<Integer> demandSchedule) {
        this.maxNumberOfSatisfiedDemands = 0;

        int max = 0, maxIndex = 0, temp;
        int[] sol = new int[demandSchedule.size()+1];
        ArrayList<Integer>[] hours  = new ArrayList[demandSchedule.size()+1];
        sol[0] = 0;
        hours[0] = new ArrayList<>();

        //Dynamic programming algorithm given at assignment pdf
        for (int j = 1; j <= demandSchedule.size(); j++) {
            for (int i = 0; i < j; i++) {
                temp = sol[i] + Math.min(demandSchedule.get(j-1), (int)Math.pow(j-i, 2));

                if (temp > max) {
                    max = temp;
                    maxIndex = i;
                }
            }

            sol[j] = max;
            hours[j] = (ArrayList<Integer>) hours[maxIndex].clone();
            hours[j].add(j);
            
        }

        this.maxNumberOfSatisfiedDemands = sol[sol.length-1];
        this.hoursToDischargeBatteriesForMaxEfficiency = hours[hours.length-1];
    }

    public OptimalPowerGridSolution() {
        this.maxNumberOfSatisfiedDemands = 0;
        this.hoursToDischargeBatteriesForMaxEfficiency = new ArrayList<>();
    }

    public int getmaxNumberOfSatisfiedDemands() {
        return maxNumberOfSatisfiedDemands;
    }

    public ArrayList<Integer> getHoursToDischargeBatteriesForMaxEfficiency() {
        return hoursToDischargeBatteriesForMaxEfficiency;
    }

}
