package com.bjuw.twnln.keyboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public abstract class SettingsFragment extends PreferenceFragment implements SettingsInterface {
    private final SettingsImpl mSettings = new SettingsImpl();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getActivity();
        setPreferenceScreen(getPreferenceManager().createPreferenceScreen(context));
        mSettings.initImpl(context, getPreferenceScreen());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSettingsCategoryTitle(int resId) {
        mSettings.setSettingsCategoryTitle(resId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSettingsCategoryTitle(CharSequence title) {
        mSettings.setSettingsCategoryTitle(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubclasseEnablerTitle(int resId) {
        mSettings.setSubclasseEnablerTitle(resId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubclasseEnablerTitle(CharSequence title) {
        mSettings.setSubclasseEnablerTitle(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubclassEnablerIcon(int resId) {
        mSettings.setSubclassEnablerIcon(resId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubclassEnablerIcon(Drawable drawable) {
        mSettings.setSubclassEnablerIcon(drawable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        mSettings.updateSubclassEnabler();
    }
}
