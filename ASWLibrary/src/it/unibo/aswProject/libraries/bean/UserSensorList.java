/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.bean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author enricobenini
 *
 * SEE http://www.dotnetperls.com/listmultimap
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class UserSensorList {

    public Map<String, List<String>> userSensor;

    public UserSensorList() {
        userSensor = new LinkedHashMap<>();
    }
}
