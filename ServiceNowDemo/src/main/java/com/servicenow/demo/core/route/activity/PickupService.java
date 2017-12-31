package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Pickup;
import com.servicenow.demo.core.job.Service;
import com.servicenow.demo.core.location.Location;

public final class PickupService implements PickupActivity {

    private Service pickup;

    private double arrTime;

    private double depTime;

    private double theoreticalEarliest = 0;

    private double theoreticalLatest = Double.MAX_VALUE;


    private int index;
	@Override
    public int getIndex() {
        return index;
    }
	@Override
    public void setIndex(int index) {
        this.index = index;
    }
    public PickupService(Pickup pickup) {
        super();
        this.pickup = pickup;
    }

    public PickupService(Service service) {
        this.pickup = service;
    }

    private PickupService(PickupService pickupActivity) {
        this.pickup = pickupActivity.getJob();
        this.arrTime = pickupActivity.getArrTime();
        this.depTime = pickupActivity.getEndTime();
        setIndex(pickupActivity.getIndex());
        this.theoreticalEarliest = pickupActivity.getTheoreticalEarliestOperationStartTime();
        this.theoreticalLatest = pickupActivity.getTheoreticalLatestOperationStartTime();
    }

    @Override
    public String getName() {
        return pickup.getType();
    }

    @Override
    public Location getLocation() {
        return pickup.getLocation();
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
    public void setTheoreticalEarliestOperationStartTime(double earliest) {
        this.theoreticalEarliest = earliest;
    }

    @Override
    public void setTheoreticalLatestOperationStartTime(double latest) {
        this.theoreticalLatest = latest;
    }

    @Override
    public double getOperationTime() {
        return pickup.getServiceDuration();
    }

    @Override
    public double getArrTime() {
        return arrTime;
    }

    @Override
    public double getEndTime() {
        return depTime;
    }

    @Override
    public void setArrTime(double arrTime) {
        this.arrTime = arrTime;
    }

    @Override
    public void setEndTime(double endTime) {
        this.depTime = endTime;
    }

    @Override
    public Activity duplicate() {
        return new PickupService(this);
    }

    @Override
    public Service getJob() {
        return pickup;
    }

    public String toString() {
        return "[type=" + getName() + "][locationId=" + getLocation().getId()
            + "][size=" + getSize() + "]";
    }

    @Override
    public int getSize() {
        return pickup.getSize();
    }

}
