package jjsan.widget.callback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.Toast;

public class ClickOneActivity extends Activity {

	public String callerPhoneNumber = null;
	public String callerName = null;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());   
			String showDialog = prefs.getString("show_dialog","true");
			
		    //we need last number
			//Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();
			// Querying for a cursor is like querying for any SQL-Database
			Cursor c = getContentResolver().query(
					android.provider.CallLog.Calls.CONTENT_URI,
					null, null, null, 
					android.provider.CallLog.Calls.DATE + " DESC");

			startManagingCursor(c);
			
			try	{ 
				boolean moveToFirst=c.moveToFirst(); 
				if (moveToFirst) {};
				}

			catch(Exception e) { ; 
			// could not move to the first row. return; 
			//Toast.makeText(getApplicationContext(), "Error moving to first ", Toast.LENGTH_SHORT).show();
			}

			int numberColumn = c.getColumnIndex(
					android.provider.CallLog.Calls.NUMBER);

			//just first number
			if(c.isFirst()){
				//do{
					callerPhoneNumber = c.getString(numberColumn);
				//}while(c.isLast());
			}
			
			c = getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(callerPhoneNumber)), new String[] {PhoneLookup.DISPLAY_NAME}, null, null, null);
	         while(c.moveToNext()){
	        	 callerName = c.getString(c.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
	         }
	         
			//Toast.makeText(getApplicationContext(), "Alert dialog", Toast.LENGTH_SHORT).show();
			if (showDialog.equalsIgnoreCase("true")) {
			//ask a question ;)
			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            // Set the message to display
            if (callerName.equalsIgnoreCase("null")) {
            	alertbox.setMessage(getString(R.string.qeuCallBack)+"\n"+callerPhoneNumber+" ?");
            }	
            else
            {
            	alertbox.setMessage(getString(R.string.qeuCallBack)+"\n"+callerName+"?");	
            }
             // Set a positive/yes button and create a listener
            alertbox.setPositiveButton(getString(R.string.strYes), new DialogInterface.OnClickListener() {
                // Click listener 
                public void onClick(DialogInterface arg0, int arg1) {
                    //lets call last number
                	//Toast.makeText(getApplicationContext(), "CallBack: "+callerPhoneNumber
					//		, Toast.LENGTH_SHORT).show();
        			try {
        		        Intent callIntent = new Intent(Intent.ACTION_CALL);
        		        callIntent.setData(Uri.parse("tel:"+callerPhoneNumber));
        		        startActivity(callIntent);
        		    } catch (ActivityNotFoundException e) {
        		        Log.e("CallBack Widget", "Call failed ", e);
        		    }
        		    //we can end 
        		    finish();
                }
            });
            // Set a negative/no button and create a listener
            alertbox.setNegativeButton(getString(R.string.strNo), new DialogInterface.OnClickListener() {
                // Click listener
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText(getApplicationContext(), "'No' button clicked", Toast.LENGTH_SHORT).show();
                	//we can end
                	finish();
                }
            });
            alertbox.show();
									}
			else
			{
				//Toast.makeText(getApplicationContext(), "CallBack: "+callerPhoneNumber
				//		, Toast.LENGTH_SHORT).show();
    			try {
    		        Intent callIntent = new Intent(Intent.ACTION_CALL);
    		        callIntent.setData(Uri.parse("tel:"+callerPhoneNumber));
    		        startActivity(callIntent);
    		    } catch (ActivityNotFoundException e) {
    		        Log.e("CallBack Widget", "Call failed", e);
    		    }
    		    //we can end 
    		    finish();
			}
			
            // display box
			//Toast.makeText(getApplicationContext(), "Display Alert dialog", Toast.LENGTH_SHORT).show();
		}
}
