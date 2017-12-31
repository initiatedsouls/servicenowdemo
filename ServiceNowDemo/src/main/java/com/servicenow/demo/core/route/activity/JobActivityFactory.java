package com.servicenow.demo.core.route.activity;

import java.util.List;

import com.servicenow.demo.core.job.Job;

public interface JobActivityFactory {
	public List<Activity> createActivities(Job job);

}
