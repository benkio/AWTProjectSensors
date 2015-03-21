/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Function to build the XML Requests for the actuators RIA
 * 
 */

/*
 * Return the Get Sensors request XML
 * 
 * @returns {GetSensorsXML.doc|Document}
 */
function GetSensorsXML() {
    var doc = document.implementation.createDocument("", "", null);
    var getSensorsElem = doc.createElement("getSensors");
    doc.appendChild(getSensorsElem);
    console.log(new XMLSerializer().serializeToString(doc));
    return doc;
}

function GetOfflineXMLFunction(sensorID) {
    return function () {
        var doc = document.implementation.createDocument("", "", null);
        var getSensorsElem = doc.createElement("disableSensor");

        var sensorIDElement = doc.createElement("id");
        var sensorIDTextElement = doc.createTextNode(sensorID);
        sensorIDElement.appendChild(sensorIDTextElement);

        getSensorsElem.appendChild(sensorIDElement);

        doc.appendChild(getSensorsElem);
        console.log(new XMLSerializer().serializeToString(doc));
        return doc;
    }
}

function GetActiveXMLFunction(sensorID){
    return function () {
        var doc = document.implementation.createDocument("", "", null);
        var getSensorsElem = doc.createElement("enableSensor");

        var sensorIDElement = doc.createElement("id");
        var sensorIDTextElement = doc.createTextNode(sensorID);
        sensorIDElement.appendChild(sensorIDTextElement);

        getSensorsElem.appendChild(sensorIDElement);

        doc.appendChild(getSensorsElem);
        console.log(new XMLSerializer().serializeToString(doc));
        return doc;
    }
}

