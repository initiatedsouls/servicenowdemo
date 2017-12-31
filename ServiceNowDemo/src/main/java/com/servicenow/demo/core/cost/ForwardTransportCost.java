package com.servicenow.demo.core.cost;

import com.servicenow.demo.core.location.Location;
import com.servicenow.demo.core.vehicle.Vehicle;

public interface ForwardTransportCost {

    public double getTransportCost(Location from, Location to, double departureTime, Vehicle vehicle);

}
