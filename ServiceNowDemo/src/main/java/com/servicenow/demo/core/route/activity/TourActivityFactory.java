package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Service;

public interface TourActivityFactory {

    public Activity createActivity(Service service);

}
