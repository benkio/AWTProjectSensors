package asw1030.libraries.xml;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ManageXML {

    private Transformer transformer;
    private DocumentBuilder builder;
    
    public ManageXML() throws TransformerConfigurationException, ParserConfigurationException {
        transformer = TransformerFactory.newInstance().newTransformer();
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();        
    }

    public Document newDocument(String rootTag) {
        Document newDoc = builder.newDocument(); 
        newDoc.appendChild(newDoc.createElement(rootTag));
        return newDoc;
    }

    public void transform(OutputStream out, Document document) throws TransformerException, IOException {
        transformer.transform(new DOMSource(document), new StreamResult(out));
    }

    public void transformIndented(OutputStream out, Document document) throws TransformerException, IOException {
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(out));
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
    }
    
    public Document parse(InputStream in) throws IOException, SAXException {
        return builder.parse(in);
    }
}
