
package com.servicenow.demo.core.cost;

import com.servicenow.demo.core.location.Location;
import com.servicenow.demo.core.vehicle.Vehicle;

public interface TransportDistance {

    public double getDistance(Location from, Location to, double departureTime, Vehicle vehicle);

}
