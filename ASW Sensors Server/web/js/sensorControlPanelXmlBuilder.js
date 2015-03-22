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
        var disableSensorsElem = doc.createElement("disableSensor");

        var sensorIDElement = doc.createElement("id");
        var sensorIDTextElement = doc.createTextNode(sensorID);
        sensorIDElement.appendChild(sensorIDTextElement);

        disableSensorsElem.appendChild(sensorIDElement);

        doc.appendChild(disableSensorsElem);
        console.log(new XMLSerializer().serializeToString(doc));
        return doc;
    };
}

function GetRemoveXMLFunction(sensorID) {
    return function () {
        var doc = document.implementation.createDocument("", "", null);
        var disableSensorsElem = doc.createElement("removeSensor");

        var sensorIDElement = doc.createElement("id");
        var sensorIDTextElement = doc.createTextNode(sensorID);
        sensorIDElement.appendChild(sensorIDTextElement);

        disableSensorsElem.appendChild(sensorIDElement);

        doc.appendChild(disableSensorsElem);
        console.log(new XMLSerializer().serializeToString(doc));
        return doc;
    };
}

function GetActiveXMLFunction(sensorID){
    return function () {
        var doc = document.implementation.createDocument("", "", null);
        var enableSensorsElem = doc.createElement("enableSensor");

        var sensorIDElement = doc.createElement("id");
        var sensorIDTextElement = doc.createTextNode(sensorID);
        sensorIDElement.appendChild(sensorIDTextElement);

        enableSensorsElem.appendChild(sensorIDElement);

        doc.appendChild(enableSensorsElem);
        console.log(new XMLSerializer().serializeToString(doc));
        return doc;
    };
}

function GetAddSensorXML(){
    var doc = document.implementation.createDocument("", "", null);
    var addSensorsElem = doc.createElement("addSensor");
    doc.appendChild(addSensorsElem);
    console.log(new XMLSerializer().serializeToString(doc));
    return doc;
}