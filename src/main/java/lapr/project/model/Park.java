package lapr.project.model;

import java.util.Objects;

public class Park {

    private int id;
    private int scooterChargersNumber;
    private int droneChargersNumber;
    private float scooterChargerCapacity;
    private float droneChargerCapacity;

    public Park(int id, int scooterChargersNumber, int droneChargersNumber, float scooterChargerCapacity, float droneChargerCapacity) {
        this.id = id;
        this.scooterChargersNumber = scooterChargersNumber;
        this.droneChargersNumber = droneChargersNumber;
        this.scooterChargerCapacity = scooterChargerCapacity;
        this.droneChargerCapacity = droneChargerCapacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScooterChargersNumber() {
        return scooterChargersNumber;
    }

    public void setScooterChargersNumber(int scooterChargersNumber) {
        this.scooterChargersNumber = scooterChargersNumber;
    }

    public int getDroneChargersNumber() {
        return droneChargersNumber;
    }

    public void setDroneChargersNumber(int droneChargersNumber) {
        this.droneChargersNumber = droneChargersNumber;
    }

    public float getScooterChargerCapacity() {
        return scooterChargerCapacity;
    }

    public void setScooterChargerCapacity(float scooterChargerCapacity) {
        this.scooterChargerCapacity = scooterChargerCapacity;
    }

    public float getDroneChargerCapacity() {
        return droneChargerCapacity;
    }

    public void setDroneChargerCapacity(float droneChargerCapacity) {
        this.droneChargerCapacity = droneChargerCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Park)) return false;
        Park park = (Park) o;
        return getId() == park.getId()
                && getScooterChargersNumber() == park.getScooterChargersNumber()
                && getDroneChargersNumber() == park.getDroneChargersNumber()
                && Float.compare(park.getScooterChargerCapacity(), getScooterChargerCapacity()) == 0
                && Float.compare(park.getDroneChargerCapacity(), getDroneChargerCapacity()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getScooterChargersNumber(),
                getDroneChargersNumber(),
                getScooterChargerCapacity(),
                getDroneChargerCapacity());
    }
}
