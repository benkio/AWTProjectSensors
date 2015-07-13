package asw1030.controllers.sensors;

import asw1030.beans.Sensor;
import asw1030.beans.Sensor.SensorKind;
import asw1030.libraries.xml.ManageXML;
import asw1030.model.IModelEventsListener;
import asw1030.dal.IXMLTable;
import asw1030.model.ModelEventType;
import asw1030.model.SensorModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author Thomas Farneti
 */
@WebServlet(name = "SensorsService", urlPatterns = {"/Sensors"},asyncSupported = true)
public class SensorsService extends HttpServlet{

    private LinkedList<AsyncContext> contexts;
    private SensorModel sm;
    
    @Override
    public void init() throws ServletException {
        super.init();
        contexts= new LinkedList<>();      
        sm = SensorModel.getInstance( this.getServletContext());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        response.setContentType("text/xml;charset=UTF-8");
        
        ManageXML mngXML = null;
        Document data= null;
        
        try(InputStream in = request.getInputStream()){
            mngXML = new ManageXML();
            data = mngXML.parse(in);
            
            operations(data,request.getSession(),mngXML,request,response);
            
        } catch(Exception ex){
            Logger.getLogger(SensorsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void operations(Document data, HttpSession session, ManageXML mngXML, HttpServletRequest request, HttpServletResponse response) throws IOException, TransformerException, Exception {
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        
        Document answer= null;
        String user = (String) session.getAttribute("username");
        Boolean logged = (Boolean) session.getAttribute("isLoggedIn");
        
        if(logged== null || !logged){ 
            switch (operation) {
                case "testLogin":
                    request.getSession(true).setAttribute("username", "test");
                    request.getSession().setAttribute("isLoggedIn", true);
                    request.getSession().setAttribute("isAdmin", true);
                    sendMessage("Creata sessione test", mngXML, response);
                break;
                    
                default:
                    sendErrorMsg("Errore login", "Utente non autenticato", response, mngXML);
                break;
            }
        }else{
            switch (operation) {
                case "getSensors":
                    sendSensors(mngXML,response,user);
                    break;
                case "waitEvents":
                    waitEvents(mngXML,response,request);
                    break;
                case "addSensor":
                    addSensor(mngXML,response,request,data);
                    break;
                case "removeSensor":
                    removeSensor(mngXML,response,request,data);
                    break;
                case "enableSensor":
                    enableSensor(mngXML,response,request);
                    break;
                case "disableSensor":
                    disableSensor(mngXML,response,request);
                    break;
                default:
                    sendErrorMsg("Errore", "Operazione non supportata", response, mngXML);
                    break;
            }
        }
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

    private void sendSensors(ManageXML mngXML, HttpServletResponse response, String username) throws Exception {
        System.out.println("Get Sensors Recived");

        Document doc= mngXML.newDocument("SensorsList");
//        User tempUser = new User();
//        tempUser.username = username;
//        Map<String,Boolean> sensorList = uslf.getSensorIdsByUser(tempUser);
//        for (Map.Entry<String, Boolean> s : sensorList.entrySet()){
//            Sensor sensor = slf.getSensorByName(s.getKey());
//            Element sensorXml = doc.createElement("Sensor");
//            sensorXml.setAttribute("id", sensor.Name);
//            sensorXml.setAttribute("value", Integer.toString(sensor.Value));
//            sensorXml.setAttribute("visible", s.getValue().toString());
//            sensorXml.appendChild(doc.createTextNode(sensor.Status.name()));
//            doc.getDocumentElement().appendChild(sensorXml);
//        }

        mngXML.transform(response.getOutputStream(), doc);
        response.getOutputStream().close();
    }

    private synchronized void waitEvents(ManageXML mngXML, HttpServletResponse response, HttpServletRequest request) {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(10 * 1000);
        asyncContext.addListener(new AsyncListener() {

            @Override
            public void onComplete(AsyncEvent event) throws IOException {
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                AsyncContext asyncContext = event.getAsyncContext();
                    boolean confirm;
                    synchronized (this) {
                        if ((confirm = contexts.contains(asyncContext))) {
                            contexts.remove(asyncContext);
                        }
                    }
                    if (confirm) {
                        try {
                            sendMessage("Timeout", mngXML, (HttpServletResponse)asyncContext.getResponse());
                        } catch (TransformerException ex) {
                            Logger.getLogger(SensorsService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        asyncContext.complete();
                    }
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        contexts.add(asyncContext);
    }
    
    private void addSensor(ManageXML mngXML, HttpServletResponse response, HttpServletRequest request, Document data) throws IOException, TransformerException {
        if ((boolean) request.getSession().getAttribute("isAdmin")) {
            
            mngXML.transform(System.out, data);
            Element root = data.getDocumentElement();
            NodeList childs = root.getElementsByTagName("sensor");
            
            if (childs.getLength() !=0) {
                Element sensor = (Element) childs.item(0);
                Text kind =(Text) sensor.getChildNodes().item(0).getFirstChild();
                Text status =(Text) sensor.getChildNodes().item(1).getFirstChild();
                
                Sensor s = new Sensor(SensorKind.valueOf(kind.getNodeValue()));
                
                int id = sm.addSensor(s);
                
                sendMessage(""+id, mngXML, response);
            }
        }
    }

    private void removeSensor(ManageXML mngXML, HttpServletResponse response, HttpServletRequest request, Document data) throws IOException, TransformerException {
        if ((boolean) request.getSession().getAttribute("isAdmin")) {
            Element root = data.getDocumentElement();
            NodeList childs = root.getElementsByTagName("sensorId");
            mngXML.transform(System.out, data);
            
            if (childs.getLength() !=0) {
                Element sensor = (Element) childs.item(0);
                Text textNode = (Text) sensor.getFirstChild();
                
                sm.removeSensor(Integer.parseInt(textNode.getData()));
                
                sendMessage("done", mngXML, response);
            }
        }
    }

    private void enableSensor(ManageXML mngXML, HttpServletResponse response, HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void disableSensor(ManageXML mngXML, HttpServletResponse response, HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
//    @Override
//    public void newEvent(SensorEventType se) {
//        System.out.println("New Event");
//        
//        synchronized (this) {
//            contexts.stream().forEach((asyncContext) -> {
//                try {
//                    ManageXML mngXML = new ManageXML();
//                    sendMessage("NewEvent", mngXML, (HttpServletResponse)asyncContext.getResponse());
//                    asyncContext.complete();
//                } catch (ParserConfigurationException | IOException | TransformerException ex) {
//                    Logger.getLogger(SensorsService.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//            contexts.clear();
//        }
//    }
}
