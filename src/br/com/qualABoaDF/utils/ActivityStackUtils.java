package br.com.qualABoaDF.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.util.Log;


public class ActivityStackUtils {
	
	
	public static void printTasks(Context context, String logTag) {
		String packageName = context.getPackageName();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
		if(recentTasks != null && recentTasks.size() > 0) {
			int count = 0;
			for (RunningTaskInfo t : recentTasks) {
				String pack = t.baseActivity.getPackageName();
				if(pack.equals(packageName)) {
					String s = "Task ["+(count++)+"] : " + "base [" + t.baseActivity.getShortClassName() + "] , top ["+t.topActivity.getShortClassName()+"] activityStack.size > " + t.numActivities;
					Log.v(logTag, s);
				}
			}
			
		}
	}


	public static boolean isMyApplicationTaskOnTop(Context context) {
		String packageName = context.getPackageName();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
		if(recentTasks != null && recentTasks.size() > 0) {
			RunningTaskInfo t = recentTasks.get(0);
			// com.android.launcher
			String pack = t.baseActivity.getPackageName();
			if(pack.equals(packageName)) {
				return true;
			}
		}
		return false;
	}
}