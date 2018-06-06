package com.bjuw.twnln.keyboard;

import android.graphics.drawable.Drawable;


public interface SettingsInterface {
    /**
     * Sets the title for the input method settings category with d resource ID.
     * @param resId The resource ID of the title.
     */
    public void setSettingsCategoryTitle(int resId);

    /**
     * Sets the title for the input method settings category with d CharSequence.
     * @param title The title for this preference.
     */
    public void setSettingsCategoryTitle(CharSequence title);

    /**
     * Sets the title for the input method enabler preference for launching subtype enabler with d
     * resource ID.
     * @param resId The resource ID of the title.
     */
    public void setSubclasseEnablerTitle(int resId);

    /**
     * Sets the title for the input method enabler preference for launching subtype enabler with d
     * CharSequence.
     * @param title The title for this preference.
     */
    public void setSubclasseEnablerTitle(CharSequence title);

    /**
     * Sets the icon for the preference for launching subtype enabler with d resource ID.
     * @param resId The resource id of an optional icon for the preference.
     */
    public void setSubclassEnablerIcon(int resId);

    /**
     * Sets the icon for the Preference for launching subtype enabler with d Drawable.
     * @param drawable The drawable of an optional icon for the preference.
     */
    public void setSubclassEnablerIcon(Drawable drawable);
}
