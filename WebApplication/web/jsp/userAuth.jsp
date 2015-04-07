<!DOCTYPE html>
<html>
    <head>
        <title>Advanced Web Tecnology Sensors Manager</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/main.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    </head>
    <body>

        <%@ include file="/WEB-INF/jspf/header.jspf" %>

        <%@ include file="/WEB-INF/jspf/navbar.jspf" %>        

        <%@include file="/WEB-INF/jspf/sessionRedirect.jspf" %>

        <!-- TODO: Insert controls to add and remove association through users to sensors -->
        <form method="POST">
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
                    <%= request.getAttribute("sensorList")%>
                </tbody>          
            </table>  
            <input type="submit" value="Send Preferences" name="sendAuth" />
        </form>
                <h3 name="ErrorMessage" class="error"></h3>

        <script type="text/javascript" >
            var errorMessage = $("h3[name='ErrorMessage']");

            function refreshCheckboxValues(numeric_part, checked) {
                var sensorName = $("td[name='SensorName" + numeric_part + "']");
                var sensorStatus = $("td[name='SensorStatus" + numeric_part + "']");
                if (checked) {
                    //Do stuff
                    sensorName.css({color: "Black"});
                    sensorStatus.css({color: "Black"});
                } else {
                    sensorName.css({color: "DarkGray"});
                    sensorStatus.css({color: "DarkGray"});
                }
            }

            $(document).ready(function () {
                $("input[name*='SensorEnable']").each(function () {
                    var numeric_part = $(this).attr('name').substr(12);
                    refreshCheckboxValues(numeric_part, $(this).prop('checked'));
                });
            });

            $("input[name*='SensorEnable']").change(function () {
                var numeric_part = $(this).attr('name').substr(12);
                refreshCheckboxValues(numeric_part, $(this).prop('checked'));
            });

            $("input[name='sendAuth']").click(function () {
                var data = $.map($("input[name*='SensorEnable']").toArray(), function (val, i) {
                    var numeric_part = val.getAttribute("name").substr(12);
                    return {
                        name: $("td[name='SensorName" + numeric_part + "']").text(),
                        enable: val.checked
                    };
                });
                var sensorNames = data.map(function (elem) {
                    return elem['name'];
                });
                var sensorEnabled = data.map(function (elem) {
                    return elem['enable'];
                });

                $.ajax({
                    method: "POST",
                    url: "<%= request.getContextPath()%>/UserSensorsServlet",
                    data: 'names=' + sensorNames + '&enabled=' + sensorEnabled,
                    fail: function () {
                                errorMessage.html("<strong>ERROR occurred:</strong> something goes wrong in the server! ");
                            } 
                });
            });

        </script>

    </body>
</html>