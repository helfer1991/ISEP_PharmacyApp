package lapr.project.ui.dto;

import lapr.project.model.CreditCard;

/**
 *
 * @author Asus
 */
public class CreditCardDTO {
    private String number;
    private int ccv;
    private String validThru;
    
    public CreditCardDTO(String number, int ccv, String validThru) {
        this.number = number;
        this.ccv = ccv;
        this.validThru = validThru;
    }

    public CreditCardDTO() {
        
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
    
    public void setValidThru(String validThru) { //missing exceptions
        this.validThru = validThru;
    }
}
