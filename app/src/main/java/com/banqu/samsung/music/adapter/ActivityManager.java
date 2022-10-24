package com.banqu.samsung.music.adapter;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityManager {
    public static ActivityManager activityManager;
    public static ArrayList<Activity> activities;

    private ActivityManager() {
        activities = new ArrayList<Activity>();
    }

    public static ActivityManager getInstance() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    public void add(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public void remove(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public void clear() {
        for (Activity activity : activities) {
            if (activity.getDisplay().getDisplayId() == 0) {
                activity.finish();
            }
        }
    }

    public void clearAll() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

}
