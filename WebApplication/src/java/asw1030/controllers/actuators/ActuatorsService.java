/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.controllers.actuators;

import asw1030.libraries.xml.ManageXML;
import asw1030.controllers.sensors.SensorsService;
import asw1030.model.ActuatorModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
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

    private ActuatorModel am;
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        try {
            am = ActuatorModel.getInstance(getServletContext());
        } catch (Exception ex) {
            Logger.getLogger(ActuatorsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
        } catch (Exception ex) {
            Logger.getLogger(ActuatorsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void operations(Document data, HttpSession session, ManageXML mngXML, HttpServletRequest request, HttpServletResponse response) throws JAXBException, IOException, TransformerException, ParserConfigurationException, Exception {
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        Document answer= null;
        
        switch (operation) {
            case"getActuators":
                sendActuators(mngXML,response);
                break;  
            case "setValue":
                setActuatorValue(mngXML,response,data);
                break;
            case "addActuator":
                
                break;
            case "removeActuator":
                break;
        }
    }

    private void sendActuators(ManageXML mngXML, HttpServletResponse response) throws IOException, TransformerException {
        System.out.println("Get Actuators Recived");

        Document doc= mngXML.newDocument("actuatorsList");
        
        am.getActuators().stream().forEach(a->{
            Element actuator= doc.createElement("actuator");
            
            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(""+a.getId()));
            actuator.appendChild(id);
            
//            Element kind = doc.createElement("kind");
//            kind.appendChild(doc.createTextNode(s.getKind().toString()));
//            sensor.appendChild(kind);
            
            Element value = doc.createElement("value");
            value.appendChild(doc.createTextNode(""+a.getValue()));
            actuator.appendChild(value);
            
            doc.getDocumentElement().appendChild(actuator);
        });

        mngXML.transform(response.getOutputStream(), doc);
        response.getOutputStream().close();
    }

    private void setActuatorValue(ManageXML mngXML, HttpServletResponse response, Document data) throws IOException, TransformerException {
        Element idEl = (Element) data.getElementsByTagName("id").item(0);
        Element valEl = (Element) data.getElementsByTagName("value").item(0);

        am.setActuatorValue(Integer.parseInt(idEl.getFirstChild().toString()), Integer.parseInt(valEl.getFirstChild().toString()));

        sendMessage("done", mngXML, response);
        response.getOutputStream().close();
    }
    
    private void sendErrorMsg(String caption, String message, HttpServletResponse response, ManageXML mngXML) throws IOException, TransformerException {
        Document answer = mngXML.newDocument("error");
        Element cpt = answer.createElement("caption");
        cpt.appendChild(answer.createTextNode(caption));
        
        Element msg = answer.createElement("message");
        msg.appendChild(answer.createTextNode(message));
        
        answer.getDocumentElement().appendChild(cpt);
        answer.getDocumentElement().appendChild(msg);
        
        mngXML.transform(response.getOutputStream(), answer);
        response.getOutputStream().close();
    }

    private void sendMessage(String msg, ManageXML mngXML, HttpServletResponse response) throws IOException, TransformerException {
        Document answer = mngXML.newDocument("message");
        answer.getDocumentElement().appendChild(answer.createTextNode(msg));
        mngXML.transform(response.getOutputStream(), answer);
        response.getOutputStream().close(); 
    }
}
