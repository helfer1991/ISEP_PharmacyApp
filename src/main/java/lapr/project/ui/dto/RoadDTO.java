package lapr.project.ui.dto;
import lapr.project.model.*;
import java.util.Objects;


/**
 *
 * @author catarinaserrano
 */
public class RoadDTO {
    
    private int id;
    private AddressDTO addressOrig;
    private AddressDTO addressDest;
    // attrition , dependet of road conditions, used to calculate Fr(road load (friction))
    

    /**
     * Constructor to road
     * @param addressOrig
     * @param addressDest
     */
    public RoadDTO(int id, AddressDTO addressOrig, AddressDTO addressDest) {
        this.id = id;
        this.addressOrig = addressOrig;
        this.addressDest = addressDest;
    }

    public RoadDTO() {
        id =0;
        addressOrig = new AddressDTO();
        addressDest = new AddressDTO();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddressOrig(AddressDTO a) {
        this.addressOrig = a;
    }

    public void setAddressDest(AddressDTO a) {
        this.addressDest = a;
    }

    public AddressDTO getAddressOrig() {
        return addressOrig;
    }

    public AddressDTO getAddressDest() {
        return addressDest;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoadDTO)) return false;
        RoadDTO roadDTO = (RoadDTO) o;
        return getId() == roadDTO.getId()
                && Objects.equals(getAddressOrig(), roadDTO.getAddressOrig())
                && Objects.equals(getAddressDest(), roadDTO.getAddressDest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAddressOrig(), getAddressDest());
    }
}
