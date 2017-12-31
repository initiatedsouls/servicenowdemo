package com.servicenow.demo.core.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.servicenow.demo.core.job.Delivery;
import com.servicenow.demo.core.job.Job;
import com.servicenow.demo.core.job.Pickup;
import com.servicenow.demo.core.job.Service;
import com.servicenow.demo.core.job.Shipment;
import com.servicenow.demo.core.route.activity.Activity;
import com.servicenow.demo.core.route.activity.End;
import com.servicenow.demo.core.route.activity.JobActivityFactory;
import com.servicenow.demo.core.route.activity.Start;
import com.servicenow.demo.core.route.activity.TimeWindow;
import com.servicenow.demo.core.vehicle.Vehicle;
import com.servicenow.demo.core.route.activity.RouteActivities;
import com.servicenow.demo.core.route.activity.ServiceActivityFactory;
import com.servicenow.demo.core.route.activity.ShipmentActivityFactory;

public class VehicleRoute {

    public static VehicleRoute copyOf(VehicleRoute route) {
        if (route == null) throw new IllegalArgumentException("route must not be null");
        return new VehicleRoute(route);
    }

    public static class Builder {

        private Map<Shipment, Activity> openActivities = new HashMap<Shipment, Activity>();

        public static Builder newInstance(Vehicle vehicle) {
            if (vehicle == null)
                throw new IllegalArgumentException("null arguments not accepted. ini emptyRoute with VehicleImpl.createNoVehicle() and DriverImpl.noDriver()");
            return new Builder(vehicle);
        }

        private Vehicle vehicle;


        private Start start;

        private End end;

        private RouteActivities RouteActivities = new RouteActivities();

        private ServiceActivityFactory serviceActivityFactory = new ServiceActivityFactory();

        private ShipmentActivityFactory shipmentActivityFactory = new ShipmentActivityFactory();

        private Set<Shipment> openShipments = new HashSet<Shipment>();

        private JobActivityFactory jobActivityFactory = new JobActivityFactory() {

            @Override
            public List<Activity> createActivities(Job job) {
                List<Activity> acts = new ArrayList<Activity>();
                if (job instanceof Service) {
                    acts.add(serviceActivityFactory.createActivity((Service) job));
                } else if (job instanceof Shipment) {
                    acts.add(shipmentActivityFactory.createPickup((Shipment) job));
                    acts.add(shipmentActivityFactory.createDelivery((Shipment) job));
                }
                return acts;
            }

        };

        public Builder setJobActivityFactory(JobActivityFactory jobActivityFactory) {
            this.jobActivityFactory = jobActivityFactory;
            return this;
        }

        private Builder(Vehicle vehicle) {
            super();
            this.vehicle = vehicle;
            start = new Start(vehicle.getStartLocation(), vehicle.getEarliestDeparture(), Double.MAX_VALUE);
            start.setEndTime(vehicle.getEarliestDeparture());
            end = new End(vehicle.getEndLocation(), 0.0, vehicle.getLatestArrival());
        }

        public Builder setDepartureTime(double departureTime) {
            if (departureTime < start.getEndTime())
                throw new IllegalArgumentException("departureTime < vehicle.getEarliestDepartureTime(). this must not be.");
            start.setEndTime(departureTime);
            return this;
        }

        public Builder addService(Service service) {
            return addService(service,service.getTimeWindow());
        }

        public Builder addService(Service service, TimeWindow timeWindow) {
            if (service == null) throw new IllegalArgumentException("service must not be null");
            List<Activity> acts = jobActivityFactory.createActivities(service);
            Activity act = acts.get(0);
            act.setTheoreticalEarliestOperationStartTime(timeWindow.getStart());
            act.setTheoreticalLatestOperationStartTime(timeWindow.getEnd());
            RouteActivities.addActivity(act);
            return this;
        }

        public Builder addPickup(Pickup pickup) {
            if (pickup == null) throw new IllegalArgumentException("pickup must not be null");
            return addService(pickup);
        }

        public Builder addPickup(Pickup pickup, TimeWindow timeWindow) {
            if (pickup == null) throw new IllegalArgumentException("pickup must not be null");
            return addService(pickup,timeWindow);
        }

        public Builder addDelivery(Delivery delivery) {
            if (delivery == null) throw new IllegalArgumentException("delivery must not be null");
            return addService(delivery);
        }

        public Builder addDelivery(Delivery delivery, TimeWindow timeWindow) {
            if (delivery == null) throw new IllegalArgumentException("delivery must not be null");
            return addService(delivery,timeWindow);
        }

        public Builder addPickup(Shipment shipment) {
            return addPickup(shipment, shipment.getPickupTimeWindow());
        }

        public Builder addPickup(Shipment shipment, TimeWindow pickupTimeWindow) {
            if (openShipments.contains(shipment))
                throw new IllegalArgumentException("shipment has already been added. cannot add it twice.");
            List<Activity> acts = jobActivityFactory.createActivities(shipment);
            Activity act = acts.get(0);
            act.setTheoreticalEarliestOperationStartTime(pickupTimeWindow.getStart());
            act.setTheoreticalLatestOperationStartTime(pickupTimeWindow.getEnd());
            RouteActivities.addActivity(act);
            openShipments.add(shipment);
            openActivities.put(shipment, acts.get(1));
            return this;
        }

        public Builder addDelivery(Shipment shipment) {
            return addDelivery(shipment,shipment.getDeliveryTimeWindow());
        }

        public Builder addDelivery(Shipment shipment, TimeWindow deliveryTimeWindow) {
            if (openShipments.contains(shipment)) {
                Activity act = openActivities.get(shipment);
                act.setTheoreticalEarliestOperationStartTime(deliveryTimeWindow.getStart());
                act.setTheoreticalLatestOperationStartTime(deliveryTimeWindow.getEnd());
                RouteActivities.addActivity(act);
                openShipments.remove(shipment);
            } else {
                throw new IllegalArgumentException("cannot deliver shipment. shipment " + shipment + " needs to be picked up first.");
            }
            return this;
        }


        public VehicleRoute build() {
            if (!openShipments.isEmpty()) {
                throw new IllegalArgumentException("there are still shipments that have not been delivered yet.");
            }
            if (!vehicle.isReturnToDepot()) {
                if (!RouteActivities.isEmpty()) {
                    end.setLocation(RouteActivities.getActivities().get(RouteActivities.getActivities().size() - 1).getLocation());
                }
            }
            return new VehicleRoute(this);
        }

    }

    private RouteActivities RouteActivities;

    private Vehicle vehicle;


    private Start start;

    private End end;

    private VehicleRoute(VehicleRoute route) {
        this.start = Start.copyOf(route.getStart());
        this.end = End.copyOf(route.getEnd());
        this.RouteActivities = RouteActivities.copyOf(route.getRouteActivities());
        this.vehicle = route.getVehicle();
    }

    private VehicleRoute(Builder builder) {
        this.RouteActivities = builder.RouteActivities;
        this.vehicle = builder.vehicle;
        this.start = builder.start;
        this.end = builder.end;
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(RouteActivities.getActivities());
    }

    public RouteActivities getRouteActivities() {
        return RouteActivities;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicleAndDepartureTime(Vehicle vehicle, double vehicleDepTime) {
        this.vehicle = vehicle;
        setStartAndEnd(vehicle, vehicleDepTime);
    }

    private void setStartAndEnd(Vehicle vehicle, double vehicleDepTime) {
        if (vehicle != null) {
            if (start == null && end == null) {
                start = new Start(vehicle.getStartLocation(), vehicle.getEarliestDeparture(), vehicle.getLatestArrival());
                end = new End(vehicle.getEndLocation(), vehicle.getEarliestDeparture(), vehicle.getLatestArrival());
            }
            start.setEndTime(Math.max(vehicleDepTime, vehicle.getEarliestDeparture()));
            start.setTheoreticalEarliestOperationStartTime(vehicle.getEarliestDeparture());
            start.setTheoreticalLatestOperationStartTime(vehicle.getLatestArrival());
            start.setLocation(vehicle.getStartLocation());
            end.setLocation(vehicle.getEndLocation());
            end.setTheoreticalEarliestOperationStartTime(vehicle.getEarliestDeparture());
            end.setTheoreticalLatestOperationStartTime(vehicle.getLatestArrival());
        }

    }

    public double getDepartureTime() {
        if (start == null)
            throw new IllegalArgumentException("cannot get departureTime without having a vehicle on this route. use setVehicle(vehicle,departureTime) instead.");
        return start.getEndTime();
    }

    public boolean isEmpty() {
        return RouteActivities.isEmpty();
    }

    public Start getStart() {
        return start;
    }

    public End getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "[start=" + start + "][end=" + end + "][departureTime=" + start.getEndTime() + "][vehicle=" + vehicle + "][nuOfActs=" + RouteActivities.getActivities().size() + "]";
    }

}
