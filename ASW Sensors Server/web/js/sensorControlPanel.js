/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function sensorToHTML(sensorID, sensorStatus, playable, contextPath) {
    var icon = contextPath + "/img/stopIcon.png";
    if (playable) icon = contextPath + "/img/playIcon.png";
    return "<tr><td><p name=\"SensorName"+ sensorID + "\" >"+sensorID+"</p></td><td><p name=\"SensorStatus"+sensorID+"\" >"+sensorStatus+"</p></td><td><input name=\"SensorControlButton"+sensorID+"\" type=\"image\" src="+icon+" height=\"32\" width=\"32\"></td></tr>";   
}