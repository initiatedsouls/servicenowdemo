package com.servicenow.demo.core.job;


public interface Job {


    public String getId();

    public int getSize();

    public String getName();

    public int getPriority();
    
    public int getIndex();
    
    public void setIndex(int s);

}