package lapr.project.model;

import java.util.Objects;

public class Address {

    private int id;
    private double latitude;
    private double longitude;
    private String strAddress;
    private String zipcode;
    private float elevation;

    /**
     * Constructor of Address class
     * @param id
     * @param latitude
     * @param longitude
     * @param strAddress
     * @param zipcode
     * @param elevation 
     */
    public Address(int id, double latitude, double longitude, String strAddress, String zipcode, float elevation) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.strAddress = strAddress;
        this.zipcode = zipcode;
        this.elevation = elevation;
    }

    /**
     * Return id from Address
     * @return int 
     */
    public int getId() {
        return id;
    }

    /**
     * Return latitude from Address
     * @return double
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Return longitude from Address
     * @return double
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Return description from Address
     * @return String
     */
    public String getAddress() {
        return strAddress;
    }

    /**
     * Return zipCode from address
     * @return String
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * Return elevation in meters from address
     * @return 
     */
    public float getElevation() {
        return elevation;
    }

    /**
     * Change the id value of Address
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Change the latitude value of Address
     * @param latitude 
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Change the longitude value of Address
     * @param longitude 
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Change the description of Address
     * @param strAddress 
     */
    public void setAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    /**
     * Change the zipcode
     * @param zipcode 
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return getId() == address.getId()
                && Double.compare(address.getLatitude(), getLatitude()) == 0
                && Double.compare(address.getLongitude(), getLongitude()) == 0
                && Float.compare(address.getElevation(), getElevation()) == 0
                && Objects.equals(strAddress, address.strAddress)
                && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLatitude(), getLongitude(), strAddress, getZipcode(), getElevation());
    }

    @Override
    public String toString() {
        return "Address " +id +", " + strAddress;
    }
}
