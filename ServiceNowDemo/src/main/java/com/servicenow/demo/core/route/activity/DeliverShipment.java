package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Job;
import com.servicenow.demo.core.job.Shipment;
import com.servicenow.demo.core.location.Location;

public final class DeliverShipment implements DeliveryActivity {

    private Shipment shipment;

    private double endTime;

    private double arrTime;

    private int capacity;

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
    public DeliverShipment(Shipment shipment) {
        super();
        this.shipment = shipment;
        this.capacity = 0-shipment.getSize();
    }

    private DeliverShipment(DeliverShipment deliveryShipmentActivity) {
        this.shipment = (Shipment) deliveryShipmentActivity.getJob();
        this.arrTime = deliveryShipmentActivity.getArrTime();
        this.endTime = deliveryShipmentActivity.getEndTime();
        this.capacity = deliveryShipmentActivity.getSize();
        setIndex(deliveryShipmentActivity.getIndex());
        this.earliest = deliveryShipmentActivity.getTheoreticalEarliestOperationStartTime();
        this.latest = deliveryShipmentActivity.getTheoreticalLatestOperationStartTime();
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
        return "deliverShipment";
    }

    @Override
    public Location getLocation() {
        return shipment.getDeliveryLocation();
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
        return shipment.getDeliveryServiceTime();
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
        return new DeliverShipment(this);
    }

    public String toString() {
        return "[type=" + getName() + "][locationId=" + getLocation().getId()
            + "][size=" + getSize()+ "]";
    }

    public int getSize() {
        return capacity;
    }
}
