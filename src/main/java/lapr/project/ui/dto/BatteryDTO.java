/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui.dto;

/**
 *
 * @author Asus
 */
public class BatteryDTO {
    private int id_Battery;
    private float capacity;

    public BatteryDTO() {

    }

    public BatteryDTO(int id_Battery, float capacity) {
        this.id_Battery = id_Battery;
        this.capacity = capacity;
    }

 
    /**
     * @return the id_Battery
     */
    public int getIdBattery() {
        return id_Battery;
    }

    /**
     * @param id_Battery the id_Battery to set
     */
    public void setId_Battery(int id_Battery) {
        this.id_Battery = id_Battery;
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
    public boolean equals(Object outroObjeto) {
        if (this == outroObjeto) {
            return true;
        }
        if (outroObjeto == null || this.getClass() != outroObjeto.getClass()) {
            return false;
        }
        BatteryDTO otherBattery = (BatteryDTO) outroObjeto;
        return this.id_Battery == otherBattery.id_Battery && this.capacity == otherBattery.capacity;
    }
}
