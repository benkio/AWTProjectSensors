/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.libraries.commonServiceRequests;

import asw1030.libraries.http.HTTPClient;
import asw1030.libraries.xml.ManageXML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * Richieste comuni riguardanti i sensori
 * 
 * @author Enrico Benini
 */
public class SensorRequests {
    /**
     * Esegue la richiesta di nuovi sensori e restituisce il risultato
     * 
     * @param mngXML libreria per la gestione degli XML
     * @param hc HTTPClient utilizzato per effettuare la richiesta
     * @return ritorna la lista dei nodi parsati della risposta
     * @throws Exception 
     */
    public NodeList getSensors(ManageXML mngXML, HTTPClient hc) throws Exception {
            // prepare the request xml
            Document data = mngXML.newDocument("getSensors");
            // do request
            Document answer = hc.execute("Sensors", data);
            // get response
            mngXML.transform(System.out, answer);
            return answer.getElementsByTagName("SensorsList");
    }

    /**
     * Esegue la richiesta per le notifiche comet e restituisce il risultato
     * 
     * @param mngXML libreria per la gestione degli XML
     * @param hc HTTPClient utilizzato per effettuare la richiesta
     * @return ritorna la lista dei nodi parsati della risposta
     * @throws Exception 
     */
    public Document getNewEvent(ManageXML mngXML, HTTPClient hc) throws Exception{
        Document data = mngXML.newDocument("waitEvents");
        Document answer = hc.execute("Sensors", data);
        mngXML.transform(System.out, answer);
        return answer;
    } 
}
