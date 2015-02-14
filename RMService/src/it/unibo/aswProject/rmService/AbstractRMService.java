/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.rmService;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.w3c.dom.*;

import it.unibo.aswProject.http.*;
/**
 *
 * @author Thomas
 */
public abstract class AbstractRMService extends HttpServlet{
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InputStream is = request.getInputStream();
        response.setContentType("text/xml;charset=UTF-8");

        try {
            ManageXML mngXML = new ManageXML();
            Document data = mngXML.parse(is);
            is.close();

            operations(data, request, response, mngXML);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    protected abstract void operations(Document data, HttpServletRequest request, HttpServletResponse response, ManageXML mngXML);
}
