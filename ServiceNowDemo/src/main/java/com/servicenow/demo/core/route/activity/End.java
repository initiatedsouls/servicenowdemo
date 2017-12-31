package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.location.Location;

public final class End implements Activity {

    public static End newInstance(String locationId, double earliestArrival, double latestArrival) {
        return new End(locationId, earliestArrival, latestArrival);
    }

    public static End copyOf(End end) {
        return new End(end);
    }

    private final static int capacity = 0;


    private double endTime = -1;


    private double theoretical_earliestOperationStartTime;

    private double theoretical_latestOperationStartTime;

    private double arrTime;

    private Location location;


    private int index;
	@Override
    public int getIndex() {
        return index;
    }
	@Override
    public void setIndex(int index) {
        this.index = index;
    }
    public void setTheoreticalEarliestOperationStartTime(double theoreticalEarliestOperationStartTime) {
        theoretical_earliestOperationStartTime = theoreticalEarliestOperationStartTime;
    }

    public void setTheoreticalLatestOperationStartTime(double theoreticalLatestOperationStartTime) {
        theoretical_latestOperationStartTime = theoreticalLatestOperationStartTime;
    }

    public End(Location location, double theoreticalStart, double theoreticalEnd) {
        super();
        this.location = location;
        theoretical_earliestOperationStartTime = theoreticalStart;
        theoretical_latestOperationStartTime = theoreticalEnd;
        endTime = theoreticalEnd;
        setIndex(-2);
    }

    public End(String locationId, double theoreticalStart, double theoreticalEnd) {
        super();
        if (locationId != null) this.location = Location.Builder.newInstance().setId(locationId).build();
        theoretical_earliestOperationStartTime = theoreticalStart;
        theoretical_latestOperationStartTime = theoreticalEnd;
        endTime = theoreticalEnd;
        setIndex(-2);
    }

    public End(End end) {
        this.location = end.getLocation();
//		this.locationId = end.getLocation().getId();
        theoretical_earliestOperationStartTime = end.getTheoreticalEarliestOperationStartTime();
        theoretical_latestOperationStartTime = end.getTheoreticalLatestOperationStartTime();
        arrTime = end.getArrTime();
        endTime = end.getEndTime();
        setIndex(-2);
    }

    public double getTheoreticalEarliestOperationStartTime() {
        return theoretical_earliestOperationStartTime;
    }

    public double getTheoreticalLatestOperationStartTime() {
        return theoretical_latestOperationStartTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public double getOperationTime() {
        return 0.0;
    }


    @Override
    public String toString() {
        return "[type=" + getName() + "][location=" + location
            + "] ";
    }

    @Override
    public String getName() {
        return "end";
    }

    @Override
    public double getArrTime() {
        return this.arrTime;
    }

    @Override
    public void setArrTime(double arrTime) {
        this.arrTime = arrTime;

    }

    @Override
    public Activity duplicate() {
        return new End(this);
    }

    @Override
    public int getSize() {
        return capacity;
    }

}
