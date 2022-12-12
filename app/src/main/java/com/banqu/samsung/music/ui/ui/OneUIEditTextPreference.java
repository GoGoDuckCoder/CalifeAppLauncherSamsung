package com.banqu.samsung.music.ui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.banqu.samsung.music.R;
import com.banqu.samsung.music.carlifeapplauncher.adapter.Common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceViewHolder;

public class OneUIEditTextPreference extends EditTextPreference {
    public OneUIEditTextPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public OneUIEditTextPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OneUIEditTextPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OneUIEditTextPreference(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
//        android.R.id.title
        ImageView imageView = (ImageView) holder.findViewById(android.R.id.icon);
        if (imageView.getResources() != null) {
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            lp.height = getContext().getResources().getDimensionPixelSize(R.dimen.oneui_icon_size);
            lp.width = getContext().getResources().getDimensionPixelSize(R.dimen.oneui_icon_size);
            imageView.setLayoutParams(lp);
            imageView.setBackgroundColor(Common.getRandomColor());
            int padding = getContext().getResources().getDimensionPixelSize(R.dimen.oneui_icon_padding);
            imageView.setPadding(padding, padding, padding, padding);
            Common.setBgRadius(imageView, getContext().getResources().getDimensionPixelSize(R.dimen.oneui_icon_radius));
        }


        if (getParent() != null && getParent().getPreferenceCount() == 1) {
            Common.setBgRadius(holder.itemView, getContext().getResources().getDimensionPixelSize(R.dimen.oneui_radius));
        } else {
            if (getOrder() == 0) {
                Common.setBgRadiusTops(holder.itemView, getContext().getResources().getDimensionPixelSize(R.dimen.oneui_radius));
            } else {
                if (getParent() != null && getOrder() == getParent().getPreferenceCount() - 1) {
                    {
                        Common.setBgRadiusBottoms(holder.itemView, getContext().getResources().getDimensionPixelSize(R.dimen.oneui_radius));
                    }
                }
            }
        }

        holder.itemView.setBackgroundColor(getContext().getColor(R.color.OneUI_Button_BackgroundColor));

    }
}
