/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Function to build the XML Requests for the actuators RIA
 * 
 */

/*
 * Return the Get Actuator request XML
 * @returns {GetActuatorsXML.doc|Document}
 */
function GetActuatorsXML() {
    var doc = document.implementation.createDocument("", "", null);
    var getActuatorsElem = doc.createElement("getActuators");
    doc.appendChild(getActuatorsElem);
    console.log(new XMLSerializer().serializeToString(doc));
    return doc;
}

/*
 * Return the Set actuators XML Request.
 * 
 * @param {type} numeric_part
 * @returns {setActuatorsValueXML.data|Document}
 */
function setActuatorsValueXML(numeric_part) {
    var data = document.implementation.createDocument("", "", null);
    var actuatorTag = data.createElement("setValue");

    var actuatorIdTag = data.createElement("id");
    var id = $("p[name*='Name" + numeric_part + "']").text();
    var actuatorIdTextContent = data.createTextNode(id);
    actuatorIdTag.appendChild(actuatorIdTextContent);

    var actuatorValueTag = data.createElement("value");
    var value = parseInt($("progress[name*='"+numeric_part+"']").val());
    var actuatorValueContent = data.createTextNode(value);
    actuatorValueTag.appendChild(actuatorValueContent);

    actuatorTag.appendChild(actuatorIdTag);
    actuatorTag.appendChild(actuatorValueTag);

    data.appendChild(actuatorTag);

    console.log(new XMLSerializer().serializeToString(data));
    return data;
}

/**
 * Return a new xml document used to subscribe to the comet
 * 
 * @returns {undefined}
 */
function GetWaitEventXML(){
    var data = document.implementation.createDocument("", "", null);
    var waitEventTag = data.createElement("waitEvents");
    data.appendChild(waitEventTag);
    console.log(new XMLSerializer().serializeToString(data));
    return data;
}