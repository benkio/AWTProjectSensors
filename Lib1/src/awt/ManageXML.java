/*
 * Copyright 2015 Enrico Benini.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package awt;

/**
 *
 * @author Enrico Benini
 */
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

    public Document newDocument() {
        return builder.newDocument();
    }

    public void transform(OutputStream out, Document document) throws TransformerException, IOException {
        transformer.transform(new DOMSource(document), new StreamResult(out));
    }

    public Document parse(InputStream in) throws IOException, SAXException {
        return builder.parse(in);
    }
}
