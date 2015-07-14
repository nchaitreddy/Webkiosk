package com.blackMonster.webkiosk.refresher;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.blackMonster.webkiosk.M;
import com.blackMonster.webkiosk.controller.Timetable;
import com.blackMonster.webkiosk.utils.NetworkUtils;

public class ServiceRefreshTimetable extends IntentService {
	public static boolean RUNNING_STATUS = false;
	public static final String TAG = "serviceRefreshTimetable";

	public ServiceRefreshTimetable() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		M.log("ServiceRefreshTimetable", "onHandleIntent");
		new Thread() {
			public void run() {
				try {
					startExecution();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	private void startExecution() {
		if (!NetworkUtils.isInternetAvailable(this))
			return;
		RUNNING_STATUS = true;
		printStatus();
		Timetable.handleChangesRefresh(this);
		RUNNING_STATUS=false;
	}

	public static void runIfNotRunning(Context context) {
		printStatus();
		if (RUNNING_STATUS == false) {
			Intent intent = new Intent(context.getApplicationContext(),
					ServiceRefreshTimetable.class);
			context.getApplicationContext().startService(intent);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RUNNING_STATUS = false;
		M.log("ServiceRefreshTimetable", "onDestroy");
		printStatus();
	}

	static void printStatus() {
		M.log("ServiceRefreshTimetable", "status " + RUNNING_STATUS);
	}
}
