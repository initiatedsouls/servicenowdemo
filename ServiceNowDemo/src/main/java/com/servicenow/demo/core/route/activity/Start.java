package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.location.Location;

public final class Start  implements Activity {


    private final static int capacity = 0;

    public static Start newInstance(String locationId, double theoreticalStart, double theoreticalEnd) {
        return new Start(locationId, theoreticalStart, theoreticalEnd);
    }

    public static Start copyOf(Start start) {
        return new Start(start);
    }

    private String locationId;

    private double theoretical_earliestOperationStartTime;

    private double theoretical_latestOperationStartTime;

    private double endTime;

    private double arrTime;

    private Location location;

    private int index;

    public int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }
    private Start(String locationId, double theoreticalStart, double theoreticalEnd) {
        super();
        if (locationId != null) this.location = Location.Builder.newInstance().setId(locationId).build();
        this.theoretical_earliestOperationStartTime = theoreticalStart;
        this.theoretical_latestOperationStartTime = theoreticalEnd;
        this.endTime = theoreticalStart;
        setIndex(-1);
    }

    public Start(Location location, double theoreticalStart, double theoreticalEnd) {
        super();
        this.location = location;
        this.theoretical_earliestOperationStartTime = theoreticalStart;
        this.theoretical_latestOperationStartTime = theoreticalEnd;
        this.endTime = theoreticalStart;
        setIndex(-1);
    }

    private Start(Start start) {
        this.location = start.getLocation();
        theoretical_earliestOperationStartTime = start.getTheoreticalEarliestOperationStartTime();
        theoretical_latestOperationStartTime = start.getTheoreticalLatestOperationStartTime();
        endTime = start.getEndTime();
        setIndex(-1);
    }

    public double getTheoreticalEarliestOperationStartTime() {
        return theoretical_earliestOperationStartTime;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    ;

    public double getTheoreticalLatestOperationStartTime() {
        return theoretical_latestOperationStartTime;
    }


    public void setTheoreticalEarliestOperationStartTime(double time) {
        this.theoretical_earliestOperationStartTime = time;
    }

    public void setTheoreticalLatestOperationStartTime(double time) {
        this.theoretical_latestOperationStartTime = time;
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
            + "]";
    }

    @Override
    public String getName() {
        return "start";
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
        return new Start(this);
    }

    @Override
    public int getSize() {
        return capacity;
    }

}
