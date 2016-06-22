package org.bob.android;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class MyPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    /**
     * Checks that a preference is a valid numerical value
     */

    Preference.OnPreferenceChangeListener numberCheckListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // check that the string is an integer
            if (newValue != null && newValue.toString().length() > 0
                    && newValue.toString().matches("\\d*")) {
                return true;
            }

            return false;
        }
    };

}