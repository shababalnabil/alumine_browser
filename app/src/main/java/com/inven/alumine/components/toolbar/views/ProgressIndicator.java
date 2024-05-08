package com.inven.alumine.components.toolbar.views;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.inven.alumine.R;

public class ProgressIndicator extends LinearProgressIndicator {
    public ProgressIndicator(@NonNull Context context) {
        super(context);
    }

    public ProgressIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        getRootView().findViewById(R.id.progress_divider).setVisibility(visibility);
    }
}
