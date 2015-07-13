/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.dal;

import asw1030.libraries.xml.ManageXML;
import asw1030.beans.interfaces.IXmlRecord;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Class to model a XML table
 * @author Farneti Thomas
 * @param <T> Type of the objects stored as records of the table
 */
@XmlRootElement(name="xmlTable")
public class XMLTable<T> implements IXMLTable<T> {

    @XmlTransient
    private static XMLTable instance= null;
    @XmlTransient
    private static final Object locker= new Object();
    
    @XmlElement(name = "records")
    private final List<T> records;
    
    @XmlTransient
    private JAXBContext context;
    @XmlTransient
    private final ManageXML mngXML;
    
    @XmlTransient
    private File db;
    
    @XmlElement(name = "prog")
    private int idProg;
    
    /**
     *
     * @param dbPath
     * @return
     * @throws javax.xml.bind.JAXBException
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public static XMLTable getInstance(String dbPath) throws JAXBException, TransformerConfigurationException, ParserConfigurationException, IOException, FileNotFoundException, SAXException{
        synchronized(locker){
            if (instance == null) {
                    instance = new XMLTable<>(dbPath);
            }
            return instance;
        }
    }
    
    private XMLTable() throws TransformerConfigurationException, ParserConfigurationException{
        records = new ArrayList<T>();
        mngXML= new ManageXML();
        idProg=0;
    }
    
    private XMLTable(String dbPath) throws JAXBException, TransformerConfigurationException, ParserConfigurationException, FileNotFoundException, IOException, SAXException {
        this();
        
        db = new File(dbPath);
        
        if(db.exists()){
            try (InputStream in = new FileInputStream(db)) {
                Document dbDoc = mngXML.parse(in);
                context = JAXBContext.newInstance(this.getClass());
                Unmarshaller u = context.createUnmarshaller();
                XMLTable table = (XMLTable) u.unmarshal(dbDoc);
                this.records.addAll(table.getRecords());
                this.idProg= table.getIdProg();
            }
        }
    }
    
    @Override
    public synchronized int addRecord(T item) {
        ((IXmlRecord)item).setId(idProg);
        idProg+=1;
        
        this.records.add(item);
        save(item.getClass());
               
        return idProg;
    }

    @Override
    public void removeRecord(int index) {
        records.forEach((T record) -> {
            if(((IXmlRecord)record).getId()==index) records.remove(record);
        });
    }
 
    private void save(Class<?> c) {
        new Thread( () -> {        
            try {
                context = JAXBContext.newInstance(this.getClass(),c);

                Marshaller marsh = context.createMarshaller();

                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                marsh.marshal(this, doc);
                mngXML.transformIndented(System.out, doc);
                try (OutputStream out = new FileOutputStream(db)) {
                    mngXML.transformIndented(out, doc);
                }
                
            } catch (JAXBException | ParserConfigurationException | TransformerException | IOException ex) {
                Logger.getLogger(XMLTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    public List<T> getRecords() {
        return records;
    }

    public int getIdProg() {
        return idProg;
    }
   
    @Override
    public HashMap<Integer, T> fetchRecords() {
        HashMap<Integer,T> rec= new HashMap<Integer, T>();
        
        this.records.forEach(r-> rec.put(((IXmlRecord)r).getId(), r));
        
        return rec;
    }
}