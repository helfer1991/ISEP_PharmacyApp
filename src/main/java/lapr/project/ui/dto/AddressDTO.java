package lapr.project.ui.dto;


import java.util.Objects;

public class AddressDTO {

    private int idDTO;
    private double latitudeDTO;
    private double longitudeDTO;
    private String addressDTO;
    private String zipcodeDTO;
    private float elevationDTO;

    public AddressDTO(){

    }

    public AddressDTO(int id, double latitude, double longitude, String address, String zipcode, float elevation) {
        this.idDTO = id;
        this.latitudeDTO = latitude;
        this.longitudeDTO = longitude;
        this.addressDTO = address;
        this.zipcodeDTO = zipcode;
        this.elevationDTO = elevation;
    }


    public int getId() {
        return idDTO;
    }

    public void setId(int id) {
        this.idDTO = id;
    }

    public double getLatitude() {
        return latitudeDTO;
    }

    public void setLatitude(double latitude) {
        this.latitudeDTO = latitude;
    }

    public double getLongitude() {
        return longitudeDTO;
    }

    public void setLongitude(double longitude) {
        this.longitudeDTO = longitude;
    }

    public String getAddress() {
        return addressDTO;
    }

    public void setAddress(String address) {
        this.addressDTO = address;
    }

    public String getZipcode() {
        return zipcodeDTO;
    }

    public void setZipcode(String zipcode) {
        this.zipcodeDTO = zipcode;
    }

    public float getElevation(){
        return elevationDTO;
    }

    public void setElevation(float elevation) {
        elevationDTO = elevation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDTO)) return false;
        AddressDTO that = (AddressDTO) o;
        return getId() == that.getId()
                && Double.compare(that.getLatitude(), getLatitude()) == 0
                && Double.compare(that.getLongitude(), getLongitude()) == 0
                && Float.compare(that.getElevation(), getElevation()) == 0
                && Objects.equals(getAddress(), that.getAddress())
                && Objects.equals(getZipcode(), that.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLatitude(), getLongitude(), getAddress(), getZipcode(), getElevation());
    }
    
    @Override
    public String toString(){
        return idDTO+ " - "+ addressDTO;
    }
}
