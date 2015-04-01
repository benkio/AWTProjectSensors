/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.servlet;

import CommonServiceRequests.SensorRequests;
import it.unibo.aswProject.libraries.http.HTTPClient;
import it.unibo.aswProject.libraries.http.HTTPClientFactory;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Enrico Benini
 */
@WebServlet(name = "UserSensorsServlet", urlPatterns = {"/UserSensorsServlet"})
public class UserSensorsServlet extends HttpServlet {

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
            List<String> sensorNames = Arrays.asList(request.getParameter("names").split("\\s*,\\s*"));
            List<String> sensorEnables = Arrays.asList(request.getParameter("enabled").split("\\s*,\\s*"));
            List<Pair<String,Boolean>> parameters = castParameters(sensorNames, sensorEnables);
            parameters.stream().forEach(param -> {
                sensorsRequests.setSensorVisibility(param.l,param.r);
            });
            response.sendRedirect(request.getContextPath() + "/UserAuthServlet");
            
            
        } catch (Exception ex) {
            errorMsg = ex.getMessage();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
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


    private List<Pair<String, Boolean>> castParameters(List<String> sensorNames, List<String> sensorEnables) throws Exception {
        List<Pair<String,Boolean>> parameters;
        parameters = new ArrayList<>();
        if (sensorNames.size() == sensorEnables.size()){
            for (int i = 0; i< sensorNames.size(); i++){
                    Pair<String,Boolean> pair = new Pair<>(sensorNames.get(i), Boolean.valueOf(sensorEnables.get(i)));
                    parameters.add(pair);
            }
            return parameters;
         }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class Pair<L, R> {

        private L l;
        private R r;

        public Pair(L l, R r) {
            this.l = l;
            this.r = r;
        }

        public L getL() {
            return l;
        }

        public R getR() {
            return r;
        }

        public void setL(L l) {
            this.l = l;
        }

        public void setR(R r) {
            this.r = r;
        }
    }
}
