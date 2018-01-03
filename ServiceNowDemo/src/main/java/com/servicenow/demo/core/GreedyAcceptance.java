package com.servicenow.demo.core;


import java.util.Collection;


/**
 * Acceptor that accepts solutions to be memorized only better solutions.
 * <p>
 * <p>If there is enough memory, every solution will be accepted. If there is no memory anymore and the solution
 * to be evaluated is better than the worst, the worst will be replaced by the new solution.</p>
 */
public class GreedyAcceptance implements SolutionAcceptor {

    private final int solutionMemory;

    public GreedyAcceptance(int solutionMemory) {
        this.solutionMemory = solutionMemory;
    }

    @Override
    public boolean acceptSolution(Collection<VehicleRoutingProblemSolution> solutions, VehicleRoutingProblemSolution newSolution) {
        boolean solutionAccepted = false;
        if (solutions.size() < solutionMemory) {
            solutions.add(newSolution);
            solutionAccepted = true;
        } else {
            VehicleRoutingProblemSolution worstSolution = null;
            for (VehicleRoutingProblemSolution s : solutions) {
                if (worstSolution == null) worstSolution = s;
                else if (s.getCost() > worstSolution.getCost()) worstSolution = s;
            }
            if (newSolution.getCost() < worstSolution.getCost()) {
                solutions.remove(worstSolution);
                solutions.add(newSolution);
                solutionAccepted = true;
            }
        }
        return solutionAccepted;
    }

    @Override
    public String toString() {
        return "[name=GreedyAcceptance]";
    }


}
