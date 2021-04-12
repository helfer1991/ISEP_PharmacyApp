package lapr.project.model;

import java.util.Objects;

/**
 *
 * @author catarinaserrano
 */
public class Courier extends Person{
    double weight;
    Boolean isWorking;
    
    /**
     * Initialize Courier with the received parameters.
     * @param weight
     * @param isWorking 
     */
    public Courier(double weight, boolean isWorking) {
        super();
        this.weight = weight;
        this.isWorking = isWorking;
    }
    
    /**
     * Returns the weight
     * @return weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Returns if boolean, where true he is working and false he is not working
     * @return isWorking
     */
    public Boolean getIsWorking() {
        return isWorking;
    }

    
    /**
     * Set the weight
     * @param weight 
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Set if the courier is working
     * @param isWorking 
     */
    public void setIsWorking(Boolean isWorking) {
        this.isWorking = isWorking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Courier)) return false;
        if (!super.equals(o)) return false;
        Courier courier = (Courier) o;
        return Double.compare(courier.getWeight(), getWeight()) == 0 && Objects.equals(getIsWorking(), courier.getIsWorking());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWeight(), getIsWorking());
    }
}
