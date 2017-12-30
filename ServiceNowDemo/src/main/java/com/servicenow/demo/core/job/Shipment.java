package com.servicenow.demo.core.job;


import java.util.Collection;

import com.servicenow.demo.core.location.Location;
import com.servicenow.demo.core.route.activity.TimeWindow;
import com.servicenow.demo.core.route.activity.TimeWindows;


public class Shipment implements Job {




    public static class Builder {

        private String id;

        private double pickupServiceTime = 0.0;

        private double deliveryServiceTime = 0.0;


        private int capacity;


        private String name = "no-name";

        private Location pickupLocation_;

        private Location deliveryLocation_;

        protected TimeWindows deliveryTimeWindows;

        private boolean deliveryTimeWindowAdded = false;

        private boolean pickupTimeWindowAdded = false;

        private TimeWindows pickupTimeWindows;

        private int priority = 2;

        public Object userData;

        public double maxTimeInVehicle = Double.MAX_VALUE;

        public static Builder newInstance(String id) {
            return new Builder(id);
        }

        Builder(String id) {
            if (id == null) throw new IllegalArgumentException("id must not be null");
            this.id = id;
            pickupTimeWindows = new TimeWindows();
            pickupTimeWindows.add(TimeWindow.newInstance(0.0, Double.MAX_VALUE));
            deliveryTimeWindows = new TimeWindows();
            deliveryTimeWindows.add(TimeWindow.newInstance(0.0, Double.MAX_VALUE));
        }

        public Builder setPickupLocation(Location pickupLocation) {
            this.pickupLocation_ = pickupLocation;
            return this;
        }

        public Builder setPickupServiceTime(double serviceTime) {
            if (serviceTime < 0.0)
                throw new IllegalArgumentException("The service time of a shipment must not be < 0.0.");
            this.pickupServiceTime = serviceTime;
            return this;
        }

        public Builder setPickupTimeWindow(TimeWindow timeWindow) {
            if (timeWindow == null) throw new IllegalArgumentException("The delivery time window must not be null.");
            this.pickupTimeWindows = new TimeWindows();
            this.pickupTimeWindows.add(timeWindow);
            return this;
        }



        public Builder setDeliveryLocation(Location deliveryLocation) {
            this.deliveryLocation_ = deliveryLocation;
            return this;
        }

        public Builder setDeliveryServiceTime(double deliveryServiceTime) {
            if (deliveryServiceTime < 0.0)
                throw new IllegalArgumentException("The service time of a delivery must not be < 0.0.");
            this.deliveryServiceTime = deliveryServiceTime;
            return this;
        }

        public Builder setDeliveryTimeWindow(TimeWindow timeWindow) {
            if (timeWindow == null) throw new IllegalArgumentException("The delivery time window must not be null.");
            this.deliveryTimeWindows = new TimeWindows();
            this.deliveryTimeWindows.add(timeWindow);
            return this;
        }

        public Builder addSizeDimension(int dimensionValue) {
            if (dimensionValue < 0)
                throw new IllegalArgumentException("The capacity value must not be negative, but is " + dimensionValue + ".");
            capacity=dimensionValue;
            return this;
        }
        public Shipment build() {
            if (pickupLocation_ == null) throw new IllegalArgumentException("The pickup location is missing.");
            if (deliveryLocation_ == null) throw new IllegalArgumentException("The delivery location is missing.");
            capacity = 0;
            return new Shipment(this);
        }



        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder addDeliveryTimeWindow(TimeWindow timeWindow) {
            if (timeWindow == null) throw new IllegalArgumentException("The time window must not be null.");
            if(!deliveryTimeWindowAdded){
                deliveryTimeWindows = new TimeWindows();
                deliveryTimeWindowAdded = true;
            }
            deliveryTimeWindows.add(timeWindow);
            return this;
        }

        public Builder addDeliveryTimeWindow(double earliest, double latest) {
            addDeliveryTimeWindow(TimeWindow.newInstance(earliest, latest));
            return this;
        }

        public Builder addAllDeliveryTimeWindows(Collection<TimeWindow> timeWindow) {
            for (TimeWindow tw : timeWindow) addDeliveryTimeWindow(tw);
            return this;
        }

        public Builder addPickupTimeWindow(TimeWindow timeWindow) {
            if (timeWindow == null) throw new IllegalArgumentException("The time window must not be null.");
            if(!pickupTimeWindowAdded){
                pickupTimeWindows = new TimeWindows();
                pickupTimeWindowAdded = true;
            }
            pickupTimeWindows.add(timeWindow);
            return this;
        }

        public Builder addPickupTimeWindow(double earliest, double latest) {
            return addPickupTimeWindow(TimeWindow.newInstance(earliest, latest));
        }

        public Builder addAllPickupTimeWindows(Collection<TimeWindow> timeWindow) {
            for (TimeWindow tw : timeWindow) addPickupTimeWindow(tw);
            return this;
        }

        public Builder setPriority(int priority) {
            if (priority < 1 || priority > 10)
                throw new IllegalArgumentException("The priority value is not valid. Only 1 (very high) to 10 (very low) are allowed.");
            this.priority = priority;
            return this;
        }

        public Builder setMaxTimeInVehicle(double maxTimeInVehicle){
            if (maxTimeInVehicle < 0)
                throw new IllegalArgumentException("The maximum time in vehicle must be positive.");
            this.maxTimeInVehicle = maxTimeInVehicle;
            return this;
        }
    }

    private final String id;

    private final double pickupServiceTime;

    private final double deliveryServiceTime;

    private final int capacity;

    private final String name;

    private final Location pickupLocation_;

    private final Location deliveryLocation_;

    private final TimeWindows deliveryTimeWindows;

    private final TimeWindows pickupTimeWindows;

    private final int priority;

    private final double maxTimeInVehicle;

    Shipment(Builder builder) {
        this.id = builder.id;
        this.pickupServiceTime = builder.pickupServiceTime;
        this.deliveryServiceTime = builder.deliveryServiceTime;
        this.capacity = builder.capacity;
        this.name = builder.name;
        this.pickupLocation_ = builder.pickupLocation_;
        this.deliveryLocation_ = builder.deliveryLocation_;
        this.deliveryTimeWindows = builder.deliveryTimeWindows;
        this.pickupTimeWindows = builder.pickupTimeWindows;
        this.priority = builder.priority;
        this.maxTimeInVehicle = builder.maxTimeInVehicle;
    }

    @Override
    public String getId() {
        return id;
    }

    public Location getPickupLocation() {
        return pickupLocation_;
    }

    public double getPickupServiceTime() {
        return pickupServiceTime;
    }

    public Location getDeliveryLocation() {
        return deliveryLocation_;
    }

    public double getDeliveryServiceTime() {
        return deliveryServiceTime;
    }

    public TimeWindow getDeliveryTimeWindow() {
        return deliveryTimeWindows.getTimeWindows().iterator().next();
    }

    public Collection<TimeWindow> getDeliveryTimeWindows() {
        return deliveryTimeWindows.getTimeWindows();
    }

    public TimeWindow getPickupTimeWindow() {
        return pickupTimeWindows.getTimeWindows().iterator().next();
    }

    public Collection<TimeWindow> getPickupTimeWindows() {
        return pickupTimeWindows.getTimeWindows();
    }

    @Override
    public String toString() {
        return "[id=" + id + "][name=" + name + "][pickupLocation=" + pickupLocation_
                + "][deliveryLocation=" + deliveryLocation_ + "][capacity=" + capacity
                + "][pickupServiceTime=" + pickupServiceTime + "][deliveryServiceTime="
                + deliveryServiceTime + "][pickupTimeWindows=" + pickupTimeWindows
                + "][deliveryTimeWindows=" + deliveryTimeWindows + "]";
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Shipment other = (Shipment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int getSize() {
        return capacity;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
