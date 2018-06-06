package com.bjuw.twnln.pre;

import android.content.Context;
import android.content.SharedPreferences;

public class AppLicationPreferences {

    private static AppLicationPreferences prefs;

    protected Context context;
    public static final String PREF_NAME = "keybord";

    private AppLicationPreferences(Context context) {
        this.context = context;
    }

    public static AppLicationPreferences getInstance(Context context) {

        if (prefs == null) {
            prefs = new AppLicationPreferences(context);
        }
        return prefs;
    }
    
    private static final String THEMES_ID = "themesid";
  
    /*Addition level */
    public void setKeyBoardThemeId(int addLevel) {
        SharedPreferences.Editor editor = getPreferencesEditor();
        editor.putInt(THEMES_ID,addLevel);
        editor.commit();
    }

    public int getKeyBoardThemesId() {
        return getPreferences().getInt(THEMES_ID, 0);
    }
 
    private static final String CUSTOME_THEMES_BACKGROUND = "customethemesbackground";

    public int getCustomeKeyBoardThemesBg() {
        return getPreferences().getInt(CUSTOME_THEMES_BACKGROUND, 0);
    }
    
	protected SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getPreferencesEditor() {
        return getPreferences().edit();
    }

}
