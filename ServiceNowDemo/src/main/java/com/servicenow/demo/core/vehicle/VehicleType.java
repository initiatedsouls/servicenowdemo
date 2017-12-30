package com.servicenow.demo.core.vehicle;

public class VehicleType {
    public static class VehicleCostParams {


        public static VehicleType.VehicleCostParams newInstance(double fix, double perTimeUnit, double perDistanceUnit) {
            return new VehicleCostParams(fix, perTimeUnit, perDistanceUnit);
        }

        public final double fix;
        public final double perTransportTimeUnit;
        public final double perDistanceUnit;
        public final double perWaitingTimeUnit;
        public final double perServiceTimeUnit;

        private VehicleCostParams(double fix, double perTimeUnit, double perDistanceUnit) {
            super();
            this.fix = fix;
            this.perTransportTimeUnit = perTimeUnit;
            this.perDistanceUnit = perDistanceUnit;
            this.perWaitingTimeUnit = 0.;
            this.perServiceTimeUnit = 0.;
        }

        public VehicleCostParams(double fix, double perTimeUnit, double perDistanceUnit, double perWaitingTimeUnit) {
            this.fix = fix;
            this.perTransportTimeUnit = perTimeUnit;
            this.perDistanceUnit = perDistanceUnit;
            this.perWaitingTimeUnit = perWaitingTimeUnit;
            this.perServiceTimeUnit = 0.;
        }

        public VehicleCostParams(double fix, double perTimeUnit, double perDistanceUnit, double perWaitingTimeUnit, double perServiceTimeUnit) {
            this.fix = fix;
            this.perTransportTimeUnit = perTimeUnit;
            this.perDistanceUnit = perDistanceUnit;
            this.perWaitingTimeUnit = perWaitingTimeUnit;
            this.perServiceTimeUnit = perServiceTimeUnit;
        }

        @Override
        public String toString() {
            return "[fixed=" + fix + "][perTime=" + perTransportTimeUnit + "][perDistance=" + perDistanceUnit + "][perWaitingTimeUnit=" + perWaitingTimeUnit + "]";
        }
    }

    public static class Builder {


        public static VehicleType.Builder newInstance(String id) {
            if (id == null) throw new IllegalArgumentException();
            return new Builder(id);
        }

        private String id;
        private double maxVelo = Double.MAX_VALUE;
        /**
         * default cost values for default vehicle type
         */
        private double fixedCost = 0.0;
        private double perDistance = 1.0;
        private double perTime = 0.0;
        private double perWaitingTime = 0.0;
        private double perServiceTime = 0.0;

        private String profile = "car";

        private double capacity = 0.0;

        private boolean dimensionAdded = false;

        private Object userData;

        private Builder(String id) {
            this.id = id;
        }

        public Builder setUserData(Object userData) {
            this.userData = userData;
            return this;
        }

        public VehicleType.Builder setMaxVelocity(double inMeterPerSeconds) {
            if (inMeterPerSeconds < 0.0)
                throw new IllegalArgumentException("The velocity of a vehicle (type) cannot be smaller than zero.");
            this.maxVelo = inMeterPerSeconds;
            return this;
        }

        public VehicleType.Builder setFixedCost(double fixedCost) {
            if (fixedCost < 0.0) throw new IllegalArgumentException("Fixed costs must not be smaller than zero.");
            this.fixedCost = fixedCost;
            return this;
        }

        public VehicleType.Builder setCostPerDistance(double perDistance) {
            if (perDistance < 0.0)
                throw new IllegalArgumentException("Cost per distance must not be smaller than zero.");
            this.perDistance = perDistance;
            return this;
        }

        public VehicleType.Builder setCostPerTransportTime(double perTime) {
            if (perTime < 0.0) throw new IllegalArgumentException();
            this.perTime = perTime;
            return this;
        }

        public VehicleType.Builder setCostPerWaitingTime(double perWaitingTime) {
            if (perWaitingTime < 0.0) throw new IllegalArgumentException();
            this.perWaitingTime = perWaitingTime;
            return this;
        }

        public VehicleType.Builder setCostPerServiceTime(double perServiceTime) {
            this.perServiceTime = perServiceTime;
            return this;
        }

        public VehicleType build() {
            return new VehicleType(this);
        }

        public Builder setCapacityDimensions(double capacity) {
            this.capacity = capacity;
            return this;
        }

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((typeId == null) ? 0 : typeId.hashCode());
        return result;
    }

    /**
     * Two vehicle-types are equal if they have the same vehicleId.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VehicleType other = (VehicleType) obj;
        if (typeId == null) {
            if (other.typeId != null)
                return false;
        } else if (!typeId.equals(other.typeId))
            return false;
        return true;
    }

    private final String typeId;


    private final String profile;

    private final VehicleType.VehicleCostParams vehicleCostParams;

    private final double capacity;

    private VehicleType(VehicleType.Builder builder) {
        typeId = builder.id;
        capacity = builder.capacity;
        vehicleCostParams = new VehicleCostParams(builder.fixedCost, builder.perTime, builder.perDistance, builder.perWaitingTime, builder.perServiceTime);
        profile = builder.profile;
    }

    public String getTypeId() {
        return typeId;
    }

    public VehicleType.VehicleCostParams getVehicleCostParams() {
        return vehicleCostParams;
    }

    @Override
    public String toString() {
        return "[typeId=" + typeId + "]" +
            "[capacity=" + capacity + "]" +
            "[costs=" + vehicleCostParams + "]";
    }

    public double getCapacity() {
        return capacity;
    }


}
