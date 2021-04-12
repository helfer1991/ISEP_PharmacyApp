package lapr.project.ui.dto;

import lapr.project.model.Person;

/**
 * This class represents a DTO to Person class in model in order to show the object Person in UI.
 * @author catarinaserrano
 */
public class PersonDTO extends UserDTO{
    
    private int nif;
    private String name;

    public PersonDTO(int nif, String name) {
        super();
        this.nif = nif;
        this.name = name;
    }
        
    public PersonDTO(){
        super();
    }
    
    public int getNIF() {
        return nif;
    }

    public String getName() {
        return name;
    }

    
    public void setNIF(int nif) {
        this.nif = nif;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

}
