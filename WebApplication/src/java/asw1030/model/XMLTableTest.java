/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import asw1030.beans.Sensor;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.xml.sax.SAXException;

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
    public static void main(String[] args) throws JAXBException, TransformerConfigurationException, ParserConfigurationException, IOException, FileNotFoundException, SAXException {
        IXMLTable<Sensor> table = XMLTable.getInstance("C:\\Users\\Thomas\\Desktop\\Test.txt");
        
        table.addRecord(new Sensor("TEST"));
    }
    
}
