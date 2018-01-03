/*
 * Licensed to GraphHopper GmbH under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * GraphHopper GmbH licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.servicenow.demo.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.servicenow.demo.core.job.Job;
import com.servicenow.demo.core.route.VehicleRoute;
import com.servicenow.demo.core.route.activity.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class VehicleRoutingAlgorithm {


    private final static Logger logger = LoggerFactory.getLogger(VehicleRoutingAlgorithm.class);

    private final int counter = 0;

    private final VehicleRoutingProblem problem;

//    private final SearchStrategyManager searchStrategyManager;

//    private final VehicleRoutingAlgorithmListeners algoListeners = new VehicleRoutingAlgorithmListeners();

    private final Collection<VehicleRoutingProblemSolution> initialSolutions;

    private int maxIterations = 100;

//    private TerminationManager terminationManager = new TerminationManager();

    private VehicleRoutingProblemSolution bestEver = null;


    public VehicleRoutingAlgorithm(VehicleRoutingProblem problem) {
        super();
        this.problem = problem;
//        this.searchStrategyManager = searchStrategyManager;
        initialSolutions = new ArrayList<VehicleRoutingProblemSolution>();
//        objectiveFunction = null;
    }

    public VehicleRoutingAlgorithm(VehicleRoutingProblem problem, Collection<VehicleRoutingProblemSolution> initialSolutions) {
        super();
        this.problem = problem;
//        this.searchStrategyManager = searchStrategyManager;
        this.initialSolutions = initialSolutions;
//        objectiveFunction = null;
    }


    public void addInitialSolution(VehicleRoutingProblemSolution solution) {
        // We will make changes so let's make a copy
        solution = VehicleRoutingProblemSolution.copyOf(solution);
//        verify(solution);
        initialSolutions.add(solution);
    }
    
    public VehicleRoutingProblemSolution buildSolution(){
    	
    	for(Job job : problem.getJobs().values()) {
    		
    	}
    }
    
/*
    private void verify(VehicleRoutingProblemSolution solution) {
        Set<Job> allJobs = new HashSet<Job>(problem.getJobs().values());
        allJobs.removeAll(solution.getUnassignedJobs());
        for (VehicleRoute route : solution.getRoutes()) {
            allJobs.removeAll(route.getRouteActivities().getJobs());
            if (route.getVehicle().getIndex() == 0)
                throw new IllegalStateException("vehicle used in initial solution has no index. probably a vehicle is used that has not been added to the " +
                    " the VehicleRoutingProblem. only use vehicles that have already been added to the problem.");
            for (Activity act : route.getActivities()) {
                if (act.getIndex() == 0)
                    throw new IllegalStateException("act in initial solution has no index. activities are created and associated to their job in VehicleRoutingProblem\n." +
                        " thus if you build vehicle-routes use the jobActivityFactory from vehicle routing problem like that \n" +
                        " VehicleRoute.Builder.newInstance(knownVehicle).setJobActivityFactory(vrp.getJobActivityFactory).addService(..)....build() \n" +
                        " then the activities that are created to build the route are identical to the ones used in VehicleRoutingProblem");
            }
        }

        solution.getUnassignedJobs().addAll(allJobs);
        solution.setCost(getObjectiveFunction().getCosts(solution));

    }*/
    
    

    /*public Collection<VehicleRoutingProblemSolution> searchSolutions() {
        logger.info("algorithm starts: [maxIterations={}]", maxIterations);
        double now = System.currentTimeMillis();
        int noIterationsThisAlgoIsRunning = maxIterations;
//        counter.reset();
        Collection<VehicleRoutingProblemSolution> solutions = new ArrayList<VehicleRoutingProblemSolution>(initialSolutions);
        bestEver = Solutions.bestOf(solutions);
        logger.info("iterations start");
        for (int i = 0; i < maxIterations; i++) {
           
            DiscoveredSolution discoveredSolution = strategy.run(problem, solutions);

            memorizeIfBestEver(discoveredSolution);
            
            selectedStrategy(discoveredSolution, problem, solutions);
        }
        logger.info("iterations end at {} iterations", noIterationsThisAlgoIsRunning);
        addBestEver(solutions);

        return solutions;
    }*/

    private void addBestEver(Collection<VehicleRoutingProblemSolution> solutions) {
        if (bestEver != null) {
            solutions.add(bestEver);
        }
    }

  /*  private void log(Collection<VehicleRoutingProblemSolution> solutions) {
        for (VehicleRoutingProblemSolution sol : solutions) {
            log(sol);
        }
    }

    private void log(VehicleRoutingProblemSolution solution) {
        logger.trace("solution costs: {}", solution.getCost());
        for (VehicleRoute r : solution.getRoutes()) {
            StringBuilder b = new StringBuilder();
            b.append(r.getVehicle().getId()).append(" : ").append("[ ");
            for (TourActivity act : r.getActivities()) {
                if (act instanceof TourActivity.JobActivity) {
                    b.append(((TourActivity.JobActivity) act).getJob().getId()).append(" ");
                }
            }
            b.append("]");
            logger.trace(b.toString());
        }
        StringBuilder b = new StringBuilder();
        b.append("unassigned : [ ");
        for (Job j : solution.getUnassignedJobs()) {
            b.append(j.getId()).append(" ");
        }
        b.append("]");
        logger.trace(b.toString());
    }

    private void log(DiscoveredSolution discoveredSolution) {
        logger.trace("discovered solution: {}", discoveredSolution);
        log(discoveredSolution.getSolution());
    }


    private void memorizeIfBestEver(DiscoveredSolution discoveredSolution) {
        if (discoveredSolution == null) return;
        if (bestEver == null) {
            bestEver = discoveredSolution.getSolution();
        } else if (discoveredSolution.getSolution().getCost() < bestEver.getCost()) {
            bestEver = discoveredSolution.getSolution();
        }
    }


    private void selectedStrategy(DiscoveredSolution discoveredSolution, VehicleRoutingProblem problem, Collection<VehicleRoutingProblemSolution> solutions) {
        algoListeners.selectedStrategy(discoveredSolution, problem, solutions);
    }

    private void algorithmEnds(VehicleRoutingProblem problem, Collection<VehicleRoutingProblemSolution> solutions) {
        algoListeners.algorithmEnds(problem, solutions);
    }

    public VehicleRoutingAlgorithmListeners getAlgorithmListeners() {
        return algoListeners;
    }

    public void addListener(VehicleRoutingAlgorithmListener l) {
        algoListeners.addListener(l);
        if (l instanceof SearchStrategyListener) {
            searchStrategyManager.addSearchStrategyListener((SearchStrategyListener) l);
        }
        if (l instanceof SearchStrategyModuleListener) {
            searchStrategyManager.addSearchStrategyModuleListener((SearchStrategyModuleListener) l);
        }
    }

    private void iterationEnds(int i, VehicleRoutingProblem problem, Collection<VehicleRoutingProblemSolution> solutions) {
        algoListeners.iterationEnds(i, problem, solutions);
    }

    private void iterationStarts(int i, VehicleRoutingProblem problem, Collection<VehicleRoutingProblemSolution> solutions) {
        algoListeners.iterationStarts(i, problem, solutions);
    }

    private void algorithmStarts(VehicleRoutingProblem problem, Collection<VehicleRoutingProblemSolution> solutions) {
        algoListeners.algorithmStarts(problem, this, solutions);
    }

    
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        logger.debug("set maxIterations to {}", this.maxIterations);
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public SolutionCostCalculator getObjectiveFunction(){
        return objectiveFunction;
    }
*/
}
