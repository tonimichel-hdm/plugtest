package org.tonimichel.sensorplugin;
 
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.*;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

//import android.app.Activity;
import android.app.*;

//import android.content.Intent;
import android.content.*;

public class SensorPlugin extends CordovaPlugin  {
    public static final String ACTION_ADD_CALENDAR_ENTRY = "addCalendarEntry";
    public static final String ECHO = "doEcho";
    public static final String GET_SENSOR_LIST = "getSensorList";
       
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        try {
            if (ACTION_ADD_CALENDAR_ENTRY.equals(action)) { 
                this.calendar( args, callbackContext );
                return true;
            }
            
            if (ECHO.equals(action)) {
                this.doEcho ( args, callbackContext );
                return true;
            }
            
            if (GET_SENSOR_LIST.equals(action)) {
            	this.getSensorList(callbackContext);
            	return true;
            }
            
            callbackContext.error("Invalid action");
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }
    
    private void doEcho ( JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject arg_object = args.getJSONObject(0);
        callbackContext.success(arg_object.getString("message"));
    }
    
    private void getSensorList(CallbackContext callbackContext) {
    	/* Setup Sensor Manager */ 
    	Context ctx = this.cordova.getActivity().getApplicationContext();
        SensorManager mSensorManager;
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
    	// List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        
    	/* Type Conversion fuer Kommunikation mit JS */
    	JSONArray jsonDeviceSensors = poolAllSensors( mSensorManager );
    	
    	/* Callback */
    	callbackContext.success(jsonDeviceSensors);
    }
    
    public JSONArray poolAllSensors(SensorManager Sensors) {
		List<Sensor> SensorList = Sensors.getSensorList( Sensor.TYPE_ALL );
		
		/* Loop through all sensor objects and create a JSON object */
		JSONArray rtnJSON = new JSONArray();	
		for( Sensor s : SensorList ){
			JSONObject o = new JSONObject();
			
			try {
				o.put( "vendor",		s.getVendor()				);
				o.put( "name",			s.getName() 				);
				o.put( "type",			checkType( s.getType() ) 	);
				o.put( "version",		s.getVersion() 				);
				o.put( "maxRange",		s.getMaximumRange() 		);
				//o.put( "minDelay",		s.getMinDelay() 		);
				o.put( "power",			s.getPower() 				);
				o.put( "resolution",	s.getResolution() 			);
				
				rtnJSON.put(o);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			
		} //EOF for() loop
		
		return rtnJSON;
	}    
    
    @SuppressWarnings("deprecation")
	public static String checkType(int type){
		
		switch(type){
		
			case Sensor.TYPE_ACCELEROMETER : 
				return "Accelerometer";

			case Sensor.TYPE_AMBIENT_TEMPERATURE :
				return "Ambient Temperature";
			
			case Sensor.TYPE_LIGHT :
				return "Light";
			
			case Sensor.TYPE_GRAVITY :
				return "Gravity";
				
			case Sensor.TYPE_GYROSCOPE :
				return "Gyroscope";
				
			case Sensor.TYPE_LINEAR_ACCELERATION :
				return "Linear Acceleration";
				
			case Sensor.TYPE_MAGNETIC_FIELD :
				return "Magnetic Field";
				
			case Sensor.TYPE_PRESSURE :
				return "Pressure";
				
			case Sensor.TYPE_PROXIMITY :
				return "Proximity";
			
			case Sensor.TYPE_RELATIVE_HUMIDITY :
				return "Relative Humidity";
				
			case Sensor.TYPE_ROTATION_VECTOR :
				return "Rotation Vector";
				
			/* These are deprecated - however, they are required as of Android 4.1 to correctly
			 * identify certain forms of sensor. Yes. Deprecated but even required by the official
			 * SDK, Emulator and latest Android image... You heard it here folks; Google is so
			 * hipster that they even rock out deprecated constants.				
			 */
			 case Sensor.TYPE_ORIENTATION :
				return "Orientation";
			
			 case Sensor.TYPE_TEMPERATURE :
				return "Temperature";
				
			default:
				return "Unknown";
				
		}
	}

    
    private void calendar ( JSONArray args, CallbackContext callbackContext ) throws JSONException {
        JSONObject arg_object = args.getJSONObject(0);
        Intent calIntent = new Intent(Intent.ACTION_EDIT)
            .setType("vnd.android.cursor.item/event")
            .putExtra("beginTime", arg_object.getLong("startTimeMillis"))
            .putExtra("endTime", arg_object.getLong("endTimeMillis"))
            .putExtra("title", arg_object.getString("title"))
            .putExtra("description", arg_object.getString("description"))
            .putExtra("eventLocation", arg_object.getString("eventLocation"));
             
        this.cordova.getActivity().startActivity(calIntent);
        callbackContext.success();    
    }
    
}