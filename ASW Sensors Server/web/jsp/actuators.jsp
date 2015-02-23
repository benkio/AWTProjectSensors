<!DOCTYPE html>
<html>
    <head>
        <title>Advanced Web Tecnology Sensors Manager</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/main.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="<%= request.getContextPath()%>/js/requestBuilder.js" type="text/javascript"></script>
        <script src="<%= request.getContextPath()%>/js/xmlBuilder.js" type="text/javascript"></script>
        <script>   
            XMLRequestPattern("../actuators",loadInitialActuator,GetActuatorsXML);
        </script>
    </head>
    <body>
        
        <%@ include file="/WEB-INF/jspf/header.jspf" %>

        <%@ include file="/WEB-INF/jspf/navbar.jspf" %>     
        
        <%@include file="/WEB-INF/jspf/sessionRedirect.jspf" %>
            
        <table border="0" cellpadding="1" cellspacing="1" id="ActuarorsTable">
            <thead>
                <tr>
                    <th scope="col">Nome Attuatore</th>
                    <th scope="col">Valore Attuatore</th>
                    <th scope="col">Operazioni</th>
                </tr>
            </thead>
            <tbody id="TableBody">
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
    </body>
</html> 