package com.servicenow.demo.core.cost;

import com.servicenow.demo.core.location.Location;
import com.servicenow.demo.core.vehicle.Vehicle;

public interface BackwardTransportCost {

    public double getBackwardTransportCost(Location from, Location to, double arrivalTime,  Vehicle vehicle);

}
