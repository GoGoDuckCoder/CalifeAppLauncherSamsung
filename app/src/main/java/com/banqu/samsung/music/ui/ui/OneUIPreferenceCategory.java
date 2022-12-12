package com.banqu.samsung.music.ui.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceCategory;

public class OneUIPreferenceCategory extends PreferenceCategory {
    public OneUIPreferenceCategory(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public OneUIPreferenceCategory(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OneUIPreferenceCategory(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OneUIPreferenceCategory(@NonNull Context context) {
        super(context);
    }
}
