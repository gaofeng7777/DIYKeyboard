package com.bjuw.twnln.keyboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import java.util.List;

class SettingsImpl implements SettingsInterface {
    private Preference mSubtypeEnablerPreference;
    private int mInputMethodSettingsCategoryTitleRes;
    private CharSequence mInputMethodSettingsCategoryTitle;
    private int mSubtypeEnablerTitleRes;
    private CharSequence mSubtypeEnablerTitle;
    private int mSubtypeEnablerIconRes;
    private Drawable mSubtypeEnablerIcon;
    private InputMethodManager mImm;
    private InputMethodInfo mImi;
    private Context mContext;

    public boolean initImpl(final Context context, final PreferenceScreen prefScreen) {
        mContext = context;
        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImi = getUserImi(context, mImm);
        if (mImi == null || mImi.getSubtypeCount() <= 1) {
            return false;
        }
        mSubtypeEnablerPreference = new Preference(context);
        mSubtypeEnablerPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        final CharSequence title = getSubclassEnablerTitle(context);
                        final Intent intent =
                                new Intent(Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS);
                        intent.putExtra(Settings.EXTRA_INPUT_METHOD_ID, mImi.getId());
                        if (!TextUtils.isEmpty(title)) {
                            intent.putExtra(Intent.EXTRA_TITLE, title);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        return true;
                    }
                });
        prefScreen.addPreference(mSubtypeEnablerPreference);
        updateSubclassEnabler();
        return true;
    }

    private static InputMethodInfo getUserImi(Context context, InputMethodManager imm) {
        final List<InputMethodInfo> imis = imm.getInputMethodList();
        for (int i = 0; i < imis.size(); ++i) {
            final InputMethodInfo imi = imis.get(i);
            if (imis.get(i).getPackageName().equals(context.getPackageName())) {
                return imi;
            }
        }
        return null;
    }

    private static String getUseSubtypesLabel(Context context, InputMethodManager imm, InputMethodInfo imi) {
        if (context == null || imm == null || imi == null) return null;
        final List<InputMethodSubtype> subtypes = imm.getEnabledInputMethodSubtypeList(imi, true);
        final StringBuilder sb = new StringBuilder();
        final int N = subtypes.size();
        for (int i = 0; i < N; ++i) {
            final InputMethodSubtype subtype = subtypes.get(i);
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(subtype.getDisplayName(context, imi.getPackageName(),
                    imi.getServiceInfo().applicationInfo));
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSettingsCategoryTitle(int resId) {
        mInputMethodSettingsCategoryTitleRes = resId;
        updateSubclassEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSettingsCategoryTitle(CharSequence title) {
        mInputMethodSettingsCategoryTitleRes = 0;
        mInputMethodSettingsCategoryTitle = title;
        updateSubclassEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubclasseEnablerTitle(int resId) {
        mSubtypeEnablerTitleRes = resId;
        updateSubclassEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubclasseEnablerTitle(CharSequence title) {
        mSubtypeEnablerTitleRes = 0;
        mSubtypeEnablerTitle = title;
        updateSubclassEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubclassEnablerIcon(int resId) {
        mSubtypeEnablerIconRes = resId;
        updateSubclassEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubclassEnablerIcon(Drawable drawable) {
        mSubtypeEnablerIconRes = 0;
        mSubtypeEnablerIcon = drawable;
        updateSubclassEnabler();
    }

    private CharSequence getSubclassEnablerTitle(Context context) {
        if (mSubtypeEnablerTitleRes != 0) {
            return context.getString(mSubtypeEnablerTitleRes);
        } else {
            return mSubtypeEnablerTitle;
        }
    }

    public void updateSubclassEnabler() {
        if (mSubtypeEnablerPreference != null) {
            if (mSubtypeEnablerTitleRes != 0) {
                mSubtypeEnablerPreference.setTitle(mSubtypeEnablerTitleRes);
            } else if (!TextUtils.isEmpty(mSubtypeEnablerTitle)) {
                mSubtypeEnablerPreference.setTitle(mSubtypeEnablerTitle);
            }
            final String summary = getUseSubtypesLabel(mContext, mImm, mImi);
            if (!TextUtils.isEmpty(summary)) {
                mSubtypeEnablerPreference.setSummary(summary);
            }
            if (mSubtypeEnablerIconRes != 0) {
                mSubtypeEnablerPreference.setIcon(mSubtypeEnablerIconRes);
            } else if (mSubtypeEnablerIcon != null) {
                mSubtypeEnablerPreference.setIcon(mSubtypeEnablerIcon);
            }
        }
    }
}
