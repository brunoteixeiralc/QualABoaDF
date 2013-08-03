
package br.com.qualABoaDF.fragment;
 
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import br.com.qualABoaDF.R;

public class SettingPreferenceFragment extends PreferenceFragment {
  
	private CheckBoxPreference checkBoxPreference;
	private SharedPreferences sp;
	private Editor editor;
	
  @Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		   addPreferencesFromResource(R.xml.fragment_settings);
		   
			checkBoxPreference = (CheckBoxPreference) findPreference("notification");
			sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
			checkBoxPreference.setChecked(sp.getBoolean("NOTIFICATION", false));
			checkBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					editor = sp.edit();
					editor.putBoolean("NOTIFICATION", checkBoxPreference.isChecked());
					editor.commit();
					return false;
				}
			});
	}
	
}