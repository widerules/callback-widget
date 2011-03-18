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
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class ClickOneActivity extends Activity {

	public String callerPhoneNumber = "no_callerPhoneNumber";
	public String callerName = "no_callerName";
	private AlertDialog.Builder alertbox;	
	@Override
		
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());   
			String showDialog = prefs.getString("show_dialog","true");

		    //we need last number
			// Querying for a cursor is like querying for any SQL-Database
			Cursor c = null;
			try	{
			c = getContentResolver().query(
					android.provider.CallLog.Calls.CONTENT_URI,
					null, null, null, 
					android.provider.CallLog.Calls.DATE + " DESC");
					startManagingCursor(c);
			}
			catch(Exception e) { ; 
			}

			try	{ 
				boolean moveToFirst=c.moveToFirst(); 
				if (moveToFirst) {};
				}

			catch(Exception e) { ; 
			// could not move to the first row. return; 
			//Toast.makeText(getApplicationContext(), "Error moving to first ", Toast.LENGTH_SHORT).show();
			}
			
			int numberColumn = 0;
			try	{
					numberColumn = c.getColumnIndex(
					android.provider.CallLog.Calls.NUMBER);
					//just first number
			}
			catch(Exception e) { ; 
			// could not move to the first row. return; 
			//Toast.makeText(getApplicationContext(), "Error moving to first ", Toast.LENGTH_SHORT).show();
			}
			
			try	{
			if(c.isFirst()){
				//do{
					callerPhoneNumber = c.getString(numberColumn);
				//}while(c.isLast());
			}
			}
			catch(Exception e) { ; 
			// could not move to the first row. return; 
			}
			
			try	{
			c = getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(callerPhoneNumber)), new String[] {PhoneLookup.DISPLAY_NAME}, null, null, null);
	         while(c.moveToNext()){
	        	 callerName = c.getString(c.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
	         }
			}
			catch(Exception e) { ; 
			// could not move to the first row. return; 
			}

	        
			//Toast.makeText(getApplicationContext(), "Alert dialog", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), callerName, Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), callerPhoneNumber, Toast.LENGTH_SHORT).show();
			//do we have a number?
			if (callerName.equalsIgnoreCase("no_callerName")&&callerPhoneNumber.equalsIgnoreCase("-1")) 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getString(R.string.noPhoneNumber))
				       .setCancelable(false)
				       .setPositiveButton(getString(R.string.strOK), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                finish();
				           }
				       });
				
				AlertDialog alert = builder.create();
				alert.show();
			}

			
			if (showDialog.equalsIgnoreCase("true")) {
			//ask a question ;)
			
			alertbox = new AlertDialog.Builder(this);
			
			
            // Set the message to display
            if (callerName.equalsIgnoreCase("no_callerName")) {
            	alertbox.setMessage(getString(R.string.qeuCallBack)+"\n"+callerPhoneNumber+" ?");
            }	
            else
            {
            	if (!callerName.equalsIgnoreCase("no_callerName"))
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
        		        callIntent.setData(Uri.parse("tel: "+callerPhoneNumber));
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
            if (!callerName.equalsIgnoreCase("no_callerName")&&!callerPhoneNumber.equalsIgnoreCase("-1"))
            {
            	alertbox.show();
			}
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
			
		}
}
