package lapr.project.ui.dto;

import java.util.Objects;

public class VehicleDTO{
    
    private int id;
    private int qrCode;
    private String isAvailable;
    private int weight;
    private BatteryDTO battery;
    private int actualCharge;
    
    public VehicleDTO(int id, int qrCode, int weight, BatteryDTO battery ,int actualCharge) {
        this.id = id;
        this.qrCode = qrCode;
        this.isAvailable = "true";
        this.weight = weight;
        this.battery = battery;
        this.actualCharge = actualCharge;
    }
    
    public VehicleDTO(){
        battery = new BatteryDTO();
    }

    public int getActualCharge() {
        return actualCharge;
    }

    public void setActualCharge(int actualCharge) {
        this.actualCharge = actualCharge;
    }

    /**
     * @return the id_Scooter
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param idScooter the id_Scooter to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the qrCode
     */
    public int getQrCode() {
        return this.qrCode;
    }

    /**
     * @param qrCode the qrCode to set
     */
    public void setQrCode(int qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * @return the isAvailable
     */
    public String getIsAvailable() {
        return this.isAvailable;
    }

    /**
     * @param isAvailable the isAvailable to set
     */
    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    /**
     * @return the weight
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return the battery
     */
    public BatteryDTO getBattery() {
        return this.battery;
    }

    /**
     * @param battery the battery to set
     */
    public void setBattery(BatteryDTO battery) {
        this.battery = battery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleDTO)) return false;
        VehicleDTO that = (VehicleDTO) o;
        return getId() == that.getId()
                && getQrCode() == that.getQrCode()
                && getWeight() == that.getWeight()
                && getActualCharge() == that.getActualCharge()
                && Objects.equals(getIsAvailable(), that.getIsAvailable())
                && Objects.equals(getBattery(), that.getBattery());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQrCode(), getIsAvailable(), getWeight(), getBattery(), getActualCharge());
    }
    
    
}
