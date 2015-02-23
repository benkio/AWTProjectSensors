/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.services.sensors;

import it.unibo.aswProject.model.sensors.SensorManager;
import it.unibo.aswProject.model.sensors.Sensor;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
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
 * @author Thomas Farneti
 */
@WebServlet(name = "SensorsService", urlPatterns = {"/Sensors"},asyncSupported = true)
public class SensorsService extends HttpServlet{

    private HashMap<String, Object> contexts = new HashMap<String, Object>();
    private LinkedList<String> subUsers= new LinkedList<>();
    
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

    private void operations(Document data, HttpSession session, ManageXML mngXML, HttpServletRequest request, HttpServletResponse response) throws IOException, TransformerException {
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        Document answer= null;
        String user = (String) session.getAttribute("username");
        
        String logged = (String) session.getAttribute("isLoggedIn");
        if(logged==null || !logged.equals("true")){
            answer = mngXML.newDocument("notLogged");
            mngXML.transform(response.getOutputStream(), answer);
            response.getOutputStream().close();
            return;
        }
        
        
        switch (operation) {
            case "login":
                System.out.println("Test Started");
                request.getSession(true).setAttribute("username", "test");                                
                answer = mngXML.newDocument("TestRecived");
                mngXML.transform(response.getOutputStream(), answer);
                response.getOutputStream().close();  
            break;
            
            case "subscribe":
                System.out.println("Subscription Recived From: "+user);
                
                synchronized (this){
                    if(!subUsers.contains(user)){
                        if(subUsers.isEmpty()){
                            subUsers.add(user);
                            SensorManager.getInstance().startnotifications();
                        }else{
                            subUsers.add(user);
                        }

                        contexts.put(user, new LinkedList<>());
                    }

                    mngXML.transform(response.getOutputStream(), mngXML.newDocument("subscribed"));
                    response.getOutputStream().close();  
                }
                break;
                
            case "unsubscribe":
                System.out.println("Unsubscription Recived From: "+user);
                
                synchronized (this){

                    if(subUsers.contains(user)){
                        subUsers.remove(user);

                        if(subUsers.isEmpty()){
                            SensorManager.getInstance().stopnotifications();
                        }

                        if(contexts.containsKey(user))
                            contexts.remove(user);

                    }

                    mngXML.transform(response.getOutputStream(), mngXML.newDocument("unsubscribed"));
                    response.getOutputStream().close();  
                }
                break;
                
            case "GetSensors":
                System.out.println("Get Sensors Recived From: "+session.getAttribute("username"));
                SensorManager sm = SensorManager.getInstance();
                
                Document doc= mngXML.newDocument("SensorsList");
                
                for (Sensor s : sm.getSensorList().values()) {
                    Element sensor = doc.createElement("Sensor");
                    sensor.setAttribute("id", Integer.toString(s.getNumber()));
                    sensor.setAttribute("value", Integer.toString(s.getValue()));
                    sensor.appendChild(doc.createTextNode(s.getSensorState()));
                    doc.getDocumentElement().appendChild(sensor);
                }
                              
                mngXML.transform(response.getOutputStream(), doc);
                response.getOutputStream().close();
            break;
                
            case "GetValues":
                System.out.println("GetValues Recived from: "+user);

                boolean async;
                synchronized (this) {
                    if(subUsers.contains(user)){
                        
                        if(contexts.get(user) instanceof AsyncContext){
                            //((AsyncContext)contexts.get(user)).complete();
                            contexts.put(user, new LinkedList<Document>());
                        }
                        
                        LinkedList<Document> list = (LinkedList<Document>) contexts.get(user);
                        if (async = list.isEmpty()) {
                            AsyncContext asyncContext = request.startAsync();
                            asyncContext.setTimeout(10 * 1000);
                            asyncContext.addListener(new AsyncAdapter() {
                                @Override
                                public void onTimeout(AsyncEvent e) {
                                    try {
                                        ManageXML mngXML = new ManageXML();

                                        AsyncContext asyncContext = e.getAsyncContext();
                                        HttpServletRequest reqAsync = (HttpServletRequest) asyncContext.getRequest();
                                        String user = (String) reqAsync.getSession().getAttribute("username");
                                        System.out.println(user+ " Async req Timeouted");

                                        Document answer = mngXML.newDocument("timeout");
                                        boolean confirm;
                                        synchronized (SensorsService.this) {
                                            if (confirm = (contexts.get(user) instanceof AsyncContext)) {
                                                contexts.put(user, new LinkedList<>());
                                            }
                                        }
                                        if (confirm) {
                                            try (OutputStream tos = asyncContext.getResponse().getOutputStream()) {
                                                mngXML.transform(tos, answer);
                                            }
                                            asyncContext.complete();
                                        }
                                    } catch (ParserConfigurationException | IOException | TransformerException ex) {
                                        System.out.println(ex);
                                    }
                                }
                            });
                            contexts.put(user, asyncContext);
                            System.out.println(user + " Async req Started");
                        } else {
                            answer = list.removeFirst();
                        }                    
                        if (!async) {
                            mngXML.transform(response.getOutputStream(), answer);
                            response.getOutputStream().close();
                            System.out.println(user + " Message Sended");
                        }
                    }
                    else{
                        mngXML.transform(response.getOutputStream(), mngXML.newDocument("userNotSubscribed"));
                        response.getOutputStream().close(); 
                    }
                }
            break;
                
            case "Notify":
                
                synchronized (this) {
                    
                    if(subUsers.isEmpty()){
                        answer = mngXML.newDocument("noUsers");
                        mngXML.transform(response.getOutputStream(), answer);
                        response.getOutputStream().close();
                    }
                    else{ 
                        for (String destUser : subUsers) {
                            Object value = contexts.get(destUser);
                            if (value instanceof AsyncContext) {
                                try (OutputStream aos = ((AsyncContext) value).getResponse().getOutputStream()) {
                                    mngXML.transform(aos, data);
                                }
                                ((AsyncContext) value).complete();
                                contexts.put(destUser, new LinkedList<>());
                                System.out.println(destUser + " Async Req Ended");
                            } else {
                                
                                LinkedList<Document> list = ((LinkedList<Document>) value);
                                
                                if(list.size()>1000){
                                    list.clear();
                                }
                                list.addLast(data);
                                System.out.println(destUser + " message appended");
                            }
                        }
                        
                        answer = mngXML.newDocument("Done");
                        mngXML.transform(response.getOutputStream(), answer);
                        response.getOutputStream().close();
                    }
                }    
                break;
        }
    }
}
