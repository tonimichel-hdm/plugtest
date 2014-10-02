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

public class SensorPlugin extends CordovaPlugin implements SensorEventListener {
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
        SensorManager mSensorManager;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        
    	/* Type Conversion fuer Kommunikation mit JS */
    	JSONArray jsonDeviceSensors = new JSONArray(deviceSensors);
    	
    	/* Callback */
    	callbackContext.success(jsonDeviceSensors);
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