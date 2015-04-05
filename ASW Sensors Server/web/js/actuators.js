/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Utility functions for:
 * - Rendering of actuators after response from the service.
 * - Setup the events Listener
 * - Update the GUI from client side
 * - Prepare the request of actuators update, see actuatorXmlBuilder
 * 
 */

/*
 * 
 * Get the actuators data from the response and update the GUI
 * 
 * @param {type} xmlhttp
 * @returns update the value of the actuators
 */
function loadInitialActuator(xmlhttp) {
    var actuator_name, actuator_value, actuator, i;
    actuator = xmlhttp.responseXML.documentElement.getElementsByTagName("list");
    for (i = 0; i < actuator.length; i++) {
        actuator_name = actuator.item(i).getElementsByTagName("id").item(0).textContent;
        actuator_value = actuator.item(i).getElementsByTagName("value").item(0).textContent;
        $("#ActuatorsTableBody").prepend(actuatorToHTML(actuator_name , actuator_value ));
        try {
            $("p[name*='Name" + actuator_name + "']").text(actuator_name );
            updateActuatorValue(actuator_name, function (x, y) {
                return actuator_value;
            });
        } catch (er) {
            $("p[name*='Name" + actuator_name + "']").text("XML Fetch Error");
        }
    }
    setEventListeners();
}

/*
 * 
 * get the value of the actuators and made a update value request forearch of them
 * 
 * @returns undefined
 */
function sendActuatorsValue() {
    $("#errorMessage").text("");
    $("progress").each(function () {
        var numeric_part = $(this).attr('name').substr(21);
        XMLRequestPattern("../actuators", function(xmlhttp){
            var doneResponse = xmlhttp.responseXML.documentElement.nodeName;
            if (doneResponse != "done") $("#errorMessage").text("Some error occurred in the send of Actuators Value"); 
        }, function () {
            return setActuatorsValueXML(numeric_part);
        });
    });
}

//Utilities functions 

/*
 * 
 * from actuatorID and value it return the rendering HTML
 * 
 * @param {type} actuatorID
 * @param {type} actuatorValue
 * @returns HTML of the actuator
 */
function actuatorToHTML(actuatorID, actuatorValue) {
    return "<tr><td><p name=\"ActuatorName" + actuatorID + "\">" + actuatorID + "</p></td><td><progress name=\"ActuatorValueProgress" + actuatorID + "\" value=\"0\" max=\"100\"></progress><p class=\"ActuatorValue\" name=\"ActuatorValue" + actuatorID + "\">" + actuatorValue + "%</p></td><td><input type=\"number\" min=\"1\" max=\"100\" step=\"1\" name=\"ActuatorSpinner"+actuatorID+"\" value=\""+actuatorValue+"\"></td></tr>";
}

/*
 * From the num of the actuator and an operation function 
 * it update the value of the progress bar and related label
 * 
 * @param {type} num of the actuator control
 * @param {type} operation function used to transform the progress value.
 * @returns Update the value of the progress bar and label related to the input actuator
 */
function updateActuatorValue(num,operation){
    var currentVal = parseInt($("progress[name*="+num+"]").val());
    if (!isNaN(currentVal)) {
      $("progress[name*="+num+"]").val( operation(currentVal,1) );
      var newVal = parseInt($("progress[name*="+num+"]").val());
           $("p[name*='Value" + num + "']").text(newVal + '%');
    }
}

//Events
/*
 * Add a change event in order to update the value of the progress bar
 * every time the spinner value change.
 * 
 * @returns {undefined}
 */
function setEventListeners() {
    $("input[name*='Spinner']").each(function(){
        $(this).change(function () {
           var numeric_part = $(this).attr('name').substr(15);
           console.log("spinner changed:  " + numeric_part);
           var value = $(this).val();

           updateActuatorValue(numeric_part, function (x, y) {
                       return value;
           });
        });
    });
};