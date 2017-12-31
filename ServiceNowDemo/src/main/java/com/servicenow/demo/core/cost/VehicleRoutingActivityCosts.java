package com.servicenow.demo.core.cost;

import com.servicenow.demo.core.route.activity.Activity;
import com.servicenow.demo.core.vehicle.Vehicle;

public interface VehicleRoutingActivityCosts {

    public static class Time {

        public static double TOUREND = -2.0;

        public static double TOURSTART = -1.0;

//        public static double UNDEFINED = -3.0;
    }

    /*public static interface Parameter {

        public double getPenaltyForMissedTimeWindow();

    }*/

    public double getActivityCost(Activity tourAct, double arrivalTime,  Vehicle vehicle);

    public double getActivityDuration(Activity tourAct, double arrivalTime, Vehicle vehicle);

}
