/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.http;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Enrico Benini
 */
public class HTTPClientFactory {
    public static HTTPClient GetHttpClient(String sessionID, URL documentBase) throws MalformedURLException{
        HTTPClient hc = new HTTPClient();
        hc.setSessionId(sessionID);
        // represent the path portion of the URL as a file
        URL url = documentBase;
        File file = new File(url.getPath());
        // get the parent of the file
        String parentPath = file.getParent();
        // construct a new url with the parent path
        URL parentUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), parentPath.replace('\\','/') + "/");
        hc.setBase(parentUrl);
        return hc;
    }
}
