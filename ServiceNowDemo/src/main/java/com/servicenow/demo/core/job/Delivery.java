package com.servicenow.demo.core.job;


public class Delivery extends Service {

    public static class Builder extends Service.Builder<Delivery> {

        public static Builder newInstance(String id) {
            return new Builder(id);
        }

        Builder(String id) {
            super(id);
        }

        public Delivery build() {
            if (location == null) throw new IllegalArgumentException("location is missing");
            this.setType("delivery");
            return new Delivery(this);
        }

    }

    Delivery(Builder builder) {
        super(builder);

    }

}
