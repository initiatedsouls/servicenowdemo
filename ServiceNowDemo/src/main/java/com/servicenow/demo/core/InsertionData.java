package com.servicenow.demo.core;


import java.util.ArrayList;
import java.util.List;

import com.servicenow.demo.core.vehicle.Vehicle;

public class InsertionData {


    static int NO_INDEX = -1;

    private final double insertionCost;

    private final int pickupInsertionIndex;

    private final int deliveryInsertionIndex;

    private final Vehicle selectedVehicle;


    private double departureTime;

    private double additionalTime;


    private List<String> reasons = new ArrayList<>();

    /**
     * @return the additionalTime
     */
    public double getAdditionalTime() {
        return additionalTime;
    }
    public void setAdditionalTime(double additionalTime) {
        this.additionalTime = additionalTime;
    }

    public InsertionData(double insertionCost, int pickupInsertionIndex, int deliveryInsertionIndex, Vehicle vehicle) {
        this.insertionCost = insertionCost;
        this.pickupInsertionIndex = pickupInsertionIndex;
        this.deliveryInsertionIndex = deliveryInsertionIndex;
        this.selectedVehicle = vehicle;
    }

    @Override
    public String toString() {
        return "[iCost=" + insertionCost + "][pickupIndex=" + pickupInsertionIndex + "][deliveryIndex=" + deliveryInsertionIndex + "][depTime=" + departureTime + "][vehicle=" + selectedVehicle + "]";
    }

    public int getDeliveryInsertionIndex() {
        return deliveryInsertionIndex;
    }

    public int getPickupInsertionIndex() {
        return pickupInsertionIndex;
    }

    public double getInsertionCost() {
        return insertionCost;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }
    public double getVehicleDepartureTime() {
        return departureTime;
    }

    public void setVehicleDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }


}
