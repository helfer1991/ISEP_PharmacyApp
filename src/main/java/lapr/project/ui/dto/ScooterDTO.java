package lapr.project.ui.dto;



import java.util.Objects;

/**
 *
 * @author Asus
 */
public class ScooterDTO extends VehicleDTO {

    public ScooterDTO(int id, int qrCode, int weight, BatteryDTO battery, int actualCharge) {
        super(id, qrCode, weight, battery, actualCharge);
    }
    
    public ScooterDTO(){
        super();
    }

    @Override
    public String toString() {
        return "Scooter " + super.getId();
    }
    
    

}