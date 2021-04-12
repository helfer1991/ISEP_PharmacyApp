package lapr.project.model;

import java.util.Objects;

/**
 * 
 * @author catarinaserrano
 */
public class Vehicle{
    
    private int id;
    private int qrCode;
    private String isAvailable;
    private int weight;
    private Battery battery;
    private int actualCharge;
    

    public Vehicle(int id, int qrCode, int weight, Battery battery ,int actualCharge) {
        this.id = id;
        this.qrCode = qrCode;
        this.isAvailable = "true";
        this.weight = weight;
        this.battery = battery;
        this.actualCharge = actualCharge;
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
     * @param id the id_Scooter to set
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
    public Battery getBattery() {
        return this.battery;
    }

    /**
     * @param battery the battery to set
     */
    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return getId() == vehicle.getId() && getQrCode() == vehicle.getQrCode()
                && getWeight() == vehicle.getWeight()
                && getActualCharge() == vehicle.getActualCharge()
                && Objects.equals(getIsAvailable(), vehicle.getIsAvailable())
                && Objects.equals(getBattery(), vehicle.getBattery());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQrCode(), getIsAvailable(), getWeight(), getBattery(), getActualCharge());
    }
}
