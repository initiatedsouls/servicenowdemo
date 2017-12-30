package com.servicenow.demo.core.vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FiniteFleetManagerFactory implements VehicleFleetManagerFactory {

    private Collection<Vehicle> vehicles;

    private Random random = new Random(5217);

    public FiniteFleetManagerFactory(Collection<Vehicle> vehicles) {
        super();
        this.vehicles = vehicles;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public VehicleFleetManager createFleetManager() {
        if (vehicles == null) throw new IllegalStateException("vehicles is null. this must not be.");
        if (vehicles.isEmpty()) throw new IllegalStateException("vehicle-collection is empty. this must not be");
        FiniteFleetManager vehicleFleetManager = new FiniteFleetManager(vehicles);
        vehicleFleetManager.setRandom(random);
        vehicleFleetManager.init();
        return vehicleFleetManager;
    }

}
