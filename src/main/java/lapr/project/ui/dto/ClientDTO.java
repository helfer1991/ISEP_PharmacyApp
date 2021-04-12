/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui.dto;

import lapr.project.model.Client;

import java.util.Objects;

/**
 *
 * @author Asus
 */
public class ClientDTO extends PersonDTO {
    
    private AddressDTO address;
    private CreditCardDTO creditCard;
    private int credits;
    
    public ClientDTO(AddressDTO address, CreditCardDTO creditCard) {
        super();
        this.address = address;
        this.creditCard = creditCard;
    }
    
   public ClientDTO(){
       address=new AddressDTO();
       creditCard=new CreditCardDTO();
   }
    /**
     * @return the address
     */
    public AddressDTO getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    /**
     * @return the creditCard
     */
    public CreditCardDTO getCreditCard() {
        return creditCard;
    }

    /**
     * @param credit_card the creditCard to set
     */
    public void setCreditCard(CreditCardDTO credit_card) {
        this.creditCard = credit_card;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.getEmail());
        hash = 89 * hash + Objects.hashCode(this.getPassword());
        return hash;
    }
    
    @Override
    public boolean equals(Object outroObjeto) {
        if (this == outroObjeto) {
            return true;
        }
        if (outroObjeto == null || this.getClass() != outroObjeto.getClass()) {
            return false;
        }
        ClientDTO oc = (ClientDTO) outroObjeto;
        
        if(!Objects.equals(this.getEmail(), oc.getEmail())){
            return false; 
        }
        if (!Objects.equals(this.getPassword(), oc.getPassword())) {
            return false;
        }
        if (!Objects.equals(this.getNIF(), oc.getNIF())) {
            return false;
        }
        if(!Objects.equals(this.getName(), oc.getName())){
            return false; 
        }
        if(!Objects.equals(this.getAddress().getId(), oc.getAddress().getId())){
            return false; 
        }
        if(!Objects.equals(this.getAddress().getLatitude(), oc.getAddress().getLatitude())){
            return false; 
        }
        if(!Objects.equals(this.getAddress().getLongitude(), oc.getAddress().getLongitude())){
            return false; 
        }
        if(!Objects.equals(this.getAddress().getAddress(), oc.getAddress().getAddress())){
            return false; 
        }
        if(!Objects.equals(this.getCreditCard().getNumber(), oc.getCreditCard().getNumber())){
            return false; 
        }
        if(!Objects.equals(this.getCreditCard().getCcv(), oc.getCreditCard().getCcv())){
            return false; 
        }
        if(!Objects.equals(this.getCreditCard().getValidThru(), oc.getCreditCard().getValidThru())){
            return false; 
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getEmail();
    }
    
    
    

}
