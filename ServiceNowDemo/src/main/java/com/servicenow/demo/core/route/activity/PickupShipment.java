package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Job;
import com.servicenow.demo.core.job.Shipment;
import com.servicenow.demo.core.location.Location;

public final class PickupShipment implements PickupActivity{

    private Shipment shipment;

    private double endTime;

    private double arrTime;

    private double earliest = 0;

    private double latest = Double.MAX_VALUE;


    private int index;
	@Override
    public int getIndex() {
        return index;
    }
	@Override
    public void setIndex(int index) {
        this.index = index;
    }
    public PickupShipment(Shipment shipment) {
        super();
        this.shipment = shipment;
    }

    private PickupShipment(PickupShipment pickupShipmentActivity) {
        this.shipment = (Shipment) pickupShipmentActivity.getJob();
        this.arrTime = pickupShipmentActivity.getArrTime();
        this.endTime = pickupShipmentActivity.getEndTime();
        setIndex(pickupShipmentActivity.getIndex());
        this.earliest = pickupShipmentActivity.getTheoreticalEarliestOperationStartTime();
        this.latest = pickupShipmentActivity.getTheoreticalLatestOperationStartTime();
    }

    @Override
    public Job getJob() {
        return shipment;
    }

    @Override
    public void setTheoreticalEarliestOperationStartTime(double earliest) {
        this.earliest = earliest;
    }

    @Override
    public void setTheoreticalLatestOperationStartTime(double latest) {
        this.latest = latest;
    }

    @Override
    public String getName() {
        return "pickupShipment";
    }

    @Override
    public Location getLocation() {
        return shipment.getPickupLocation();
    }

    @Override
    public double getTheoreticalEarliestOperationStartTime() {
        return earliest;
    }

    @Override
    public double getTheoreticalLatestOperationStartTime() {
        return latest;
    }

    @Override
    public double getOperationTime() {
        return shipment.getPickupServiceTime();
    }

    @Override
    public double getArrTime() {
        return arrTime;
    }

    @Override
    public double getEndTime() {
        return endTime;
    }

    @Override
    public void setArrTime(double arrTime) {
        this.arrTime = arrTime;
    }

    @Override
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    @Override
    public Activity duplicate() {
        return new PickupShipment(this);
    }

    public String toString() {
        return "[type=" + getName() + "][locationId=" + getLocation().getId()
            + "][size=" + getSize()+ "]";
    }

    @Override
    public int getSize() {
        return shipment.getSize();
    }


}
