/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import asw1030.libraries.bean.Sensor;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

/**
 *
 * @author Thomas
 */
public class XMLTableTest {

    /**
     * @param args the command line arguments
     * @throws javax.xml.bind.JAXBException
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public static void main(String[] args) throws JAXBException, TransformerConfigurationException, ParserConfigurationException {
        IXMLTable<Sensor> table = XMLTable.getInstance();
        
        table.addRecord(new Sensor("test"));
    }
    
}
