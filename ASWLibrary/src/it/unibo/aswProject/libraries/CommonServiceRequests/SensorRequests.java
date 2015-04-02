/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.CommonServiceRequests;

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
            return answer.getElementsByTagName("SensorsList");
    }

    public String getNewEvent(ManageXML mngXML, HTTPClient hc) throws Exception{
        Document data = mngXML.newDocument("waitEvents");
        Document answer = hc.execute("Sensors", data);
        mngXML.transform(System.out, answer);
        return answer.getElementsByTagName("message").item(0).getTextContent();
    } 
}
