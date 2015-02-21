/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.services.actuators;

import it.unibo.aswProject.libraries.xml.ManageXML;
import it.unibo.aswProject.services.sensors.SensorsService;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Thomas
 */
@WebServlet(name = "ActuatorsService", urlPatterns = {"/actuators"})
public class ActuatorsService extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                response.setContentType("text/xml;charset=UTF-8");
        
        ManageXML mngXML = null;
        Document data= null;
        
        try(InputStream in = request.getInputStream()){
            mngXML = new ManageXML();
            data = mngXML.parse(in);
            
            operations(data,request.getSession(),mngXML,request,response);
            
        } catch (TransformerConfigurationException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(SensorsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(SensorsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void operations(Document data, HttpSession session, ManageXML mngXML, HttpServletRequest request, HttpServletResponse response) {
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        Document answer= null;
        
        switch (operation) {
        
        }
    }
}
