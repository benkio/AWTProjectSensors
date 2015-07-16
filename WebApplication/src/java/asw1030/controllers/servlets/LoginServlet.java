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

import asw1030.beans.User;
import asw1030.dal.UserListFile;
import java.io.IOException;
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
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet", "/LogoutServlet"})
public class LoginServlet extends HttpServlet {

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
            if (request.getServletPath().equals("/LoginServlet")) {
                // setting user as logged in
                User user = new User();
                user.pass = request.getParameter("password");
                user.username = request.getParameter("username");
                UserListFile ulf = UserListFile.getInstance(getServletContext());
                user = ulf.loginUser(user);
                // setting user as logged in
                session.setAttribute("isLoggedIn", true);
                session.setAttribute("username", user.username);
                session.setAttribute("email", user.email);
                session.setAttribute("isAdmin", user.isAdmin);
            } else {
                // setting user as logged out
                session.removeAttribute("isLoggedIn");
                session.removeAttribute("username");
                session.removeAttribute("email");
            }
            response.sendRedirect( request.getContextPath() + "/index.jsp");
        } catch (Exception ex) {
            errorMsg = "Error Occurred: " + ex.getMessage();
            
            request.setAttribute("errorMsg", errorMsg);

            // forward request (along with its attributes) to the status JSP
            RequestDispatcher rd = request.getRequestDispatcher("/jsp/login.jsp");
            rd.forward(request, response);
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

}
