/*
 * Copyright 2015 Enrico Benini.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asw1030.controllers.servlets;

import asw1030.libraries.commonServiceRequests.SensorRequests;
import asw1030.libraries.http.HTTPClient;
import asw1030.libraries.http.HTTPClientFactory;
import asw1030.libraries.xml.ManageXML;
import java.io.IOException;
import java.net.URL;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Enrico Benini
 */
@WebServlet(name = "UserAuthServlet", urlPatterns = {"/UserAuthServlet"})
public class UserAuthServlet extends HttpServlet {

    private ManageXML mngXML;
    private HTTPClient hc;
    private SensorRequests sensorsRequests = new SensorRequests();
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String errorMsg = "";

        try {
            HttpSession session = request.getSession();
            hc = HTTPClientFactory.GetHttpClient(   session.getId(), 
                                                    new URL(request.getScheme(), 
                                                            request.getServerName(), 
                                                            request.getServerPort(), 
                                                            request.getContextPath()+request.getServletPath()));
            mngXML = new ManageXML();
            request.setAttribute("sensorList",fetchSensors());
            RequestDispatcher rd = request.getRequestDispatcher("/jsp/userAuth.jsp");
            rd.forward(request, response);
            
        } catch (Exception ex) {
            errorMsg = ex.getMessage();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String fetchSensors() throws Exception{
        NodeList sensors = sensorsRequests.getSensors(mngXML, hc).item(0).getChildNodes();
        String sensorsHTML = "";
        for (int i = 0; i < sensors.getLength();i++){
            Node sensor = sensors.item(i);
            String sensor_name = sensor.getAttributes().getNamedItem("id").getNodeValue();
            String sensor_state = sensor.getTextContent();
            String sensor_checked = Boolean.valueOf(sensor.getAttributes().getNamedItem("visible").getNodeValue()) ? "checked=\"checked\"" : "";
            sensorsHTML += "<tr><td name=\"SensorName"+i+"\">"+sensor_name+"</td><td name=\"SensorStatus"+i+"\">"+sensor_state+"</td><td><input type=\"checkbox\" name=\"SensorEnable"+i+"\" value=\"ON\" "+sensor_checked+" /></td></tr>";
        }
         
        return sensorsHTML;
    
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}