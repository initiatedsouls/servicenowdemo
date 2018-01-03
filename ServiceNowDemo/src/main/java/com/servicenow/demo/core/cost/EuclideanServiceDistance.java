package com.servicenow.demo.core.cost;

import com.servicenow.demo.core.job.Job;
import com.servicenow.demo.core.job.Service;

public class EuclideanServiceDistance {

    public EuclideanServiceDistance() {
        super();
    }

    public double getDistance(Job i, Job j) {
        double avgCost = 0.0;
        if (i instanceof Service && j instanceof Service) {
            if (i.equals(j)) {
                avgCost = 0.0;
            } else {
                Service s_i = (Service) i;
                Service s_j = (Service) j;
                if (s_i.getLocation().getCoordinate() == null || s_j.getLocation().getCoordinate() == null)
                    throw new IllegalStateException("cannot calculate euclidean distance. since service coords are missing");
                avgCost = EuclideanDistanceCalculator.calculateDistance(s_i.getLocation().getCoordinate(), s_j.getLocation().getCoordinate());
            }
        } else {
            throw new UnsupportedOperationException(
                "currently, this class just works with shipments and services.");
        }
        return avgCost;
    }

}