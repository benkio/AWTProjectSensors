<!DOCTYPE html>
<html>
    <head>
        <title>Advanced Web Tecnology Sensors Manager</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="style-sheets/main.css" />
    </head>
    <body>
        
        <%@ include file="/WEB-INF/jspf/header.jspf" %>

        <%@ include file="/WEB-INF/jspf/navbar.jspf" %>        
        
        <article id='WelcomeMessage'>
            <h3>
                Welcome to the project of Advanced Web Tecnology
                <br/>
                by Enrico Benini and Thomas Farneti
            </h3>
            <p>
                The main idea of this project is to simulate a control panel for a set of sensors.
                All the functionality can be accessed only after the authentication process. 
                The simple user you can only see the values of the sensors, their types, value and act on a set of actuators.
                This changes causes some effects back on the sensors.
                The admin can also modify the sensors in term of: disable/enable, add/remove sensors.
                In particular with this site you can:</p>
                <ul>
                    <li>Register and Login in the sensor system</li>
                    <li>Access and view the values of the sersors(if autheticated)</li>
                    <li>Access, view and act on the actuators</li>
                    <li>Access, view, add or remove, start or stop the sensors(if admin)</li>
                </ul>
        </article>        
    </body>
</html>
