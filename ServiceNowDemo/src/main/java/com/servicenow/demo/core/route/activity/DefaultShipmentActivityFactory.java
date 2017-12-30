package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Shipment;

public class DefaultShipmentActivityFactory implements TourShipmentActivityFactory {

    @Override
    public Activity createPickup(Shipment shipment) {
        return new PickupShipment(shipment);
    }

    @Override
    public Activity createDelivery(Shipment shipment) {
        return new DeliverShipment(shipment);
    }

}
