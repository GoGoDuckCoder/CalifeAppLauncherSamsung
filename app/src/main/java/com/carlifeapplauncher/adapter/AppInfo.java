package com.carlifeapplauncher.adapter;

import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Comparator;

public class AppInfo implements Comparator<AppInfo>, Serializable {
    public CharSequence label;
    public CharSequence packageName;
    public Drawable icon;
    public ResolveInfo resolveInfo;

    @Override
    public int compare(AppInfo o1, AppInfo o2) {
        String name1 = o1.label.toString();
        String name2 = o2.label.toString();
        if (name1.compareTo(name2) > 0) {
            return 0;
        } else {
            return -1;
        }
    }
}
