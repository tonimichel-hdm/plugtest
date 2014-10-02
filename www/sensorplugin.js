var sensorplugin = {
    
    createEvent: function(title, location, notes, startDate, endDate, successCallback, errorCallback) {
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'SensorPlugin', // mapped to our native Java class called "CalendarPlugin"
            'addCalendarEntry', // with this action name
            [{                  // and this array of custom arguments to create our entry
                "title": title,
                "description": notes,
                "eventLocation": location,
                "startTimeMillis": startDate.getTime(),
                "endTimeMillis": endDate.getTime()
            }]
        ); 
    }, 
    
    putEcho: function ( msg, successCallback, errorCallback ) {
        cordova.exec( 
            successCallback,
            errorCallback,
            'SensorPlugin',
            'doEcho',
            [{"message": msg}]
        );
    }
    
    //Get a list of all available sensors
    
    getSensorList: function ( successCallback, errorCallback ) {
        cordova.exec(
            successCallback,
            errorCallback,
            'SensorPlugin',
            'getSensorList',
            [{"sensor": "all"}]
        );
    }
}
module.exports = sensorplugin;