/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.libraries.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author enricobenini
 */
public class ListWrapper {

    @XmlElementWrapper(name = "SensorList")
    public Map<String,Boolean> sensorList;
    
    public void setList(List<String> list) {
        list.stream().forEach(e -> this.sensorList.put(e, Boolean.TRUE));
    }
    public ListWrapper(){
        sensorList = new HashMap<>();
    }
}
