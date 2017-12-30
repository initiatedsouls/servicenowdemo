package com.servicenow.demo.core.job;

public class Pickup extends Service {

    public static class Builder extends Service.Builder<Pickup> {

        public static Builder newInstance(String id) {
            return new Builder(id);
        }

        Builder(String id) {
            super(id);
        }
        public Pickup build() {
            if (location == null) throw new IllegalArgumentException("location is missing");
            this.setType("pickup");
            return new Pickup(this);
        }

    }

    Pickup(Builder builder) {
        super(builder);
    }

}
