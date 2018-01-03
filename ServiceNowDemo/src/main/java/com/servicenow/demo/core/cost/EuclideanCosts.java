package com.servicenow.demo.core.cost;

import com.servicenow.demo.core.location.Coordinate;
import com.servicenow.demo.core.location.Location;
import com.servicenow.demo.core.vehicle.Vehicle;

public class EuclideanCosts implements VehicleRoutingTransportCosts {

    public int speed = 1;


    @Override
    public String toString() {
        return "[name=EuclideanDistanceCost]";
    }

    @Override
    public double getTransportCost(Location from, Location to, double time,  Vehicle vehicle) {
        double distance = calculateDistance(from, to);
        if (vehicle != null && vehicle.getType() != null) {
            return distance * vehicle.getType().getVehicleCostParams().perDistanceUnit;
        }
        return distance;
    }

    double calculateDistance(Location fromLocation, Location toLocation) {
        return calculateDistance(fromLocation.getCoordinate(), toLocation.getCoordinate());
    }

    double calculateDistance(Coordinate from, Coordinate to) {
        try {
            return EuclideanDistanceCalculator.calculateDistance(from, to) ;
        } catch (NullPointerException e) {
            throw new NullPointerException("cannot calculate euclidean distance. coordinates are missing. either add coordinates or use another transport-cost-calculator.");
        }
    }
    @Override
    public double getBackwardTransportCost(Location from, Location to, double arrivalTime, Vehicle vehicle) {
        return getTransportCost(from, to, arrivalTime,  vehicle);
    }


    @Override
    public double getDistance(Location from, Location to, double departureTime, Vehicle vehicle) {
            return calculateDistance(from, to);
    }


}
