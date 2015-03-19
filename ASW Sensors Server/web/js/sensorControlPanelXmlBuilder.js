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
    var getActuatorsElem = doc.createElement("getSensors");
    doc.appendChild(getActuatorsElem);
    console.log(new XMLSerializer().serializeToString(doc));
    return doc;
}

