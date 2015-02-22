/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.services.actuators;

import it.unibo.aswProject.libraries.xml.ManageXML;
import it.unibo.aswProject.model.actuators.Actuator;
import it.unibo.aswProject.model.actuators.ActuatorList;
import it.unibo.aswProject.model.actuators.ActuatorsManager;
import it.unibo.aswProject.services.sensors.SensorsService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilderFactory;
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
        } catch (JAXBException ex) {
            Logger.getLogger(ActuatorsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void operations(Document data, HttpSession session, ManageXML mngXML, HttpServletRequest request, HttpServletResponse response) throws JAXBException, IOException, TransformerException, ParserConfigurationException {
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        Document answer= null;
        
        switch (operation) {
            case"getActuators":
                ActuatorsManager am = ActuatorsManager.getInstance();
                am.addActuator(new Actuator(1, 1, null));
                am.addActuator(new Actuator(2, 2, null));
                
                if(!am.getList().isEmpty())
                {
                    JAXBContext jc = JAXBContext.newInstance(ActuatorList.class);
                    Marshaller marsh = jc.createMarshaller();

                    try (OutputStream os = response.getOutputStream()) {
                            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                            ActuatorList al = new ActuatorList();
                            al.setList(am.getList());
                            marsh.marshal(al, doc);
                            mngXML.transform(os, doc);
                            mngXML.transform(System.out, doc);
                    }

                }
                else{
                    //TODO: No actuators
                }

                break;
            case "setValue":
                //int id= root.getElementsByTagName("id");
                break;
        }
    }
}
