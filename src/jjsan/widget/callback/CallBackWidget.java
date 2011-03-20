package jjsan.widget.callback;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class CallBackWidget extends AppWidgetProvider {
	
	//ok we need constants for configuration of widget
	public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

	//this method is called when the widget is updated
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//Toast message when the widget is updated
		//Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();

		//set action to configure widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
		Intent configIntent = new Intent(context, ClickOneActivity.class);
		configIntent.setAction(ACTION_WIDGET_CONFIGURE);

		//set pending intent
		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
		
		//set our button to view
		remoteViews.setOnClickPendingIntent(R.id.button_two, configPendingIntent);

		//update widget
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}
	
	//method for receiving message
	@Override
	public void onReceive(Context context, Intent intent) {
		//Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
		//get name of Action 
		final String action = intent.getAction();

	    //this onreceive is called for the first time, the widget is added to screen
		if ("PreferencesUpdated".equals(action)) {

	    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			
	    	//get an Id of our widget
	    	int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
	        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
			appWidgetManager.updateAppWidget(appWidgetId, views);
			
			//we need int[] for calling onupdate
			int[] appWidgetIds = new int[] {appWidgetId};

			//call on Update so the widget is clickable after first add
	        onUpdate(context, appWidgetManager, appWidgetIds);
	    }   		
		
		//widget deletion
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			// check, if our Action was called
			if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
				//its used in try so we need to suppress warning
				@SuppressWarnings("unused")
				String msg = "null";
				try {
					msg = intent.getStringExtra("msg");
				} catch (NullPointerException e) {
					Log.e("Error", "msg = null");
				}
				
			} else {
				// do nothing
			}
			//call super onreceive
			super.onReceive(context, intent);
		}
	}

	//method for update of widget
	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
	            int appWidgetId, String titlePrefix) {
			
			//set up view for our package and our layout
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);

	        // Tell it to the widget manager
	        appWidgetManager.updateAppWidget(appWidgetId, views);
	    }
}
