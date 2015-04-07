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
        <div id="register" class="form">
            <form method="post" action="<%= request.getContextPath()%>/RegistrationServlet" autocomplete="on">
                <p>
                    <label for="usernamesignup" class="uname" data-icon="u">Username</label>
                    <input id="usernamesignup" name="usernamesignup" required="required" type="text" placeholder="username" />
                </p>
                <p>
                    <label for="emailsignup" class="youmail" data-icon="e" >Email</label>
                    <input id="emailsignup" name="emailsignup" required="required" type="email" placeholder="email@mail.com"/>
                </p>
                <p>
                    <label for="passwordsignup" class="youpasswd" data-icon="p">Password</label>
                    <input id="passwordsignup" name="passwordsignup" required="required" type="password" placeholder="password"/>
                </p>
                <p class="signin button">
                    <input type="submit" value="Sign up"/>
                </p>
            </form>
        </div>
        
    </body>
</html>