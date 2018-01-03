
package com.servicenow.demo.core.cost;

import com.servicenow.demo.core.route.activity.Activity;
import com.servicenow.demo.core.vehicle.Vehicle;

public class WaitingTimeCosts implements VehicleRoutingActivityCosts {

    @Override
    public double getActivityCost(Activity routeAct, double arrivalTime,  Vehicle vehicle) {
        if (vehicle != null) {
            double waiting = vehicle.getType().getVehicleCostParams().perWaitingTimeUnit * Math.max(0., routeAct.getTheoreticalEarliestOperationStartTime() - arrivalTime);
            double servicing = vehicle.getType().getVehicleCostParams().perServiceTimeUnit * getActivityDuration(routeAct,arrivalTime,vehicle);
            return waiting + servicing;
        }
        return 0;
    }

    @Override
    public double getActivityDuration(Activity tourAct, double arrivalTime, Vehicle vehicle) {
        return tourAct.getOperationTime();
    }

}
