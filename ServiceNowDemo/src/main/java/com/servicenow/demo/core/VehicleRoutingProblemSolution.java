package com.servicenow.demo.core;

import java.util.ArrayList;
import java.util.Collection;

import com.servicenow.demo.core.job.Job;
import com.servicenow.demo.core.route.VehicleRoute;


public class VehicleRoutingProblemSolution {

    /**
     * Makes a deep copy of the solution to be copied.
     *
     * @param solution2copy solution to be copied
     * @return solution
     */
    public static VehicleRoutingProblemSolution copyOf(VehicleRoutingProblemSolution solution2copy) {
        return new VehicleRoutingProblemSolution(solution2copy);
    }

    private final Collection<VehicleRoute> routes;

    private Collection<Job> unassignedJobs = new ArrayList<Job>();

    private double cost;

    private VehicleRoutingProblemSolution(VehicleRoutingProblemSolution solution) {
        routes = new ArrayList<VehicleRoute>();
        for (VehicleRoute r : solution.getRoutes()) {
            VehicleRoute route = VehicleRoute.copyOf(r);
            routes.add(route);
        }
        this.cost = solution.getCost();
        unassignedJobs.addAll(solution.getUnassignedJobs());
    }

    public VehicleRoutingProblemSolution(Collection<VehicleRoute> routes, double cost) {
        super();
        this.routes = routes;
        this.cost = cost;
    }

    public VehicleRoutingProblemSolution(Collection<VehicleRoute> routes, Collection<Job> unassignedJobs, double cost) {
        super();
        this.routes = routes;
        this.unassignedJobs = unassignedJobs;
        this.cost = cost;
    }

    public Collection<VehicleRoute> getRoutes() {
        return routes;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Collection<Job> getUnassignedJobs() {
        return unassignedJobs;
    }

    @Override
    public String toString() {
        return "[costs=" + cost + "][routes=" + routes.size() + "][unassigned=" + unassignedJobs.size() + "]";
    }
}

