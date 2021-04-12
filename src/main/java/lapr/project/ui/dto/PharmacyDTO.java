package lapr.project.ui.dto;

import lapr.project.model.Pharmacy;

import java.util.Objects;

public class PharmacyDTO {

    private String name;
    private int id;
    private ParkDTO park;
    private AddressDTO address;
    private float maximumPayloadCourier;
    private float minimumLoadCourier;

    public PharmacyDTO(String name, int id, ParkDTO park, AddressDTO address, float maximumPayloadCourier, float minimumLoadCourier) {
        this.name = name;
        this.id = id;
        this.park = park;
        this.address = address;
        this.maximumPayloadCourier = maximumPayloadCourier;
        this.minimumLoadCourier = minimumLoadCourier;
    }

    public PharmacyDTO() {
    }

    
//    public PharmacyDTO(Pharmacy pharmacy) {
//        this.name = pharmacy.getName();
//        this.id = pharmacy.getId();
//        this.park = new ParkDTO(pharmacy.getPark());
//        this.address =new AddressDTO(pharmacy.getAddress());
//        this.maximumPayloadCourier = pharmacy.getMaximumPayloadCourier();
//        this.minimumLoadCourier = pharmacy.getMinimumLoadCourier();
//    }

//    public PharmacyDTO(){
//        park = new ParkDTO();
//        address = new AddressDTO();
//    }
            
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ParkDTO getPark() {
        return park;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public float getMaximumPayloadCourier() {
        return maximumPayloadCourier;
    }

    public float getMinimumLoadCourier() {
        return minimumLoadCourier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPark(ParkDTO park) {
        this.park = park;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public void setMaximumPayloadCourier(float maximumPayloadCourier) {
        this.maximumPayloadCourier = maximumPayloadCourier;
    }

    public void setMinimumLoadCourier(float minimumLoadCourier) {
        this.minimumLoadCourier = minimumLoadCourier;
    }

    @Override
    public String toString(){
        return this.getId() + " - " + this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PharmacyDTO)) return false;
        PharmacyDTO that = (PharmacyDTO) o;
        return getId() == that.getId()
                && Float.compare(that.getMaximumPayloadCourier(), getMaximumPayloadCourier()) == 0
                && Float.compare(that.getMinimumLoadCourier(), getMinimumLoadCourier()) == 0
                && Objects.equals(getName(), that.getName()) && Objects.equals(getPark(), that.getPark())
                && Objects.equals(getAddress(), that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getPark(), getAddress(), getMaximumPayloadCourier(), getMinimumLoadCourier());
    }
}
