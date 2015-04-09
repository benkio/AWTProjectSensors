/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import asw1030.libraries.xml.ManageXML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;

/**
 *
 * @author Farneti Thomas
 * @param <T>
 */
public class XMLTable<T> implements IXMLTable<T> {

    private static XMLTable instance= null;
    private static final Object locker= new Object();
    
    private final List<T> records;
   
    private JAXBContext context;
    private final ManageXML mngXML;
    /**
     *
     * @return
     * @throws javax.xml.bind.JAXBException
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public static XMLTable getInstance() throws JAXBException, TransformerConfigurationException, ParserConfigurationException{
        synchronized(locker){
            if (instance == null) {
                    instance = new XMLTable<>();
            }
            return instance;
        }
    }
    

    private XMLTable() throws JAXBException, TransformerConfigurationException, ParserConfigurationException {
        records = new ArrayList<>();
        mngXML= new ManageXML();
    }
    
    @Override
    public int addRecord(T item) {
        this.records.add(item);

        new Thread( () -> {
                try {
                    saveFile(records, item.getClass());
                } catch (JAXBException | ParserConfigurationException | TransformerException | IOException ex) {
                    Logger.getLogger(XMLTable.class.getName()).log(Level.SEVERE, null, ex);
                }
        }).start();
           
        return 0;
    }

    @Override
    public void removeRecord(int index) {
        
    }
    
    
    private void saveFile(List<?> list, Class<?> c) throws JAXBException, ParserConfigurationException, TransformerException, IOException {
        ListWrapper wrapper = new ListWrapper(list);

        context = JAXBContext.newInstance(ListWrapper.class, c);
        
        Marshaller marsh = context.createMarshaller();
        
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        marsh.marshal(wrapper, doc);
        mngXML.transform(System.out, doc);
//        OutputStream out = new FileOutputStream(userFile);
//        mngXML.transform(out, doc);
//        out.close();
    }
}
