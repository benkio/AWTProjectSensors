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
        
        <!-- TODO: Insert controls to add and remove association through users to sensors -->
        <table border="0" cellpadding="1" cellspacing="1" class="ManageTable">
            <thead>
                <tr>
                    <th scope="col">Nome Sensore</th>
                    <th scope="col">Stato Sensore</th>
                    <th scope="col">
                        <div>
                            <p>Abilita Visualizzazione</p>
                        </div>
                    </th>
                </tr>
            </thead>
            <tbody id="UserAuthTableBody">
                <jsp:include page="/UserAuthServlet" flush="true"></jsp:include>
            </tbody>
        </table>
        
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