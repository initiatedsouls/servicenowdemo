package com.servicenow.demo.core.vehicle;

import java.util.Collection;

public interface VehicleFleetManager {

    public abstract Collection<Vehicle> getAvailableVehicles();

//    public Collection<Vehicle> getAvailableVehicles(Vehicle withoutThisType);

    public Vehicle getAvailableVehicle(VehicleTypeKey vehicleTypeIdentifier);
}
