/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.bean;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thomas
 */
    @XmlRootElement
    public class ActuatorList{
        public ArrayList<Actuator> actuatorList;
        
        public ActuatorList(){
            actuatorList= new ArrayList<>();
        }   
    }