package com.servicenow.demo.core.cost;

import com.servicenow.demo.core.VehicleRoutingProblemSolution;
import com.servicenow.demo.core.route.VehicleRoute;
import com.servicenow.demo.core.vehicle.Vehicle;

public class SolutionCostCalculator {


    public double getCosts(VehicleRoutingProblemSolution solution) {
        double c = 0.0;
        for (VehicleRoute r : solution.getRoutes()) {
            c += r.getRouteActivities();
            c += getFixedCosts(r.getVehicle());
        }
        c += solution.getUnassignedJobs().size() * c * .1;
        return c;
    }

    private double getFixedCosts(Vehicle vehicle) {
        if (vehicle == null) return 0.0;
        if (vehicle.getType() == null) return 0.0;
        return vehicle.getType().getVehicleCostParams().fix;
    }

}
