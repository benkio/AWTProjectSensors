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
package it.unibo.aswProject.servlet;

import CommonServiceRequests.SensorRequests;
import it.unibo.aswProject.libraries.http.HTTPClient;
import it.unibo.aswProject.libraries.http.HTTPClientFactory;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.w3c.dom.NodeList;

/**
 *
 * @author Enrico Benini
 */
@WebServlet(name = "UserAuthServlet", urlPatterns = {"/UserAuthServlet","/HideSensor","/ShowSersor"})
public class UserAuth extends HttpServlet {

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
                                                            request.getContextPath()));
            mngXML = new ManageXML();
            switch (request.getServletPath()) {
                case "/LoginServlet" : break;
                case "/HideSensor" : break;
                case "/ShowSersor" : break;
            }
            
        } catch (Exception ex) {
            /*
            * TODO: manage the exception
            */
        }
    }

    private String fetchSensors() throws Exception{
        NodeList sensors = sensorsRequests.getSensors(mngXML, hc);
        /*
        * TODO: build the string for the fetch se sensors
        */
        return "";
    
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
