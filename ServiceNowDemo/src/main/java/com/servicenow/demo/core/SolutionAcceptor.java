
package com.servicenow.demo.core;


import java.util.Collection;


public interface SolutionAcceptor {

    public boolean acceptSolution(Collection<VehicleRoutingProblemSolution> solutions, VehicleRoutingProblemSolution newSolution);

}
