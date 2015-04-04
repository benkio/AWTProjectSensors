/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.bean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author enricobenini
 * 
 * SEE http://www.dotnetperls.com/listmultimap
 */
public class UserSensorList {
    public HashMap<String, List<String>> userSensor;
    
    public UserSensorList(){
        userSensor = new LinkedHashMap<>();
    }
}
