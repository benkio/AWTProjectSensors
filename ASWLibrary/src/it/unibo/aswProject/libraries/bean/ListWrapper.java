/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author enricobenini
 */
public class ListWrapper {

    @XmlElementWrapper(name = "SensorList")
    public List<String> sensorlist;

    public void setList(List<String> list) {
        this.sensorlist = list;
    }
}
