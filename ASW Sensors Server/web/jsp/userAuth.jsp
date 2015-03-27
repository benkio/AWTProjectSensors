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
                <tr>
                    <td name="SensorName1">test</td>
                    <td name="SensorStatus1">test</td>
                    <td>
                        <input type="checkbox" name="SensorEnable1" value="ON" checked="checked" />
                    </td>
                </tr>
                <c:set var="myContext" value="${pageContext.request.contextPath}"/>
                <jsp:include page="${myContext}/UserAuthServlet" flush="true"></jsp:include>
            </tbody>          
        </table>  

        <h3 name="ErrorMessage"><strong>ERROR occurred:</strong> something goes wrong in the server! </h3>
            
        <script type="text/javascript" >
            var errorMessage = $("h3[name='ErrorMessage']");
            errorMessage.hide();
            
            $("input[name*='SensorEnable']").each(function(){
                $(this).change(function() {
                    var numeric_part = $(this).attr('name').substr(12);
                    var sensorName = $("td[name='SensorName"+numeric_part+"']");
                    var sensorStatus = $("td[name='SensorStatus"+numeric_part+"']");
                    if(this.checked) {
                        //Do stuff
                        var request = $.post( "<%= request.getContextPath()%>/ShowSersor", { username: "<%= session.getAttribute("username")%>" } )
                        request.done(function(){
                                    sensorName.show();
                                    sensorStatus.show();
                                    console.log('ShowSensor Returned');
                                    errorMessage.hide();
                                }
                            );
                        request.fail(function(){
                                errorMessage.show();
                            }
                        );
                    }
                    else{
                        var request = $.post( "<%= request.getContextPath()%>/HideSensor", { username: "<%= session.getAttribute("username")%>" } )
                        request.done(function(){
                                    sensorName.hide();
                                    sensorStatus.hide();
                                    console.log('HideSensor Returned');
                                    errorMessage.hide();
                                }
                            );
                        request.fail(function(){
                                errorMessage.show();
                            }
                        );
                    }
                });
            })

        </script>

    </body>
</html>