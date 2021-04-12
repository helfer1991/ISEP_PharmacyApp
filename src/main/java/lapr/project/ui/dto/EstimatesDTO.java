package lapr.project.ui.dto;

import java.util.*;

public class EstimatesDTO {

    private final DeliveryRunDTO deliveryRunDTO;
    private LinkedHashMap<AddressDTO, Double> energyCostMapDTO;
    private LinkedHashMap<AddressDTO, Double> distanceCostMapDTO;
    private double distanceTotalLenghDTO;
    private double requiredBatteryToCompletePathDTO;
    private double requiredBatteryToReachNextPharmacyDTO;
    private double timeDurationDTO;

    public EstimatesDTO(DeliveryRunDTO deliveryRunDTO){
        this.deliveryRunDTO = deliveryRunDTO;
        energyCostMapDTO = new LinkedHashMap<>();
        distanceCostMapDTO = new LinkedHashMap<>();
    }

    public DeliveryRunDTO getDeliveryRunDTO() {
        return deliveryRunDTO;
    }

    public LinkedHashMap<AddressDTO, Double> getEnergyCostMapDTO() {
        return energyCostMapDTO;
    }
    
    public LinkedHashMap<AddressDTO, Double> getDistanceCostMapDTO() {
        return distanceCostMapDTO;
    }

    public double getRequiredBatteryToCompletePathDTO() {
        return requiredBatteryToCompletePathDTO;
    }

    public double getRequiredBatteryToReachNextPharmacyDTO() {
        return requiredBatteryToReachNextPharmacyDTO;
    }

    public void setEnergyCostMapDTO(LinkedHashMap<AddressDTO, Double> costMapDTO) {
        this.energyCostMapDTO = costMapDTO;
    }
    
    public void setDistanceCostMapDTO(LinkedHashMap<AddressDTO, Double> costMapDTO) {
        this.distanceCostMapDTO = costMapDTO;
    }

    public double getDistanceTotalLenghDTO() {
        return distanceTotalLenghDTO;
    }

    public void setDistanceTotalLenghDTO(double distanceTotalLenghDTO) {
        this.distanceTotalLenghDTO = distanceTotalLenghDTO;
    }

    public double getTimeDurationDTO() {
        return timeDurationDTO;
    }

    public void setTimeDurationDTO(double timeDurationDTO) {
        this.timeDurationDTO = timeDurationDTO;
    }

    
    
    public void setRequiredBatteryToCompletePathDTO(double requiredBatteryToCompletePathDTO) {
        this.requiredBatteryToCompletePathDTO = requiredBatteryToCompletePathDTO;
    }

    public void setRequiredBatteryToReachNextPharmacyDTO(double requiredBatteryToReachNextPharmacyDTO) {
        this.requiredBatteryToReachNextPharmacyDTO = requiredBatteryToReachNextPharmacyDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstimatesDTO)) return false;
        EstimatesDTO that = (EstimatesDTO) o;
        return Double.compare(that.getDistanceTotalLenghDTO(), getDistanceTotalLenghDTO()) == 0
                && Double.compare(that.getRequiredBatteryToCompletePathDTO(), getRequiredBatteryToCompletePathDTO()) == 0
                && Double.compare(that.getRequiredBatteryToReachNextPharmacyDTO(), getRequiredBatteryToReachNextPharmacyDTO()) == 0
                && Double.compare(that.getTimeDurationDTO(), getTimeDurationDTO()) == 0
                && Objects.equals(getDeliveryRunDTO(), that.getDeliveryRunDTO())
                && Objects.equals(getEnergyCostMapDTO(), that.getEnergyCostMapDTO())
                && Objects.equals(getDistanceCostMapDTO(), that.getDistanceCostMapDTO());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeliveryRunDTO(), getEnergyCostMapDTO(), getDistanceCostMapDTO(),
                getDistanceTotalLenghDTO(), getRequiredBatteryToCompletePathDTO(),
                getRequiredBatteryToReachNextPharmacyDTO(), getTimeDurationDTO());
    }

    /**
     * Extracted from https://stackoverflow.com/questions/36941601/how-to-check-equality-of-linkedhashmaps-in-java-also-taking-the-insertion-orde
     * @param left
     * @param right
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean comparingPositions( LinkedHashMap<K, V> left, LinkedHashMap<K, V> right) {
        Iterator<Map.Entry<K, V>> leftItr = left.entrySet().iterator();
        Iterator<Map.Entry<K, V>> rightItr = right.entrySet().iterator();

        while ( leftItr.hasNext() && rightItr.hasNext()) {
            Map.Entry<K, V> leftEntry = leftItr.next();
            Map.Entry<K, V> rightEntry = rightItr.next();

            //AbstractList does null checks here but for maps we can assume you never get null entries
            if (! leftEntry.equals(rightEntry))
                return false;
        }
        return !(leftItr.hasNext() || rightItr.hasNext());
    }
}
