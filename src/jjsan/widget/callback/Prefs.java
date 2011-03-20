package jjsan.widget.callback;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.RemoteViews;

public class Prefs extends PreferenceActivity {
	//widget id
	private int appWidgetId;
	
	public Prefs() {
		super();
//	/	Toast.makeText(getApplicationContext(), "Constructor", Toast.LENGTH_SHORT);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//show preferences
		addPreferencesFromResource(R.xml.prefs);
	}

	//back key pressed
	@Override
	public void onBackPressed() {
			//get an app context
			Context context = Prefs.this;
			
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			if (extras != null) {
			    appWidgetId = extras.getInt(
			            AppWidgetManager.EXTRA_APPWIDGET_ID, 
			            AppWidgetManager.INVALID_APPWIDGET_ID);
			}
			
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			
			//set up widget
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
			appWidgetManager.updateAppWidget(appWidgetId, views);
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			setResult(RESULT_OK, resultValue);
			
			// send an broadcast to onreceive so we can use onupdate for the first time
		    Intent updateIntent = new Intent(this, CallBackWidget.class);
		    updateIntent.setAction("PreferencesUpdated");
		    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		    sendBroadcast(updateIntent);
		    
		    //done
			finish();
			
	}
}