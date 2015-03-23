<!DOCTYPE html>
<html>
    <head>
        <title>Advanced Web Tecnology Sensors Manager</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/main.css" />
    </head>
    <body>
        
        <%@ include file="/WEB-INF/jspf/header.jspf" %>

        <%@ include file="/WEB-INF/jspf/navbar.jspf" %>        
        
        <%@include file="/WEB-INF/jspf/sessionRedirect.jspf" %>
        
        <%@include file="/WEB-INF/jspf/adminRedirect.jspf" %>
        
        <!-- TODO: Insert controls to add and remove association through users to sensors -->
        
        <table id="usersTable">
            <thead>
                <tr>
                    <td>
                        Utenti
                    </td>
                </tr>
            </thead>
            <tbody id="usersAuthUsersList">
                <!-- HTML GENERATED FROM THE SERVLET -->
            </tbody>
        </table>
        
        
        <table id="sensorsTable">
            <thead>
                <tr>
                    <td>
                        Sensori
                    </td>
                </tr>
            </thead>
            <tbody id="usersAuthUserSensors">
                <!-- HTML GENERATED FROM THE SERVLET -->
            </tbody>
        </table>
        
    </body>
</html>