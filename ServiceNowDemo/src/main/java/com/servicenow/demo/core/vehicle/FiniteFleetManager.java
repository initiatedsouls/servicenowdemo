package com.servicenow.demo.core.vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class FiniteFleetManager implements VehicleFleetManager{

    public FiniteFleetManager newInstance(Collection<Vehicle> vehicles) {
        return new FiniteFleetManager(vehicles);
    }

    static class TypeContainer {

        private ArrayList<Vehicle> vehicleList;

        private int index = 0;

        TypeContainer() {
            super();
            vehicleList = new ArrayList<Vehicle>();
        }

        void add(Vehicle vehicle) {
            if (vehicleList.contains(vehicle)) {
                throw new IllegalStateException("cannot add vehicle twice " + vehicle.getId());
            }
            vehicleList.add(vehicle);
        }

        void remove(Vehicle vehicle) {
            vehicleList.remove(vehicle);
        }

        Vehicle getVehicle() {
            if(index >= vehicleList.size()) index = 0;
            Vehicle vehicle = vehicleList.get(index);
            return vehicle;
        }

        void incIndex(){
            index++;
        }

        boolean isEmpty() {
            return vehicleList.isEmpty();
        }

    }


    private Collection<Vehicle> vehicles;

    private TypeContainer[] vehicleTypes;

    private boolean[] locked;

    private Vehicle[] vehicleArr;

    private Random random;

    FiniteFleetManager(Collection<Vehicle> vehicles) {
        super();
        this.vehicles = vehicles;
        int arrSize = vehicles.size() + 2;
        locked = new boolean[arrSize];
        vehicleArr = new Vehicle[arrSize];
    }

    void setRandom(Random random) {
        this.random = random;
    }

    void init(){
        initializeVehicleTypes();
    }

    @Override
    public String toString() {
        return "[name=finiteVehicles]";
    }

    private void initializeVehicleTypes() {
        int maxTypeIndex = 0;
        for(Vehicle v : vehicles){
            if(v.getVehicleTypeIdentifier().getIndex() > maxTypeIndex){
                maxTypeIndex = v.getVehicleTypeIdentifier().getIndex();
            }
        }
        vehicleTypes = new TypeContainer[maxTypeIndex+1];
        for(int i=0;i< vehicleTypes.length;i++){
            TypeContainer typeContainer = new TypeContainer();
            vehicleTypes[i] = typeContainer;
        }
        for (Vehicle v : vehicles) {
            vehicleArr[v.getIndex()]=v;
            addVehicle(v);
        }
    }

    private void addVehicle(Vehicle v) {
        if (v.getType() == null) {
            throw new IllegalStateException("vehicle needs type");
        }
        vehicleTypes[v.getVehicleTypeIdentifier().getIndex()].add(v);
    }

    private void removeVehicle(Vehicle v) {
        vehicleTypes[v.getVehicleTypeIdentifier().getIndex()].remove(v);
    }

    @Override
    public Collection<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        for(int i=0;i< vehicleTypes.length;i++){
            if(!vehicleTypes[i].isEmpty()){
                vehicles.add(vehicleTypes[i].getVehicle());
            }
        }
        return vehicles;
    }


    @Override
    public Vehicle getAvailableVehicle(VehicleTypeKey vehicleTypeIdentifier) {
        if(!vehicleTypes[vehicleTypeIdentifier.getIndex()].isEmpty()){
            return vehicleTypes[vehicleTypeIdentifier.getIndex()].getVehicle();
        }
        return null;
    }

    public void lock(Vehicle vehicle) {
        if(locked[vehicle.getIndex()]){
            throw new IllegalStateException("cannot lock vehicle twice " + vehicle.getId());
        }
        else{
            locked[vehicle.getIndex()] = true;
            removeVehicle(vehicle);
        }
    }

    public void unlock(Vehicle vehicle) {
        locked[vehicle.getIndex()] = false;
        addVehicle(vehicle);
    }

    public boolean isLocked(Vehicle vehicle) {
        return locked[vehicle.getIndex()];
    }

    public void unlockAll() {
        for(int i=0;i<vehicleArr.length;i++){
            if(locked[i]){
                unlock(vehicleArr[i]);
            }
        }
        for(int i=0;i<vehicleTypes.length;i++){
            vehicleTypes[i].incIndex();
        }
    }

}
