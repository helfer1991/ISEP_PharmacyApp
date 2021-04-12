package lapr.project.model;

import java.util.Objects;

/**
 *
 * @author Asus
 */
public class CreditCard {
    private String number;
    private int ccv;
    private final String validThru;
    
    public CreditCard(String number, int ccv, String validThru) {
        this.number = number;
        this.ccv = ccv;
        this.validThru = validThru;
    }
    
    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the ccv
     */
    public int getCcv() {
        return ccv;
    }

    /**
     * @param ccv the ccv to set
     */
    public void setCcv(int ccv) {
        this.ccv = ccv;
    }
    
    public String getValidThru() {
        return validThru;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return getNumber() == that.getNumber() && getCcv() == that.getCcv() && Objects.equals(validThru, that.validThru);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getCcv(), validThru);
    }
}
