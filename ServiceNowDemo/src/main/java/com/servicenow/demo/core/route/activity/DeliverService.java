package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Delivery;
import com.servicenow.demo.core.location.Location;

public final class DeliverService  implements DeliveryActivity {

    private Delivery delivery;

    private int capacity;

    private double arrTime;

    private double endTime;

    private double theoreticalEarliest = 0;

    private double theoreticalLatest = Double.MAX_VALUE;

    private int index;

    public int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }
    public DeliverService(Delivery delivery) {
        super();
        this.delivery = delivery;
        capacity = 0-delivery.getSize();
    }

    private DeliverService(DeliverService deliveryActivity) {
        this.delivery = deliveryActivity.getJob();
        this.arrTime = deliveryActivity.getArrTime();
        this.endTime = deliveryActivity.getEndTime();
        capacity = deliveryActivity.getSize();
        setIndex(deliveryActivity.getIndex());
        this.theoreticalEarliest = deliveryActivity.getTheoreticalEarliestOperationStartTime();
        this.theoreticalLatest = deliveryActivity.getTheoreticalLatestOperationStartTime();
    }

    @Override
    public String getName() {
        return delivery.getType();
    }

    @Override
    public Location getLocation() {
        return delivery.getLocation();
    }

    @Override
    public void setTheoreticalEarliestOperationStartTime(double earliest) {
        theoreticalEarliest = earliest;
    }

    @Override
    public void setTheoreticalLatestOperationStartTime(double latest) {
        theoreticalLatest = latest;
    }


    @Override
    public double getTheoreticalEarliestOperationStartTime() {
        return theoreticalEarliest;
    }

    @Override
    public double getTheoreticalLatestOperationStartTime() {
        return theoreticalLatest;
    }

    @Override
    public double getOperationTime() {
        return delivery.getServiceDuration();
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
        return new DeliverService(this);
    }

    @Override
    public Delivery getJob() {
        return delivery;
    }

    public String toString() {
        return "[type=" + getName() + "][locationId=" + getLocation().getId()
            + "][size=" + getSize()  + "]";
    }

    @Override
    public int getSize() {
        return capacity;
    }
}
