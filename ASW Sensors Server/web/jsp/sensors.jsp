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
            
    <applet codebase="<%= request.getContextPath()%>/applet/" 
            code="awt.applet.SensorsControlPanel" 
            archive="SensorsControlPanel.jar,Lib1.jar"
            width=940 height=500>
        <param name="sessionId" value="<%= session.getId()%>">
        <param name="username" value="<%= session.getAttribute("username") %>">
        Applet failed to run. No Java plug-in was found.
    </applet>
    </body>
</html> 