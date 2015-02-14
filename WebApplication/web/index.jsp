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
                The main idea of this project is to simulate a control panel for a set of sensors. All the functionality can be accessed only after the authentication process. After that, if you are a simple user you can only see the values of the sensors else you can also act on a set of actuators than change the enviroments in some way, this changes causes some effects back on the sensors.
                In particular with this site you can:</p>
                <ul>
                    <li>Register and Login in the sensor system</li>
                    <li>Access and view the values of the sersors(if autheticated)</li>
                    <li>Access, view and act on the actuators(if admin)</li>
                </ul>
        </article>
        <!--
        
        To substitute with applet
        
        <aside>Sensors List</aside>

        -->
        
        <!--

        To substitute with applet
        <section>
            Active Sensor Name
            <article>Sensor Value 1</article>
            <article>Sensor Value 2</article>
        </section>

        -->
        
    </body>
</html>
