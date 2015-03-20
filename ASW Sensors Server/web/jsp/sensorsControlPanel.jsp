<!DOCTYPE html>
<html>
    <head>
        <title>Advanced Web Tecnology Sensors Manager</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/main.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
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
                    <th scope="col">Operazioni</th>
                </tr>
            </thead>
            <tbody id="SensorControlPanelTableBody">
                <tr>
                    <td><p name="SensorName1">Nome Sensore Test</p></td>
                    <td><p name="SensorStatus1">Stato Sensore Test</p></td>
                    <td>
                        <input name="SensorControlButton1" type="image" src="<%= request.getContextPath()%>/img/stopIcon.png" height="32" width="32">
                    </td>
                </tr>
                
            </tbody>
        </table>
        
        
        <script>  
            //Made the request of the actuators and render the result.
            XMLRequestPattern("../Sensors",undefined/*TODO RESPONSE FUNCTION MANAGEMENT*/,GetSensorsXML);
            
        </script>
    </body>
</html>
