package lapr.project.ui.dto;

import java.util.Objects;

/**
 *  This class represents a DTO to Courier class in model in order to show the object Courier in UI.
 * @author catarinaserrano
 */
public class CourierDTO extends PersonDTO{

    int NIF;
    double weight;
    boolean isWorking;

    public CourierDTO(double weight, boolean isWorking) {
        super();
        this.weight = weight;
        this.isWorking = isWorking;
        this.NIF = super.getNIF();
    }
    
     public CourierDTO() {
        super();
    }
    public double getWeight() {
        return weight;
    }

    public boolean getIsWorking() {
        return isWorking;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setIsWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }

    public int getNIF() {
        this.NIF = super.getNIF();
        return NIF;
    }

    public void setNIF(int nif) {
        super.setNIF(nif);
        this.NIF = nif;
    }
    
    
    
}
