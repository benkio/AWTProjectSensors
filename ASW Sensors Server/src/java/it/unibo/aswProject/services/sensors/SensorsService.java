/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.services.sensors;

import it.unibo.aswProject.sensors.SensorManager;
import it.unibo.aswProject.sensors.Sensor;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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

    private HashMap<Integer, LinkedList<String>> subscriptions;
    private HashMap<String, Object> contexts = new HashMap<String, Object>();
    private Sensor sensor;

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.    
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
        }
    }

    private void operations(Document data, HttpSession session, ManageXML mngXML, HttpServletRequest request, HttpServletResponse response) throws IOException, TransformerException {
                //Name of operation is message root
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        Document answer= null;
        
        switch (operation) {
            case "login":
                System.out.println("Test Started");
                request.getSession(true).setAttribute("username", "test");
                synchronized (this) {
                    contexts.put("test", new LinkedList<Document>());
                }
                                
                answer = mngXML.newDocument("TestRecived");
                mngXML.transform(response.getOutputStream(), answer);
                response.getOutputStream().close();  
            break;
            
            case "Subscribe":
                System.out.println("Subscription Recived From: "+session.getAttribute("user"));
                mngXML.transform(response.getOutputStream(), mngXML.newDocument("SubscriptionRecived"));
                response.getOutputStream().close();  
            break;
            
                
            case "GetSensors":
                System.out.println("Get Sensors Recived From: "+session.getAttribute("username"));
                SensorManager sm = SensorManager.getInstance();
                
                Document doc= mngXML.newDocument("SensorsList");
                
                for (Sensor s : sm.getSensorList().values()) {
                    Element sensor = doc.createElement("Sensor");
                    sensor.setAttribute("id", Integer.toString(s.getNumber()));
                    sensor.appendChild(doc.createTextNode(s.getSensorState()));
                    doc.getDocumentElement().appendChild(sensor);
                }
                
                mngXML.transform(response.getOutputStream(), doc);
                response.getOutputStream().close();
            break;
                
            case "GetValues":
                System.out.println("GetValues Recived");
                String user = (String) session.getAttribute("username");
                
                if( !contexts.containsKey(user)){
                    synchronized(this){
                        contexts.put(user, new LinkedList<Document>());
                    }
                }
                
                boolean async;
                synchronized (this) {
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
                                    System.out.println("timeout event launched for: " + user);

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
                    } else {
                        answer = list.removeFirst();
                    }
                }
                if (!async) {
                    mngXML.transform(response.getOutputStream(), answer);
                    //os.close();
                }
            break;
                
            case "Notify":
                //System.out.println("Notify Recived From Sensor");
                
                synchronized (this) {
                    for (String destUser : contexts.keySet()) {
                        Object value = contexts.get(destUser);
                        if (value instanceof AsyncContext) {
                            OutputStream aos = ((AsyncContext) value).getResponse().getOutputStream();
                            mngXML.transform(aos, data);
                            mngXML.transform(System.out, data);
                            aos.close();
                            ((AsyncContext) value).complete();
                            contexts.put(destUser, new LinkedList<>());
                        } else {
                            System.out.println("linked");
                            ((LinkedList<Document>) value).addLast(data);
                        }
                    }
                }
                
                answer = mngXML.newDocument("Done");
                mngXML.transform(response.getOutputStream(), answer);
                response.getOutputStream().close();
                
            break;
        }
    }
}
