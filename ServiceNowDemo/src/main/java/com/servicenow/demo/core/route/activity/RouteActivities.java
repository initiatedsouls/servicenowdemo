package com.servicenow.demo.core.route.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.servicenow.demo.core.route.activity.Activity.JobActivity;
import com.servicenow.demo.core.job.Job;

public class RouteActivities {

    public static RouteActivities copyOf(RouteActivities routeActivities) {
        return new RouteActivities(routeActivities);
    }


    private final ArrayList<Activity> routeActivities = new ArrayList<Activity>();

    private final Set<Job> jobs = new HashSet<Job>();


    private RouteActivities(RouteActivities tour2copy) {
        for (Activity tourAct : tour2copy.getActivities()) {
            Activity newAct = tourAct.duplicate();
            this.routeActivities.add(newAct);
            addJob(newAct);
        }
    }

    public RouteActivities() {

    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(routeActivities);
    }

    public Iterator<Activity> iterator() {
        return routeActivities.iterator();
    }

    public boolean isEmpty() {
        return (routeActivities.size() == 0);
    }

    public Collection<Job> getJobs() {
        return Collections.unmodifiableSet(jobs);
    }

    public boolean servesJob(Job job) {
        return jobs.contains(job);
    }

    @Override
    public String toString() {
        return "[nuOfActivities=" + routeActivities.size() + "]";
    }

    public boolean removeJob(Job job) {
        boolean jobRemoved = false;
        if (!jobs.contains(job)) {
            return false;
        } else {
            jobRemoved = jobs.remove(job);
        }
        boolean activityRemoved = false;
        Iterator<Activity> iterator = routeActivities.iterator();
        while (iterator.hasNext()) {
            Activity c = iterator.next();
            if (c instanceof JobActivity) {
                Job underlyingJob = ((JobActivity) c).getJob();
                if (job.equals(underlyingJob)) {
                    iterator.remove();
                    activityRemoved = true;
                }
            }
        }
        return activityRemoved;
    }



    public boolean removeActivity(Activity activity) {
        Job job = null;
        if (activity instanceof JobActivity) {
            job = ((JobActivity) activity).getJob();
        }
        boolean jobIsAlsoAssociateToOtherActs = false;
        boolean actRemoved = false;
        List<Activity> acts = new ArrayList<Activity>(routeActivities);
        for (Activity act : acts) {
            if (act == activity) {
                routeActivities.remove(act);
                actRemoved = true;
            } else {
                if (act instanceof JobActivity && job != null) {
                    if (((JobActivity) act).getJob().equals(job)) {
                        jobIsAlsoAssociateToOtherActs = true;
                    }
                }
            }
        }
        if (!jobIsAlsoAssociateToOtherActs && actRemoved) {
            jobs.remove(job);
        }
        return actRemoved;
    }


    public void addActivity(int insertionIndex, Activity act) {

        if (insertionIndex < routeActivities.size()) {
            routeActivities.add(insertionIndex, act);
        } else if (insertionIndex >= routeActivities.size()) {
            routeActivities.add(act);
        }
        addJob(act);
    }

    public void addActivity(Activity act) {
        if (routeActivities.contains(act))
            throw new IllegalArgumentException("act " + act + " already in tour. cannot add act twice.");
        routeActivities.add(act);
        addJob(act);
    }

    private void addJob(Activity act) {
        if (act instanceof JobActivity) {
            Job job = ((JobActivity) act).getJob();
            jobs.add(job);
        }
    }

    public int jobSize() {
        return jobs.size();
    }


}
