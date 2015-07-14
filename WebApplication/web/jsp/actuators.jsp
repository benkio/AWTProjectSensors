<!DOCTYPE html>
<html>
    <head>
        <title>Advanced Web Tecnology Sensors Manager</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/main.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="<%= request.getContextPath()%>/js/requestBuilder.js" type="text/javascript"></script>
        <script src="<%= request.getContextPath()%>/js/actuatorsXmlBuilder.js" type="text/javascript"></script>
        <script src="<%= request.getContextPath()%>/js/actuators.js" type="text/javascript"></script>
    </head>
    <body>
        
        <%@ include file="/WEB-INF/jspf/header.jspf" %>

        <%@ include file="/WEB-INF/jspf/navbar.jspf" %>     
        
        <%@include file="/WEB-INF/jspf/sessionRedirect.jspf" %>
                    
        <table border="0" cellpadding="1" cellspacing="1" class="ManageTable">
            <thead>
                <tr>
                    <th scope="col">Nome Attuatore</th>
                    <th scope="col">Valore Attuatore</th>
                    <th scope="col">Operazioni</th>
                </tr>
            </thead>
            <tbody id="ActuatorsTableBody">
            <tr>
                <td/>
                <td>
                    <input type="button" name="sendActuatorsValue" value="Send Value of Actuators" onclick="sendActuatorsValue()" />
                </td>
                <td/>
            </tr>
            </tbody>
        </table>
        <p id="errorMessage" class="error"></p>
        <script>  
            //Made the request of the actuators and render the result.
            XMLRequestPattern("../actuators",loadInitialActuator,GetActuatorsXML);
        </script>
    </body>
</html> 