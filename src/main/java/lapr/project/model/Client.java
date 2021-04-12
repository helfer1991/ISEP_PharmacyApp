package lapr.project.model;

/**
 *
 */
public class Client extends Person {

    private Address address;
    private CreditCard creditCard;
    private int credits;

    /**
     * Constructor to client
     * @param address
     * @param creditCard 
     */
    public Client(Address address, CreditCard creditCard) {
        super();
        this.address = address;
        this.creditCard = creditCard;
    }
    
    public Client(){
        super();
    }
   
    
    /**
     * @return the address
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return the credit_card
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard the credit_card to set
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }


    
}
