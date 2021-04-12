package lapr.project.model;

import java.util.*;

/**
 * 
 * @author catarinaserrano
 */
public class Estimates {

    private final DeliveryRun deliveryRun;
    private LinkedHashMap<Address, Double> energyCostMap;
    private LinkedHashMap<Address, Double> distanceCostMap;
    private double distanceTotalLengh;
    private double requiredBatteryToCompletePath;
    private double requiredBatteryToReachNextPharmacy;
    private double timeDuration;
    
    /**
     * Constructor of deliveryRun
     * @param deliveryRun 
     */
    public Estimates(DeliveryRun deliveryRun) {
        this.deliveryRun = deliveryRun;
        energyCostMap = new LinkedHashMap<>();
        distanceCostMap = new LinkedHashMap<>();
    }

    public DeliveryRun getDeliveryRun() {
        return deliveryRun;
    }

    public LinkedHashMap<Address, Double> getDistanceCostMap() {
        return distanceCostMap;
    }

    public LinkedHashMap<Address, Double> getEnergyCostMap() {
        return energyCostMap;
    }
    
    public void setDistanceCostMap(LinkedHashMap<Address, Double> costMap) {
        this.distanceCostMap = costMap;
    }
    
    public void setEnergyCostMap(LinkedHashMap<Address, Double> costMap) {
        this.energyCostMap = costMap;
    }

    public double getRequiredBatteryToCompletePath() {
        return requiredBatteryToCompletePath;
    }

    public void setRequiredBatteryToCompletePath(double requiredBatteryToCompletePath) {
        this.requiredBatteryToCompletePath = requiredBatteryToCompletePath;
    }

    public double getRequiredBatteryToReachNextPharmacy() {
        return requiredBatteryToReachNextPharmacy;
    }

    public void setRequiredBatteryToReachNextPharmacy(double requiredBatteryToReachNextPharmacy) {
        this.requiredBatteryToReachNextPharmacy = requiredBatteryToReachNextPharmacy;
    }

    public double getDistanceTotalLengh() {
        return distanceTotalLengh;
    }

    public void setDistanceTotalLengh(double distanceTotalLengh) {
        this.distanceTotalLengh = distanceTotalLengh;
    }

    public double getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(double timeDuration) {
        this.timeDuration = timeDuration;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estimates)) return false;
        Estimates estimates = (Estimates) o;
        return Double.compare(estimates.getDistanceTotalLengh(), getDistanceTotalLengh()) == 0
                && Double.compare(estimates.getRequiredBatteryToCompletePath(), getRequiredBatteryToCompletePath()) == 0
                && Double.compare(estimates.getRequiredBatteryToReachNextPharmacy(), getRequiredBatteryToReachNextPharmacy()) == 0
                && Double.compare(estimates.getTimeDuration(), getTimeDuration()) == 0
                && Objects.equals(getDeliveryRun(), estimates.getDeliveryRun())
                && Objects.equals(getEnergyCostMap(), estimates.getEnergyCostMap())
                && Objects.equals(getDistanceCostMap(), estimates.getDistanceCostMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeliveryRun(), getEnergyCostMap(), getDistanceCostMap(), getDistanceTotalLengh(), getRequiredBatteryToCompletePath(), getRequiredBatteryToReachNextPharmacy(), getTimeDuration());
    }

    /**
     * Extracted from
     * https://stackoverflow.com/questions/36941601/how-to-check-equality-of-linkedhashmaps-in-java-also-taking-the-insertion-orde
     *
     * @param left
     * @param right
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean comparingPositions(LinkedHashMap<K, V> left, LinkedHashMap<K, V> right) {
        Iterator<Map.Entry<K, V>> leftItr = left.entrySet().iterator();
        Iterator<Map.Entry<K, V>> rightItr = right.entrySet().iterator();

        while (leftItr.hasNext() && rightItr.hasNext()) {
            Map.Entry<K, V> leftEntry = leftItr.next();
            Map.Entry<K, V> rightEntry = rightItr.next();

            //AbstractList does null checks here but for maps we can assume you never get null entries
            if (!leftEntry.equals(rightEntry)) {
                return false;
            }
        }
        return !(leftItr.hasNext() || rightItr.hasNext());
    }


    
}
