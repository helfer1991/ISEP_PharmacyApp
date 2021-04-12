package lapr.project.ui.dto;

import java.util.Objects;
import lapr.project.model.Battery;
import lapr.project.model.Vehicle;

/**
 *
 * @author Asus
 */
public class DroneDTO extends VehicleDTO {

    public DroneDTO(int id, int qrCode, int weight, BatteryDTO battery, int actualCharge) {
        super(id, qrCode, weight, battery, actualCharge);
    }
    
    public DroneDTO(){
        super();
    }

    @Override
    public String toString() {
        return "Drone " + super.getId();
    }

    
}