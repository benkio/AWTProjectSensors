/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Function used to manage the first call of sensor list
 * Fetch the response and render the result to html
 * 
 * @param {type} xmlhttp
 * @returns {undefined}
 */
function loadInitialSensor(xmlhttp) {
    var sensor_name, sensor_state,sensor_type, sensor, i;
    sensor = xmlhttp.responseXML.documentElement.getElementsByTagName("Sensor");
    for (i = 0; i < sensor.length; i++) {
        sensor_name = sensor.item(i).getAttribute("id");
        sensor_state = sensor.item(i).getAttribute("state");
        sensor_type = sensor.item(i).getAttribute("kind");
        $("#SensorControlPanelTableBody").prepend(sensorToHTML(sensor_name , sensor_state, function (){
            return sensor_state !== "Active";
        } ),sensor_type);
        try {
            $("p[name*='Name" + sensor_name + "']").text(sensor_name );
        } catch (er) {
            $("p[name*='Name" + sensor_name + "']").text("XML Fetch Error");
        }
        setEventListener(sensor_name);
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
    return "<tr><td><p name=\"SensorName"+ sensorID + "\" >"+sensorID+"</p></td><td><p name=\"SensorKind"+sensorID+"\" >"+sensorKind+"</p></td><td><p name=\"SensorStatus"+sensorID+"\" >"+sensorStatus+"</p></td><td><input name=\"SensorControlButton"+sensorID+"\" type=\"image\" src="+icon+" height=\"32\" width=\"32\"><input name=\"removeSensorControlButton"+sensorID+"\" type=\"image\" src="+ contextPath + "/img/removeIcon.png"+" height=\"32\" width=\"32\"></td></tr>";   
}

function setEventListener(sensorID) {
    $("input[name='SensorControlButton"+sensorID+"']").click(function () {
           console.log("operation button pressed:  " + sensorID);
            if ($("p[name='SensorStatus"+sensorID+"']").text("Active")){
                XMLRequestPattern("../Sensors", function (xmlhttp) { console.log(new XMLSerializer().serializeToString(xmlhttp.responseXML.documentElement));},GetOfflineXMLFunction(sensorID));
            } else {
                XMLRequestPattern("../Sensors", function (xmlhttp) { console.log(new XMLSerializer().serializeToString(xmlhttp.responseXML.documentElement));},GetActiveXMLFunction(sensorID));
            }
    });
    
    $("input[name='removeSensorControlButton"+sensorID+"']").click(function () {
           console.log("remove operation button pressed:  " + sensorID);
           XMLRequestPattern("../Sensors", function (xmlhttp) { console.log(new XMLSerializer().serializeToString(xmlhttp.responseXML.documentElement));},GetRemoveXMLFunction(sensorID));
    });
};