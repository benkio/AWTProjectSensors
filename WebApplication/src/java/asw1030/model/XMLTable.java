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
 * Class to model a XML table
 * @author Farneti Thomas
 * @param <T> Type of the objects stored as records of the table
 */
public class XMLTable<T> implements IXMLTable<T> {

    private static XMLTable instance= null;
    private static final Object locker= new Object();
    
    private final List<T> records;
    
    private final List<IModelEventListener> listeners;
   
    private JAXBContext context;
    private final ManageXML mngXML;
    /**
     *
     * @return
     * @throws javax.xml.bind.JAXBException
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public static XMLTable getInstance(String dbPath) throws JAXBException, TransformerConfigurationException, ParserConfigurationException{
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
        listeners = new ArrayList<>();
    }
    
    @Override
    public int addRecord(T item) {
        this.records.add(item);
        saveFile(records, item.getClass());
        
        for (IModelEventListener list : listeners) {
            list.modelEventHandler(ModelEventType.NEWRECORD, item);
        }
        
        return 0;
    }

    @Override
    public void removeRecord(int index) {
        
    }
    
    
    private void saveFile(List<?> list, Class<?> c) {
        new Thread( () -> {        
            try {
                ListWrapper wrapper = new ListWrapper(list);

                context = JAXBContext.newInstance(ListWrapper.class, c);

                Marshaller marsh = context.createMarshaller();

                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                marsh.marshal(wrapper, doc);
                mngXML.transform(System.out, doc);
        //        OutputStream out = new FileOutputStream(userFile);
        //        mngXML.transform(out, doc);
        //        out.close();
            } catch (JAXBException | ParserConfigurationException | TransformerException | IOException ex) {
                Logger.getLogger(XMLTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    @Override
    public void addListener(IModelEventListener list) {
        this.listeners.add(list);
    }

    @Override
    public void removeListener(IModelEventListener list) {
        this.listeners.remove(list);
    }
}
