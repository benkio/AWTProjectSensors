/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.actuators;

import java.util.ArrayList;
import java.util.LinkedList;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thomas
 */
    @XmlRootElement
    public class ActuatorList{
        private ArrayList<Actuator> actuatorList;
        
        public ActuatorList(){
            actuatorList= new ArrayList<>();
        }

        public ArrayList<Actuator> getList() {
            return actuatorList;
        }

        public void setList(ArrayList<Actuator> list) {
            this.actuatorList = list;
        }        
    }
