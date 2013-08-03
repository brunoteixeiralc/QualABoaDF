package br.com.qualABoaDF.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class SettingPreferenceActivity extends SherlockPreferenceActivity{
	
	private CheckBoxPreference checkBoxPreference;
	private SharedPreferences sp;
	private Editor editor;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	  public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			getSherlock().getActionBar().setHomeButtonEnabled(true);
			getSherlock().getActionBar().setDisplayHomeAsUpEnabled(true);
			getSherlock().getActionBar().setTitle("Configurações");
			
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				addPreferencesFromResource(br.com.qualABoaDF.R.xml.fragment_settings);
				checkBoxPreference = (CheckBoxPreference) findPreference("notification");
				sp = PreferenceManager.getDefaultSharedPreferences(this);
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
				
			}else{
				getFragmentManager().beginTransaction().replace(android.R.id.content,
			        new SettingPreferenceFragment()).commit();
			}

		}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}
	
}
