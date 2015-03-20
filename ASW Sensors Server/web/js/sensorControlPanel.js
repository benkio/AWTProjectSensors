/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function loadInitialSensor(xmlhttp) {
    var sensor_name, sensor_state, sensor, i;
    sensor = xmlhttp.responseXML.documentElement.getElementsByTagName("Sensor");
    for (i = 0; i < sensor.length; i++) {
        sensor_name = sensor.item(i).getAttribute("id");
        sensor_state = sensor.item(i).textContent;
        $("#SensorControlPanelTableBody").prepend(sensorToHTML(sensor_name , sensor_state, function (){
            return sensor_state !== "Active";
        } ));
        try {
            $("p[name*='Name" + sensor_name + "']").text(sensor_name );
        } catch (er) {
            $("p[name*='Name" + sensor_name + "']").text("XML Fetch Error");
        }
    }
    //TODO: CREATE AN EVENT LISTENER FOR THE BUTTON
    //setEventListeners();
}


function sensorToHTML(sensorID, sensorStatus, playable) {
    var icon = contextPath + "/img/stopIcon.png";
    if (playable())
        icon = contextPath + "/img/playIcon.png";
    return "<tr><td><p name=\"SensorName"+ sensorID + "\" >"+sensorID+"</p></td><td><p name=\"SensorStatus"+sensorID+"\" >"+sensorStatus+"</p></td><td><input name=\"SensorControlButton"+sensorID+"\" type=\"image\" src="+icon+" height=\"32\" width=\"32\"></td></tr>";   
}