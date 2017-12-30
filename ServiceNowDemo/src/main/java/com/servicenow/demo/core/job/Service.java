package com.servicenow.demo.core.job;



import java.util.Collection;

import com.servicenow.demo.core.location.Coordinate;
import com.servicenow.demo.core.location.Location;
import com.servicenow.demo.core.route.activity.TimeWindow;
import com.servicenow.demo.core.route.activity.TimeWindows;

public class Service implements Job{



    public static class Builder<T extends Service> {




        public static Builder newInstance(String id) {
            return new Builder(id);
        }

        private String id;

        protected String locationId;

        private String type = "service";

        protected Coordinate coord;

        protected double serviceTime;


        protected int capacity;

        private String name = "no-name";

        protected Location location;

        protected TimeWindows timeWindows;

        private boolean twAdded = false;

        private int priority = 2;
        protected Object userData;

		protected double maxTimeInVehicle = Double.MAX_VALUE;

		Builder(String id){
			this.id = id;
			timeWindows = new TimeWindows();
			timeWindows.add(TimeWindow.newInstance(0.0, Double.MAX_VALUE));
		}

        protected Builder<T> setType(String name) {
            this.type = name;
            return this;
        }

        public Builder<T> setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder<T> setServiceTime(double serviceTime) {
            if (serviceTime < 0)
                throw new IllegalArgumentException("The service time of a service must be greater than or equal to zero.");
            this.serviceTime = serviceTime;
            return this;
        }

        public Builder<T> setUserData(Object userData) {
            this.userData = userData;
            return this;
        }

        public Builder<T> addSizeDimension(int dimensionValue) {
            if (dimensionValue < 0) throw new IllegalArgumentException("The capacity value must not be negative.");
            capacity=dimensionValue;
            return this;
        }

        public Builder<T> setTimeWindow(TimeWindow tw){
            if (tw == null) throw new IllegalArgumentException("The time window must not be null.");
            this.timeWindows = new TimeWindows();
            timeWindows.add(tw);
            return this;
        }

        public Builder<T> addTimeWindow(TimeWindow timeWindow) {
            if (timeWindow == null) throw new IllegalArgumentException("The time window must not be null.");
            if(!twAdded){
                timeWindows = new TimeWindows();
                twAdded = true;
            }
            timeWindows.add(timeWindow);
            return this;
        }

        public Builder<T> addTimeWindow(double earliest, double latest) {
            return addTimeWindow(TimeWindow.newInstance(earliest, latest));
        }

        public Builder<T> addAllTimeWindows(Collection<TimeWindow> timeWindows) {
            for (TimeWindow tw : timeWindows) addTimeWindow(tw);
            return this;
        }

        public T build() {
            if (location == null) throw new IllegalArgumentException("The location of service " + id + " is missing.");
            this.setType("service");
            capacity = 0;
            return (T) new Service(this);
        }

        public Builder<T> setName(String name) {
            this.name = name;
            return this;
        }
        public Builder<T> setPriority(int priority) {
            if (priority < 1 || priority > 10)
                throw new IllegalArgumentException("The priority value is not valid. Only 1 (very high) to 10 (very low) are allowed.");
            this.priority = priority;
            return this;
        }

    }

    private final String id;

    private final String type;

    private final double serviceTime;

    private final int size;

    private final String name;

    private final Location location;

    private final TimeWindows timeWindows;

    private final int priority;

    Service(Builder<?> builder) {
        id = builder.id;
        serviceTime = builder.serviceTime;
        type = builder.type;
        size = builder.capacity;
        name = builder.name;
        location = builder.location;
        timeWindows = builder.timeWindows;
        priority = builder.priority;
	}

    public Collection<TimeWindow> getTimeWindows(){
        return timeWindows.getTimeWindows();
    }

    @Override
    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }


    public double getServiceDuration() {
        return serviceTime;
    }

    public TimeWindow getTimeWindow() {
        return timeWindows.getTimeWindows().iterator().next();
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[id=" + id + "][name=" + name + "][type=" + type + "][location=" + location
                + "][capacity=" + size + "][serviceTime=" + serviceTime + "][timeWindows="
                + timeWindows + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * Two services are equal if they have the same id.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Service other = (Service) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int getSize() {
        return size;
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
