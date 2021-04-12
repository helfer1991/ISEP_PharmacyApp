package lapr.project.ui.dto;


import java.util.Objects;

public class ParkDTO {

    private int id;
    private int scooterChargersNumberDTO;
    private int droneChargersNumberDTO;
    private float scooterChargerCapacityDTO;
    private float droneChargerCapacityDTO;

    public ParkDTO(int id, int scooterChargersNumber, int droneChargersNumber, float scooterChargerCapacity, float droneChargerCapacity) {
        this.id = id;
        this.scooterChargersNumberDTO = scooterChargersNumber;
        this.droneChargersNumberDTO = droneChargersNumber;
        this.scooterChargerCapacityDTO = scooterChargerCapacity;
        this.droneChargerCapacityDTO = droneChargerCapacity;
    }

    public ParkDTO(){
        
    };
    
    public int getScooterChargersNumber() {
        return scooterChargersNumberDTO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getScooterChargerCapacity() {
        return scooterChargerCapacityDTO;
    }

    public float getDroneChargerCapacity() {
        return droneChargerCapacityDTO;
    }

    public int getDroneChargersNumber() {
        return droneChargersNumberDTO;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getScooterChargersNumber(), getDroneChargersNumber(), getScooterChargerCapacity(), getDroneChargerCapacity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkDTO)) return false;
        ParkDTO parkDTO = (ParkDTO) o;
        return getId() == parkDTO.getId()
                && getScooterChargersNumber() == parkDTO.getScooterChargersNumber()
                && getDroneChargersNumber() == parkDTO.getDroneChargersNumber()
                && Float.compare(parkDTO.getScooterChargerCapacity(), getScooterChargerCapacity()) == 0
                && Float.compare(parkDTO.getDroneChargerCapacity(), getDroneChargerCapacity()) == 0;
    }


}
