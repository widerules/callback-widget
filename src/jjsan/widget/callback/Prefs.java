package jjsan.widget.callback;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.widget.RemoteViews;

public class Prefs extends PreferenceActivity {
	private int appWidgetId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the data we were launched with
	    Intent launchIntent = getIntent();
	    Bundle extras = launchIntent.getExtras();
	    if (extras != null) {
	        appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

	        Intent cancelResultValue = new Intent();
	        cancelResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	        setResult(RESULT_CANCELED, cancelResultValue);
	    } else {
	        // Only launch if it's for configuration
	        finish();
	    }
		
		addPreferencesFromResource(R.xml.prefs);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK)
		{
			
						
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

	            Intent resultValue = new Intent();
	            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	            setResult(RESULT_OK, resultValue);
            
	            Context context = getApplicationContext();
	            
	            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
	            Intent configIntent = new Intent(context, Prefs.class);
	            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	            
	            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, configIntent, 
	            												PendingIntent.FLAG_UPDATE_CURRENT);
	            
	            views.setOnClickPendingIntent(R.id.callbackwidget, pendingIntent);

	            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views);
			}

	        // Activity is now done
	        finish();
		
		return(super.onKeyDown(keyCode, event));
	}
	
	return true;
	}
}
	

