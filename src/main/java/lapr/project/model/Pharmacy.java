package lapr.project.model;


import java.util.Objects;

public class Pharmacy {

    private String name;
    private int id;
    private Park park;
    private Address address;
    private float maximumPayloadCourier;
    private float minimumLoadCourier;

    public Pharmacy(String name, int id, Park park, Address address, float maximumPayloadCourier, float minimumLoadCourier) {
        this.id = id;
        this.name = name;
        this.park = park;
        this.address = address;
        this.maximumPayloadCourier = maximumPayloadCourier;
        this.minimumLoadCourier = minimumLoadCourier;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Park getPark() {
        return park;
    }

    public Address getAddress() {
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

    public void setPark(Park park) {
        this.park = park;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setMaximumPayloadCourier(float maximumPayloadCourier) {
        this.maximumPayloadCourier = maximumPayloadCourier;
    }

    public void setMinimumLoadCourier(float minimumLoadCourier) {
        this.minimumLoadCourier = minimumLoadCourier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pharmacy)) return false;
        Pharmacy pharmacy = (Pharmacy) o;
        return getId() == pharmacy.getId() && Float.compare(pharmacy.getMaximumPayloadCourier(), getMaximumPayloadCourier()) == 0
                && Float.compare(pharmacy.getMinimumLoadCourier(), getMinimumLoadCourier()) == 0
                && Objects.equals(getName(), pharmacy.getName())
                && Objects.equals(getPark(), pharmacy.getPark())
                && Objects.equals(getAddress(), pharmacy.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getPark(), getAddress(), getMaximumPayloadCourier(), getMinimumLoadCourier());
    }
}
