package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Job;
import com.servicenow.demo.core.location.Location;

public interface Activity {

	public void setTheoreticalEarliestOperationStartTime(double earliest);

	public void setTheoreticalLatestOperationStartTime(double latest);

    public interface JobActivity extends Activity {

        public Job getJob();

    }

    public abstract String getName();

    public abstract Location getLocation();

    public abstract double getTheoreticalEarliestOperationStartTime();

    public abstract double getTheoreticalLatestOperationStartTime();

    public abstract double getOperationTime();

    public abstract double getArrTime();

    public abstract double getEndTime();

    public abstract void setArrTime(double arrTime);

    public abstract void setEndTime(double endTime);

    public abstract int getSize();

    public abstract Activity duplicate();

    public int getIndex();
    
    public void setIndex(int index);
}
