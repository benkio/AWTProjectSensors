<!DOCTYPE html>
<html>
    <head>
        <title>Advanced Web Tecnology Sensors Manager</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/main.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script type="text/javascript" > var contextPath='<%=request.getContextPath()%>'; </script>
        <script src="<%= request.getContextPath()%>/js/requestBuilder.js" type="text/javascript"></script>
        <script src="<%= request.getContextPath()%>/js/sensorControlPanel.js" type="text/javascript"></script>
        <script src="<%= request.getContextPath()%>/js/sensorControlPanelXmlBuilder.js" type="text/javascript"></script>
    </head>
    <body>
        
        <%@ include file="/WEB-INF/jspf/header.jspf" %>

        <%@ include file="/WEB-INF/jspf/navbar.jspf" %>      
        
        <%@include file="/WEB-INF/jspf/sessionRedirect.jspf" %>
        
        <%@include file="/WEB-INF/jspf/adminRedirect.jspf" %>

        <!-- RIA Sensors Control Panel -->
        <table border="0" cellpadding="1" cellspacing="1" class="ManageTable">
            <thead>
                <tr>
                    <th scope="col">Nome Sensore</th>
                    <th scope="col">Stato Sensore</th>
                    <th scope="col">
                        <div>
                            <p id="operationCaption">Operazioni</p>
                            <input id="addSensorButtonID" type="image" name="addSensorButton" src="<%= request.getContextPath()%>/img/addIcon.png">
                        </div>
                    </th>
                </tr>
            </thead>
            <tbody id="SensorControlPanelTableBody">
            </tbody>
        </table>
        
        
        <script>  
            //Made the request of the actuators and render the result.
            XMLRequestPattern("../Sensors", loadInitialSensor/*function (xmlhttp) { console.log(new XMLSerializer().serializeToString(xmlhttp.responseXML.documentElement));}*/,GetSensorsXML);
            
        </script>
    </body>
</html>
