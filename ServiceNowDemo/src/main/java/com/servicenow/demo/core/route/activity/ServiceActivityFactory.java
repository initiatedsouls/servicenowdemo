package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Delivery;
import com.servicenow.demo.core.job.Pickup;
import com.servicenow.demo.core.job.Service;

public class ServiceActivityFactory {

    public Activity createActivity(Service service) {
        Activity act;
        if (service instanceof Pickup) {
            act = new PickupService((Pickup) service);
        } else if (service instanceof Delivery) {
            act = new DeliverService((Delivery) service);
        } else {
            act = new PickupService(service);
        }
        return act;
    }

}
