/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Log the response
 * @param {type} xmlhttp
 * @returns {undefined}
 */
function logResponse(xmlhttp){
    console.log(new XMLSerializer().serializeToString(xmlhttp.responseXML.documentElement));
}

/*
 * Function used to manage the first call of sensor list
 * Fetch the response and render the result to html
 * 
 * @param {type} xmlhttp
 * @returns {undefined}
 */
function loadInitialSensor(xmlhttp) {
    var sensor_name, sensor_state,sensor_type, sensor, i;
    logResponse(xmlhttp);
    sensor = xmlhttp.responseXML.documentElement.getElementsByTagName("Sensor");
    for (i = 0; i < sensor.length; i++) {
        sensor_name = sensor.item(i).getElementsByTagName("id").item(0).textContent;
        sensor_state = sensor.item(i).getElementsByTagName("state").item(0).textContent;
        sensor_type = sensor.item(i).getElementsByTagName("kind").item(0).textContent;
        $("#SensorControlPanelTableBody").prepend(sensorToHTML(sensor_name , sensor_state, function (){
            return sensor_state !== "Active";
        } ,sensor_type));
        setSensorsEventListener(sensor_name);
    }
}

/*
 * Render a new sersor to HTML with given sensorID and status
 * 
 * @param {type} sensorID
 * @param {type} sensorStatus
 * @param {type} playable: used to decide what icon choose
 * @returns {String}
 */
function sensorToHTML(sensorID, sensorStatus, playable, sensorKind) {
    var icon = contextPath + "/img/stopIcon.png";
    if (playable())
        icon = contextPath + "/img/playIcon.png";
    return "<tr name=\"sensorId"+sensorID+"\"><td><p name=\"SensorName"+ sensorID + "\" >"+sensorID+"</p></td><td><p name=\"SensorKind"+sensorID+"\" >"+sensorKind+"</p></td><td><p name=\"SensorStatus"+sensorID+"\" >"+sensorStatus+"</p></td><td><input name=\"SensorControlButton"+sensorID+"\" type=\"image\" src="+icon+" height=\"32\" width=\"32\"><input name=\"removeSensorControlButton"+sensorID+"\" type=\"image\" src="+ contextPath + "/img/removeIcon.png"+" height=\"32\" width=\"32\"></td></tr>";   
}
/**
 * Manage the return of a done remove operation.
 * @param {type} xmlhttp
 * @param {type} sensorId
 * @returns {undefined}
 */
function removeSensorCallback(xmlhttp, sensorId){
    logResponse(xmlhttp);
    
    $('tr[name=\"sensorId'+sensorId+'\"]').remove();
}

function changeStatusCallback(xmlhttp, sensorAttributes){
    logResponse(xmlhttp);
    
    if (sensorAttributes[1] === "Active"){
        $("p[name=\"SensorStatus"+sensorAttributes[0]+"\"]").text("Disabled");
        $("input[name=\"SensorControlButton"+sensorAttributes[0]+"\"]").attr("src", contextPath + "/img/stopIcon.png");
    }
    else{
        $("p[name=\"SensorStatus"+sensorAttributes[0]+"\"]").text("Active");
        $("input[name=\"SensorControlButton"+sensorAttributes[0]+"\"]").attr("src", contextPath + "/img/playIcon.png");
    }
}

/**
 * Add the event Listeners for button etc etc.
 * @param {type} sensorID
 * @returns {undefined}
 */
function setSensorsEventListener(sensorID) {
    $("input[name='SensorControlButton"+sensorID+"']").click(function () {
           console.log("operation button pressed:  " + sensorID);
            if ($("p[name='SensorStatus"+sensorID+"']").text("Active")){
                XMLRequestPattern("../Sensors", function (xmlhttp,param) { changeStatusCallback(xmlhttp,param,"Active");},GetOfflineXML,sensorID);
            } else {
                XMLRequestPattern("../Sensors", function (xmlhttp,param) { changeStatusCallback(xmlhttp,param,"Disabled");},GetActiveXML,sensorID);
            }
    });
    
    $("input[name='removeSensorControlButton"+sensorID+"']").click(function () {
           console.log("remove operation button pressed:  " + sensorID);
           XMLRequestPattern("../Sensors", removeSensorCallback,GetRemoveXMLS,sensorID);
    });
}

/**
 * Manage the return of the addSensorCall from the server
 * @param {type} xmlhttp
 * @param {type} kind
 * @returns {undefined}
 */
function addSensorCallback(xmlhttp, kind){
    logResponse(xmlhttp);
    
    var sensor_name = xmlhttp.responseXML.documentElement.textContent;
    $("#SensorControlPanelTableBody").prepend(sensorToHTML(sensor_name , "Active", function (){
            return false;
        } ,kind));
    setSensorsEventListener(sensor_name);    
}
/**
 * set the add sensor handler
 * @returns {undefined}
 */
function setAddSensorHandler(){
    $("input[name='addSensorButton']").click(function () {
        console.log("Add sensor operation button pressed:  ");
        XMLRequestPattern( "../Sensors", addSensorCallback, GetAddSensorXML,$( '#addSensorKind' ).val());        
    });
}
