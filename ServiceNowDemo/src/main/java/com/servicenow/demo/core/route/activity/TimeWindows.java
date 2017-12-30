package com.servicenow.demo.core.route.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TimeWindows {

    private Collection<TimeWindow> timeWindows = new ArrayList<TimeWindow>();

    public void add(TimeWindow timeWindow){
        for(TimeWindow tw : timeWindows){
            if(timeWindow.getStart() > tw.getStart() && timeWindow.getStart() < tw.getEnd()){
                throw new IllegalArgumentException("time-windows cannot overlap each other. overlap: " + tw + ", " + timeWindow);
            }
            if(timeWindow.getEnd() > tw.getStart() && timeWindow.getEnd() < tw.getEnd()){
                throw new IllegalArgumentException("time-windows cannot overlap each other. overlap: " + tw + ", " + timeWindow);
            }
            if(timeWindow.getStart() <= tw.getStart() && timeWindow.getEnd() >= tw.getEnd()){
                throw new IllegalArgumentException("time-windows cannot overlap each other. overlap: " + tw + ", " + timeWindow);
            }
        }
        timeWindows.add(timeWindow);
    }

    public Collection<TimeWindow> getTimeWindows() {
        return Collections.unmodifiableCollection(timeWindows);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(timeWindows.size() * 60);
        for (TimeWindow tw : timeWindows) {
            sb.append("[timeWindow=").append(tw).append("]");
        }
        return sb.toString();
    }
}
