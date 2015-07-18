/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.libraries.http;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *Classe statica che contiene il solo metodo per generare l'httpClient
 * 
 * @author Enrico Benini
 */
public class HTTPClientFactory {
    /**
     * Metodo che genera l'oggetto HttpClient 
     * 
     * @param sessionID sessione del client
     * @param documentBase uld di base
     * @return oggetto HttpClient
     * @throws MalformedURLException 
     */
    public static HTTPClient GetHttpClient(String sessionID, URL documentBase) throws MalformedURLException{
        HTTPClient hc = new HTTPClient();
        hc.setSessionId(sessionID);
        // represent the path portion of the URL as a file
        URL url = documentBase;
        File file = new File(url.getPath());
        // get the parent of the file
        String parentPath = file.getParent();
        // construct a new url with the parent path        
        URL parentUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), fixParentPath(parentPath));
        hc.setBase(parentUrl);
        return hc;
    }

    /**
     * Metodo che dato il path fisico lo corregge in conformita' con http
     * 
     * @param parentPath
     * @return contextPath corretto
     */
    public static String fixParentPath(String parentPath) {
        if (parentPath.contains("/jsp"))
            parentPath = parentPath.replace("/jsp", "");
        return parentPath.replace('\\','/') + "/";
    }
}
