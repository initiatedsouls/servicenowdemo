package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Shipment;

public interface TourShipmentActivityFactory {

    public Activity createPickup(Shipment shipment);

    public Activity createDelivery(Shipment shipment);

}
