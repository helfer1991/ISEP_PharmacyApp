/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.Objects;

/**
 *
 * @author Asus
 */
public class Battery {
    private int idBattery;
    private float capacity;
    
    public Battery(int idBattery, float capacity) {
        this.idBattery = idBattery;
        this.capacity = capacity;
    }
    
    public Battery() {
        
    }

    /**
     * @return the id_Battery
     */
    public int getIdBattery() {
        return idBattery;
    }

    /**
     * @param idBattery the id_Battery to set
     */
    public void setIdBattery(int idBattery) {
        this.idBattery = idBattery;
    }

    /**
     * @return the capacity
     */
    public float getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdBattery(), getCapacity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Battery)) return false;
        Battery battery = (Battery) o;
        return getIdBattery() == battery.getIdBattery()
                && getCapacity() == battery.getCapacity();
    }
}
