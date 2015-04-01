/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonServiceRequests;

import it.unibo.aswProject.libraries.http.HTTPClient;
import it.unibo.aswProject.libraries.xml.ManageXML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Enrico Benini
 */
public class SensorRequests {
    public NodeList getSensors(ManageXML mngXML, HTTPClient hc) throws Exception {
            // prepare the request xml
            Document data = mngXML.newDocument("getSensors");
            // do request
            Document answer = hc.execute("Sensors", data);
            // get response
            NodeList sensorsList = answer.getElementsByTagName("SensorsList");
            return sensorsList;
    }

    public void setSensorVisibility(String l, Boolean r) {
        /*
        *   TODO: CALL THE SERVICE OR SET THE VISIBILITY OF THE SENSOR BASED ON THE BOOLEAN INPUT, TRUE VISIBLE, FALSE INVISIBLE.
        */
    }
}
