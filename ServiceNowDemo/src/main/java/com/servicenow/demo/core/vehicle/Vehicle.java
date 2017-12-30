package com.servicenow.demo.core.vehicle;

import com.servicenow.demo.core.location.Location;

public class Vehicle {

    private final String id;

    private final VehicleType type;

    private final double earliestDeparture;

    private final double latestArrival;

    private final boolean returnToDepot;

    private final Location endLocation;

    private final Location startLocation;
    private VehicleTypeKey vehicleIdentifier;


    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private Vehicle(Builder builder) {
        id = builder.id;
        type = builder.type;
        earliestDeparture = builder.earliestStart;
        latestArrival = builder.latestArrival;
        returnToDepot = builder.returnToDepot;
        endLocation = builder.endLocation;
        startLocation = builder.startLocation;
        setVehicleIdentifier(new VehicleTypeKey(type.getTypeId(), startLocation.getId(), endLocation.getId(), earliestDeparture, latestArrival, returnToDepot));
    }

    @Override
    public String toString() {
        return "[id=" + id + "]" +
            "[type=" + type + "]" +
            "[startLocation=" + startLocation + "]" +
            "[endLocation=" + endLocation + "]" +
            "[isReturnToDepot=" + isReturnToDepot() + "]" ;
    }


    public double getEarliestDeparture() {
        return earliestDeparture;
    }

    public double getLatestArrival() {
        return latestArrival;
    }

    public VehicleType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public boolean isReturnToDepot() {
        return returnToDepot;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }


    public VehicleTypeKey getVehicleTypeIdentifier() {
        return vehicleIdentifier;
    }

    protected void setVehicleIdentifier(VehicleTypeKey vehicleTypeIdentifier) {
        this.vehicleIdentifier = vehicleTypeIdentifier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /**
     * Two vehicles are equal if they have the same id and if their types are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vehicle other = (Vehicle) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
    

    public static class Builder {


        private String id;

        private double earliestStart = 0.0;

        private double latestArrival = Double.MAX_VALUE;

        private boolean returnToDepot = true;

        private VehicleType type = VehicleType.Builder.newInstance("default").build();

        private Location startLocation;

        private Location endLocation;


        private Builder(String id) {
            super();
            this.id = id;
            if (id == null) throw new IllegalArgumentException("Vehicle id must not be null.");
        }

        /**
         * Sets the {@link VehicleType}.<br>
         *
         * @param type the type to be set
         * @return this builder
         * @throws IllegalArgumentException if type is null
         */
        public Builder setType(VehicleType type) {
            if (type == null) throw new IllegalArgumentException("Vehicle type must not be null.");
            this.type = type;
            return this;
        }

        
        
        public Builder setReturnToDepot(boolean returnToDepot) {
            this.returnToDepot = returnToDepot;
            return this;
        }

       
        public Builder setStartLocation(Location startLocation) {
            if (startLocation == null)
                throw new IllegalArgumentException("Start location of vehicle " + id + " must not be null.");
            this.startLocation = startLocation;
            return this;
        }

        public Builder setEndLocation(Location endLocation) {
            this.endLocation = endLocation;
            return this;
        }

        public Builder setEarliestStart(double earliest_startTime) {
            if (earliest_startTime < 0)
                throw new IllegalArgumentException("The earliest start time of vehicle " + id + " must not be negative.");
            this.earliestStart = earliest_startTime;
            return this;
        }

        public Builder setLatestArrival(double latest_arrTime) {
            if (latest_arrTime < 0)
                throw new IllegalArgumentException("The latest arrival time of vehicle " + id + " must not be negative.");
            this.latestArrival = latest_arrTime;
            return this;
        }

        
        public Vehicle build() {
            if (latestArrival < earliestStart)
                throw new IllegalArgumentException("The latest arrival time of vehicle " + id + " must not be smaller than its start time.");
            if (startLocation != null && endLocation != null) {
                if (!startLocation.getId().equals(endLocation.getId()) && !returnToDepot)
                    throw new IllegalArgumentException("You specified both the end location and that the vehicle " + id + " does not need to return to its end location. This must not be. " +
                        "Either specify end location and return to depot or leave end location unspecified.");
            }
            if (startLocation != null && endLocation == null) {
                endLocation = startLocation;
            }
            if (startLocation == null && endLocation == null)
                throw new IllegalArgumentException("Every vehicle requires a start location, but vehicle " + id + " does not have one.");
            return new Vehicle(this);
        }

        public static Builder newInstance(String vehicleId) {
            return new Builder(vehicleId);
        }

    }

}
