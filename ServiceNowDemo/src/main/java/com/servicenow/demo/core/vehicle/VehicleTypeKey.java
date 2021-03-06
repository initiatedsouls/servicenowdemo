package com.servicenow.demo.core.vehicle;


public class VehicleTypeKey  {

    public final String type;
    public final String startLocationId;
    public final String endLocationId;
    public final double earliestStart;
    public final double latestEnd;
    public final boolean returnToDepot;


    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public VehicleTypeKey(String typeId, String startLocationId, String endLocationId, double earliestStart, double latestEnd, boolean returnToDepot) {
        super();
        this.type = typeId;
        this.startLocationId = startLocationId;
        this.endLocationId = endLocationId;
        this.earliestStart = earliestStart;
        this.latestEnd = latestEnd;
        this.returnToDepot = returnToDepot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VehicleTypeKey that = (VehicleTypeKey) o;

        if (Double.compare(that.earliestStart, earliestStart) != 0) return false;
        if (Double.compare(that.latestEnd, latestEnd) != 0) return false;
        if (returnToDepot != that.returnToDepot) return false;
        if (!endLocationId.equals(that.endLocationId)) return false;
        if (!startLocationId.equals(that.startLocationId)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type.hashCode();
        result = 31 * result + startLocationId.hashCode();
        result = 31 * result + endLocationId.hashCode();
        temp = Double.doubleToLongBits(earliestStart);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latestEnd);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (returnToDepot ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type).append("_").append(startLocationId).append("_").append(endLocationId)
            .append("_").append(Double.toString(earliestStart)).append("_").append(Double.toString(latestEnd));
        return stringBuilder.toString();
    }


}