/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.Objects;


/**
 *
 * @author catarinaserrano
 */
public class Road {

    private int id;
    private final Address addressOrig;
    private final Address addressDest;


    /**
     * Constructor to road
     * @param id
     * @param addressOrig
     * @param addressDest
     */
    public Road(int id, Address addressOrig, Address addressDest) {
        this.id = id;
        this.addressOrig = addressOrig;
        this.addressDest = addressDest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public Address getAddressOrig() {
        return addressOrig;
    }

    public Address getAddressDest() {
        return addressDest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;
        Road road = (Road) o;
        return getId() == road.getId()
                && Objects.equals(getAddressOrig(), road.getAddressOrig())
                && Objects.equals(getAddressDest(), road.getAddressDest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAddressOrig(), getAddressDest());
    }
}
