package com.servicenow.demo.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManagerAutoConfiguration;

import com.servicenow.demo.core.cost.EuclideanCosts;
import com.servicenow.demo.core.cost.EuclideanDistanceCalculator;
import com.servicenow.demo.core.cost.VehicleRoutingActivityCosts;
import com.servicenow.demo.core.cost.VehicleRoutingTransportCosts;
import com.servicenow.demo.core.cost.WaitingTimeCosts;
import com.servicenow.demo.core.job.Job;
import com.servicenow.demo.core.job.Service;
import com.servicenow.demo.core.job.Shipment;
import com.servicenow.demo.core.location.Coordinate;
import com.servicenow.demo.core.location.Location;
import com.servicenow.demo.core.route.VehicleRoute;
import com.servicenow.demo.core.route.activity.Activity;
import com.servicenow.demo.core.route.activity.JobActivityFactory;
import com.servicenow.demo.core.route.activity.ServiceActivityFactory;
import com.servicenow.demo.core.route.activity.ShipmentActivityFactory;
import com.servicenow.demo.core.vehicle.Vehicle;
import com.servicenow.demo.core.vehicle.VehicleType;
import com.servicenow.demo.core.vehicle.VehicleTypeKey;

public class VehicleRoutingProblem {

    public static class Builder {


        /**
         * Returns a new instance of this builder.
         *
         * @return builder
         */
        public static Builder newInstance() {
            return new Builder();
        }

        private EuclideanCosts transportCosts;

        private VehicleRoutingActivityCosts activityCosts = new WaitingTimeCosts();

        private Map<String, Job> jobs = new LinkedHashMap<String, Job>();

        private Map<String, Job> tentativeJobs = new LinkedHashMap<String, Job>();

        private Set<String> jobsInInitialRoutes = new HashSet<String>();

        private Map<String, Coordinate> tentative_coordinates = new HashMap<String, Coordinate>();

        private FleetSize fleetSize = FleetSize.INFINITE;

        private Collection<VehicleType> vehicleTypes = new ArrayList<VehicleType>();

        private Collection<VehicleRoute> initialRoutes = new ArrayList<VehicleRoute>();

        private Set<Vehicle> uniqueVehicles = new LinkedHashSet<Vehicle>();

        private Set<String> addedVehicleIds = new LinkedHashSet<String>();

        private boolean hasBreaks = false;

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
        private int jobIndexCounter = 1;

        private int vehicleIndexCounter = 1;

        private int activityIndexCounter = 1;

        private int vehicleTypeIdIndexCounter = 1;

        private Map<VehicleTypeKey, Integer> typeKeyIndices = new HashMap<VehicleTypeKey, Integer>();

        private Map<Job, List<Activity>> activityMap = new HashMap<Job, List<Activity>>();

        private final ShipmentActivityFactory shipmentActivityFactory = new ShipmentActivityFactory();

        private final ServiceActivityFactory serviceActivityFactory = new ServiceActivityFactory();
        
//        int loc_size = tentative_coordinates.size();
        private void incJobIndexCounter() {
            jobIndexCounter++;
        }

        private void incActivityIndexCounter() {
            activityIndexCounter++;
        }

        private void incVehicleTypeIdIndexCounter() {
            vehicleTypeIdIndexCounter++;
        }

        private HashSet<Location> allLocations = new HashSet<Location>();

        public Map<String, Coordinate> getLocationMap() {
            return Collections.unmodifiableMap(tentative_coordinates);
        }


        public Builder setRoutingCost(EuclideanCosts costs) {
            this.transportCosts = costs;
            return this;
        }


        public Builder setJobActivityFactory(JobActivityFactory jobActivityFactory) {
            this.jobActivityFactory = jobActivityFactory;
            return this;
        }

        public Builder setFleetSize(FleetSize fleetSize) {
            this.fleetSize = fleetSize;
            return this;
        }

        public Builder addJob(Job job) {
            
            if (tentativeJobs.containsKey(job.getId()))
                throw new IllegalArgumentException("The vehicle routing problem already contains a service or shipment with id " + job.getId() + ". Please make sure you use unique ids for all services and shipments.");
            if (!(job instanceof Service || job instanceof Shipment))
                throw new IllegalArgumentException("Job must be either a service or a shipment.");
            job.setIndex(jobIndexCounter);
            incJobIndexCounter();
            tentativeJobs.put(job.getId(), job);
            addLocationToTentativeLocations(job);
            return this;
        }

        private void addLocationToTentativeLocations(Job job) {
            if (job instanceof Service) {
                Location location = ((Service) job).getLocation();
                addLocationToTentativeLocations(location);
            } else if (job instanceof Shipment) {
                Shipment shipment = (Shipment) job;
                Location pickupLocation = shipment.getPickupLocation();
                addLocationToTentativeLocations(pickupLocation);
                Location deliveryLocation = shipment.getDeliveryLocation();
                addLocationToTentativeLocations(deliveryLocation);
            }
        }

        private void addLocationToTentativeLocations(Location location) {
            tentative_coordinates.put(location.getId(), location.getCoordinate());
            allLocations.add(location);
        }

        private void addJobToFinalJobMapAndCreateActivities(Job job) {
            if (job instanceof Service) {
                Service service = (Service) job;
                addService(service);
            } else if (job instanceof Shipment) {
                Shipment shipment = (Shipment) job;
                addShipment(shipment);
            }
            List<Activity> jobActs = jobActivityFactory.createActivities(job);
            for (Activity act : jobActs) {
                act.setIndex(activityIndexCounter);
                incActivityIndexCounter();
            }
            activityMap.put(job, jobActs);
        }

        public Builder addInitialVehicleRoute(VehicleRoute route) {
            if(!addedVehicleIds.contains(route.getVehicle().getId())){
                addVehicle((Vehicle) route.getVehicle());
                addedVehicleIds.add(route.getVehicle().getId());
            }
            for (Activity act : route.getActivities()) {
                Activity abstractAct = (Activity) act;
                abstractAct.setIndex(activityIndexCounter);
                incActivityIndexCounter();
                if (act instanceof Activity.JobActivity) {
                    Job job = ((Activity.JobActivity) act).getJob();
                    jobsInInitialRoutes.add(job.getId());
                    addLocationToTentativeLocations(job);
                    registerJobAndActivity(abstractAct, job);
                }
            }
            initialRoutes.add(route);
            return this;
        }



        private void registerJobAndActivity(Activity act, Job job) {
            if (activityMap.containsKey(job)) activityMap.get(job).add(act);
            else {
                List<Activity> actList = new ArrayList<Activity>();
                actList.add(act);
                activityMap.put(job, actList);
            }
        }

        public Builder addInitialVehicleRoutes(Collection<VehicleRoute> routes) {
            for (VehicleRoute r : routes) {
                addInitialVehicleRoute(r);
            }
            return this;
        }

        private void addShipment(Shipment job) {
            if (jobs.containsKey(job.getId())) {
                logger.warn("The job " + job + " has already been added to the job list. This overrides the existing job.");
            }
            addLocationToTentativeLocations(job);
            jobs.put(job.getId(), job);
        }

        public Builder addVehicle(Vehicle vehicle) {
           
            if(addedVehicleIds.contains(vehicle.getId())){
                throw new IllegalArgumentException("The vehicle routing problem already contains a vehicle with id " + vehicle.getId() + ". Please choose unique ids for each vehicle.");
            }
            else addedVehicleIds.add(vehicle.getId());
            if (!uniqueVehicles.contains(vehicle)) {
                vehicle.setIndex(vehicleIndexCounter);
                incVehicleIndexCounter();
            }
            if (typeKeyIndices.containsKey(vehicle.getVehicleTypeIdentifier())) {
                vehicle.getVehicleTypeIdentifier().setIndex(typeKeyIndices.get(vehicle.getVehicleTypeIdentifier()));
            } else {
                vehicle.getVehicleTypeIdentifier().setIndex(vehicleTypeIdIndexCounter);
                typeKeyIndices.put(vehicle.getVehicleTypeIdentifier(), vehicleTypeIdIndexCounter);
                incVehicleTypeIdIndexCounter();
            }
            uniqueVehicles.add(vehicle);
            if (!vehicleTypes.contains(vehicle.getType())) {
                vehicleTypes.add(vehicle.getType());
            }
            String startLocationId = vehicle.getStartLocation().getId();
            addLocationToTentativeLocations(vehicle.getStartLocation());
            if (!vehicle.getEndLocation().getId().equals(startLocationId)) {
                addLocationToTentativeLocations(vehicle.getEndLocation());
            }
            return this;
        }

        private void incVehicleIndexCounter() {
            vehicleIndexCounter++;
        }

        public Builder setActivityCosts(VehicleRoutingActivityCosts activityCosts) {
            this.activityCosts = activityCosts;
            return this;
        }

        public VehicleRoutingProblem build() {
            if (transportCosts == null) {
                transportCosts = new EuclideanCosts();
            }
            for (Job job : tentativeJobs.values()) {
                if (!jobsInInitialRoutes.contains(job.getId())) {
                    addJobToFinalJobMapAndCreateActivities(job);
                }
            }
            return new VehicleRoutingProblem(this);
        }

        public Builder addLocation(String locationId, Coordinate coordinate) {
            tentative_coordinates.put(locationId, coordinate);
            return this;
        }

        public Builder addAllJobs(Collection<? extends Job> jobs) {
            for (Job j : jobs) {
                addJob(j);
            }
            return this;
        }


        public Builder addAllVehicles(Collection<? extends Vehicle> vehicles) {
            for (Vehicle v : vehicles) {
                addVehicle(v);
            }
            return this;
        }

        public Collection<Vehicle> getAddedVehicles() {
            return Collections.unmodifiableCollection(uniqueVehicles);
        }

        public Collection<VehicleType> getAddedVehicleTypes() {
            return Collections.unmodifiableCollection(vehicleTypes);
        }

        public Collection<Job> getAddedJobs() {
            return Collections.unmodifiableCollection(tentativeJobs.values());
        }

        private Builder addService(Service service) {
            addLocationToTentativeLocations(service);
            if (jobs.containsKey(service.getId())) {
                logger.warn("The service " + service + " has already been added to job list. This overrides existing job.");
            }
            jobs.put(service.getId(), service);
            return this;
        }


    }

    public static enum FleetSize {
        FINITE, INFINITE
    }

    private final static Logger logger = LoggerFactory.getLogger(VehicleRoutingProblem.class);

    private final VehicleRoutingTransportCosts transportCosts;

    private final VehicleRoutingActivityCosts activityCosts;

    private final Map<String, Job> jobs;

    private final Map<String, Job> allJobs;

    private final Collection<Vehicle> vehicles;

    private final Collection<VehicleType> vehicleTypes;


    private final Collection<VehicleRoute> initialVehicleRoutes;

    private final HashSet<Location> allLocations;

    private final FleetSize fleetSize;

    private Map<Job, List<Activity>> activityMap;

    private int nuActivities;
    
    private double[][] distanceMatrix = null;


    private final JobActivityFactory jobActivityFactory = new JobActivityFactory() {

        @Override
        public List<Activity> createActivities(Job job) {
            return copyAndGetActivities(job);
        }

    };

    private VehicleRoutingProblem(Builder builder) {
        this.jobs = builder.jobs;
        this.fleetSize = builder.fleetSize;
        this.vehicles = builder.uniqueVehicles;
        this.vehicleTypes = builder.vehicleTypes;
        this.initialVehicleRoutes = builder.initialRoutes;
        this.transportCosts = builder.transportCosts;
        this.activityCosts = builder.activityCosts;
        this.activityMap = builder.activityMap;
        this.nuActivities = builder.activityIndexCounter;
        this.allLocations = builder.allLocations;
        this.allJobs = builder.tentativeJobs;
        logger.info("setup problem: {}", this);
    }


    @Override
    public String toString() {
        return "[fleetSize=" + fleetSize + "][#jobs=" + jobs.size() + "][#vehicles=" + vehicles.size() + "][#vehicleTypes=" + vehicleTypes.size() + "][" +
            "transportCost=" + transportCosts + "][activityCosts=" + activityCosts + "]";
    }

    public FleetSize getFleetSize() {
        return fleetSize;
    }

    public Map<String, Job> getJobs() {
        return Collections.unmodifiableMap(jobs);
    }

    public Map<String, Job> getJobsInclusiveInitialJobsInRoutes(){
        return Collections.unmodifiableMap(allJobs);
    }

    public Collection<VehicleRoute> getInitialVehicleRoutes() {
        Collection<VehicleRoute> copiedInitialRoutes = new ArrayList<VehicleRoute>();
        for (VehicleRoute route : initialVehicleRoutes) {
            copiedInitialRoutes.add(VehicleRoute.copyOf(route));
        }
        return copiedInitialRoutes;
    }

    public Collection<VehicleType> getTypes() {
        return Collections.unmodifiableCollection(vehicleTypes);
    }


    public Collection<Vehicle> getVehicles() {
        return Collections.unmodifiableCollection(vehicles);
    }

    public VehicleRoutingTransportCosts getTransportCosts() {
        return transportCosts;
    }

    public VehicleRoutingActivityCosts getActivityCosts() {
        return activityCosts;
    }

    public Collection<Location> getAllLocations(){
        return allLocations;
    }

    public List<Activity> getActivities(Job job) {
        return Collections.unmodifiableList(activityMap.get(job));
    }


    public int getNuActivities() {
        return nuActivities;
    }

    public JobActivityFactory getJobActivityFactory() {
        return jobActivityFactory;
    }

    public List<Activity> copyAndGetActivities(Job job) {
        List<Activity> acts = new ArrayList<Activity>();
        if (activityMap.containsKey(job)) {
            for (Activity act : activityMap.get(job)) acts.add((Activity) act.duplicate());
        }
        return acts;
    }
    
    public double[][] getDistanceMatrix(){
    	return distanceMatrix;
    	
    }
    
    public void buildDistanceMatrix() {
    	int loc_size = allLocations.size();
    	distanceMatrix = new double[loc_size][loc_size];
    	double eucDist;
    	Location[] locArr = (Location[]) allLocations.toArray();
        for (int i = 0; i <= loc_size; i++) {
            for (int j = i + 1; j <= loc_size; j++) //The table is summetric to the first diagonal
            {                                      //Use this to compute distances in O(n/2)

            	eucDist = EuclideanDistanceCalculator.calculateDistance(locArr[i].getCoordinate(), locArr[j].getCoordinate());//(Nodes[i].Node_X - Nodes[j].Node_X);


                distanceMatrix[i][j] = eucDist;
                distanceMatrix[j][i] = eucDist;
            }
        }
    }
}
