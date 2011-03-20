/*
 * our class for handling click on widget
 */

package jjsan.widget.callback;

//imports
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
import android.widget.Toast;

//definition of Activity Class
public class ClickOneActivity extends Activity {

	//some constants to handle calls without number
	public String callerPhoneNumber = "no_callerPhoneNumber";
	public String callerName = "no_callerName";
	
	//alertbox so we can use it widely ;) but its private
	private AlertDialog.Builder alertbox;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Log.d("CallBack Widget >> ","Start");

			//get our saved preferences
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());   
			//show confirmation dialog? if not saved set it to true
			String showDialog = prefs.getString("show_dialog","true");
			Log.d("CallBack Widget >> ","Cursor");

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
			Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
			Log.d("CallBack Widget >> ","SQL Error");
			}
			Log.d("CallBack Widget >> ","Move to first");
			try	{ 
				boolean moveToFirst=c.moveToFirst(); 
				if (moveToFirst) {};
				}

			catch(Exception e) { ; 
			// could not move to the first row. return; 
			Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
			Log.d("CallBack Widget >> ","Move to first");
			}
			Log.d("CallBack Widget >> ","Index");
			int numberColumn = 0;
			try	{
					numberColumn = c.getColumnIndex(
					android.provider.CallLog.Calls.NUMBER);
					//just first number
			}
			catch(Exception e) { ; 
			// could not move to the first row. return; 
			//Toast.makeText(getApplicationContext(), "Error moving to first ", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
			Log.d("CallBack Widget >> ","Column Index");
			}
			Log.d("CallBack Widget >> ","Phone number");
			try	{
			if(c.isFirst()){
				//do{
					callerPhoneNumber = c.getString(numberColumn);
				//}while(c.isLast());
			}
			}
			catch(Exception e) { ; 
			// could not move to the first row. return; 
			Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
			Log.d("CallBack Widget >> ","Move to first");
			}

			//get the name of caller
			Log.d("CallBack Widget >> ","Caller Name");
			try	{
			c = getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(callerPhoneNumber)), new String[] {PhoneLookup.DISPLAY_NAME}, null, null, null);
	         while(c.moveToNext()){
	        	 callerName = c.getString(c.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
	         }
			}
			catch(Exception e) { ; 
			Log.d("CallBack Widget >> ","get name");
			}
			
			//log variables for debug
			Log.d("CallBack Widget >> ","Variables:");
			Log.d("CallBack Widget >> ","CallerName: "+callerName);
			Log.d("CallBack Widget >> ","CallerNumber: "+callerPhoneNumber);
			Log.d("CallBack Widget >> ","------------------------------------");
			//is call log empty?
			if (callerName.equalsIgnoreCase("no_callerName")
					&&callerPhoneNumber.equalsIgnoreCase("no_callerPhoneNumber")) 
			{
				Log.d("CallBack Widget >> ","Clean CallLog");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getString(R.string.noCallLog))
				       .setCancelable(false)
				       .setPositiveButton(getString(R.string.strOK), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                finish();
				           }
				       });
				
				AlertDialog alert = builder.create();
				alert.show();
				
			}
				
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
			Log.d("CallBack Widget >> ","Alert");
			
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
    			//try to make a call
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
