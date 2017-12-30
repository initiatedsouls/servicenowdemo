package com.servicenow.demo.core.vehicle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InfiniteFleetManager implements VehicleFleetManager{

    private Map<VehicleTypeKey, Vehicle> types = new HashMap<VehicleTypeKey, Vehicle>();


    public InfiniteFleetManager(Collection<Vehicle> vehicles) {
        extractTypes(vehicles);
    }

    @Override
    public String toString() {
        return "[name=infiniteVehicle]";
    }

    private void extractTypes(Collection<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            types.put(v.getVehicleTypeIdentifier(), v);
        }
    }


    @Override
    public Collection<Vehicle> getAvailableVehicles() {
        return types.values();
    }

    @Override
    public Vehicle getAvailableVehicle(VehicleTypeKey vehicleTypeIdentifier) {
        return types.get(vehicleTypeIdentifier);
    }

}
