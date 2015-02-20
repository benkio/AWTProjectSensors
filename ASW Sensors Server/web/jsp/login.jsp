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
        
        <%-- any content can be specified here e.g.: --%>
        <div id="login" class="form">
            <form method="post" action="<%= request.getContextPath()%>/LoginServlet" autocomplete="on">
                <p>
                    <label for="username" class="uname" >Username</label>
                    <input id="username" name="username" required="required" type="text" placeholder="myusername"/>
                </p>
                <p>
                    <label for="password" class="youpasswd">Password</label>
                    <input id="password" name="password" required="required" type="password" placeholder="password" />
                </p>
                <p>
                    <input type="submit" value="Login" />
                </p>
                <p id="loginError">
                    <% if (request.getAttribute("errorMsg") != null) {%>
                        <%= request.getAttribute("errorMsg") %>
                    <% }%>
                        
                </p>
            </form>
        </div>
        
    </body>
</html>