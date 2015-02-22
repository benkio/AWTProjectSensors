/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function loadInitialActuator() {
    var actuator_name, actuator_value, actuator, i;
    actuator = xmlhttp.responseXML.documentElement.getElementsByTagName("Actuator");
    for (i = 1; i <= actuator.length; i++) {
        actuator_name = actuator[i].getElementsByTagName("ActuatorName");
        actuator_value = actuator[i].getElementsByTagName("ActuatorValue");
        $("#TableBody").html(actuatorToHTML(actuator_name, actuator_value));
        try {
            $("p[name*='Name" + i + "']").text(actuator_name[0].firstChild.nodeValue);
            updateActuatorValue(i, function (x, y) {
                return actuator_value[0].firstChild.nodeValue;
            });
        } catch (er) {
            $("p[name*='Name" + i + "']").text("XML Fetch Error");
        }
    }
}
function loadXMLDoc(url, loadFunction, xmlRequest) {
    var xmlhttp;
    if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else { // code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            loadFunction();
        }
    };
    xmlhttp.open("POST", url, true);
    xmlhttp.send(xmlRequest);
}

function sendActuatorValue(url, actuatorNum) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", url, false);
    xmlhttp.setRequestHeader("Content-Type", "text/xml");
    var data = document.implementation.createDocument("", "", null);

    var actuatorTag = data.createElement("Actuator");

    var actuatorTagName = data.createElement("ActuatorName");
    var actuatorName = data.createTextNode($("p[name*='Name" + actuatorNum + "']").val());

    var actuatorTagValue = data.createElement("ActuatorValue");
    var actuatorValue = data.createTextNode($("p[name*='Value" + actuatorNum + "']").val());


    actuatorTagName.appendChild(actuatorName);
    actuatorTagValue.appendChild(actuatorValue);
    actuatorTag.appendChild(actuatorTagName);
    actuatorTag.appendChild(actuatorTagValue);
    data.appendChild(actuatorTag);

    xmlhttp.send(data);
    if (xmlhttp.status != 200)
        alert("Error sending the value to the server");
}


//Utilities functions 

function actuatorToHTML(actuatorID, actuatorValue) {
    return "<tr><td><p name=\"ActuatorName" + actuatorID + ">" + actuatorID + "</p></td><td><progress name=\"ActuatorValueProgress" + actuatorID + " value=\"0\" max=\"100\"></progress><p class=\"ActuatorValue\" name=\"ActuatorValue" + actuatorID + ">" + actuatorValue + "%</p></td><td><input name=\"ActuatorMinusButton" + actuatorID + " type=\"button\" value=\"-\" /><input name=\"ActuatorPlusButton" + actuatorID + " type=\"button\" value=\"+\" /></td></tr>";
}
function updateActuatorValue(num,operation){
    var currentVal = parseInt($("progress[name*="+num+"]").val());
    if (!isNaN(currentVal)) {
      $("progress[name*="+num+"]").val( operation(currentVal,1) );
      var newVal = parseInt($("progress[name*="+num+"]").val());
           $("p[name*='Value" + num + "']").text(newVal + '%');
    }
}