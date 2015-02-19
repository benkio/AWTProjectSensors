<!DOCTYPE html>
<html>
    <head>
        <title>Advanced Web Tecnology Sensors Manager</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/main.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script>
           
            function updateActuatorValue(num,operation){
                var currentVal = parseInt($("progress[name*="+num+"]").val());
                if (!isNaN(currentVal)) {
                  $("progress[name*="+num+"]").val( operation(currentVal,1) );
                  var newVal = parseInt($("progress[name*="+num+"]").val());
                  $("p[name*='Value"+num+"']").text(newVal+'%');
                } 
            }
            //Events
            $(document).ready(function(){
                $("input[name*='Plus']").click(function(){
                    //TODO PLUS BUTTON ACTUATOR
                    var numeric_part = $(this).attr('name').substr(18);
                    console.log("plus button pressed " + numeric_part);
                    
                    updateActuatorValue(numeric_part, function(x,y){ return x+y });
                });
            
                $("input[name*='Minus']").click(function(){
                    //TODO Minus BUTTON ACTUATOR
                    var numeric_part = $(this).attr('name').substr(19);
                    console.log("minus button pressed " + numeric_part);
                    
                    updateActuatorValue(numeric_part, function(x,y){ return x-y });
                });
            });
        </script>
    </head>
    <body>
        
        <%@ include file="/WEB-INF/jspf/header.jspf" %>

        <%@ include file="/WEB-INF/jspf/navbar.jspf" %>        
            
        <table border="0" cellpadding="1" cellspacing="1" id="ActuarorsTable">
            <thead>
                <tr>
                    <th scope="col">Nome Attuatore</th>
                    <th scope="col">Valore Attuatore</th>
                    <th scope="col">Operazioni</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><p name="ActuatorName1">1</p></td>
                    <td>
                        <progress name="ActuatorValueProgress1" value="0" max="100"></progress>
                        <p class="ActuatorValue" name="ActuatorValue1">0%</p>
                    </td>
                    <td>                        
                        <input name="ActuatorMinusButton1" type="button" value="-" />
                        <input name="ActuatorPlusButton1" type="button" value="+" />
                    </td>
                </tr>
                <tr>
                    <td><p name="ActuatorName2">2</p></td>
                    <td>
                        <progress name="ActuatorValueProgress2" value="0" max="100"></progress>
                        <p class="ActuatorValue" name="ActuatorValue2">0%</p>
                    </td>
                    <td>
                        <input name="ActuatorMinusButton2" type="button" value="-" />
                        <input name="ActuatorPlusButton2" type="button" value="+" />                        
                    </td>
                </tr>
                <tr>
                    <td><p name="ActuatorName3">3</p></td>
                    <td>
                        <progress name="ActuatorValueProgress3" value="0" max="100"></progress>
                        <p class="ActuatorValue" name="ActuatorValue3">0%</p>
                    </td>
                    <td>
                        <input name="ActuatorMinusButton3" type="button" value="-" />
                        <input name="ActuatorPlusButton3" type="button" value="+" />
                    </td>
                </tr>
                <tr>
                    <td><p name="ActuatorName4">4</p></td>
                    <td>
                        <progress name="ActuatorValueProgress4" value="0" max="100"></progress>
                        <p class="ActuatorValue" name="ActuatorValue4">0%</p>
                    </td>
                    <td>
                        <input name="ActuatorMinusButton4" type="button" value="-" />
                        <input name="ActuatorPlusButton4" type="button" value="+" />
                    </td>
                </tr>
            </tbody>
        </table>
        
        <script>
            function loadInitialActuator(){
                var actuator_attr, actuator, i;
                actuator = xmlhttp.responseXML.documentElement.getElementsByTagName("Actuator");
                for (i = 1; i <= actuator.length; i++) {
                    actuator_attr = actuator[i].getElementsByTagName("ActuatorName"); {
                        try {
                            $("p[name*='Name"+i+"']").text(actuator_attr[0].firstChild.nodeValue);
                        } catch (er) {
                            $("p[name*='Name"+i+"']").text("XML Fetch Error");
                        }
                    }
                    actuator_attr = actuator[i].getElementsByTagName("ActuatorValue"); {
                        try {
                            updateActuatorValue(i,function(x,y){ return actuator_attr[0].firstChild.nodeValue }); 
                        } catch (er) {
                            $("p[name*='Name"+i+"']").text("XML Fetch Error");
                        }
                    }
                }
            }
            function loadXMLDoc(url, loadFunction) {
                var xmlhttp;                
                if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp = new XMLHttpRequest();
                } else { // code for IE6, IE5
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange = function() {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                        loadFunction();
                    }
                };
                xmlhttp.open("POST", url, true);
                xmlhttp.send();
            }
            
            function sendActuatorValue(url,actuatorNum) {
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.open("POST", url , false);
                xmlhttp.setRequestHeader("Content-Type", "text/xml");
                var data = document.implementation.createDocument("", "", null); 
                
                var actuatorTag = data.createElement("Actuator");
                
                var actuatorTagName = data.createElement("ActuatorName"); 
                var actuatorName = data.createTextNode($("p[name*='Name"+actuatorNum+"']").val());
                
                var actuatorTagValue = data.createElement("ActuatorValue"); 
                var actuatorValue = data.createTextNode($("p[name*='Value"+actuatorNum+"']").val());
                
                
                actuatorTagName.appendChild(actuatorName);
                actuatorTagValue.appendChild(actuatorValue);
                actuatorTag.appendChild(actuatorTagName);
                actuatorTag.appendChild(actuatorTagValue);
                data.appendChild(actuatorTag);
                
                xmlhttp.send(data);
                if (xmlhttp.status != 200)
                    alert("Error sending the value to the server");
            }
        </script>
    </body>
</html> 