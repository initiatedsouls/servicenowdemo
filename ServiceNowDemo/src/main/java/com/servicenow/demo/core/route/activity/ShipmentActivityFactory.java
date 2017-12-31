package com.servicenow.demo.core.route.activity;

import com.servicenow.demo.core.job.Shipment;

public class ShipmentActivityFactory {

    public Activity createPickup(Shipment shipment) {
        return new PickupShipment(shipment);
    }

    public Activity createDelivery(Shipment shipment) {
        return new DeliverShipment(shipment);
    }

}
