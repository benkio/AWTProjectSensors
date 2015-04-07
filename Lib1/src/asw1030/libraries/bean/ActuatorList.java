/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.libraries.bean;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thomas
 */
@XmlRootElement
public class ActuatorList {

    public List<Actuator> actuators;

    public ActuatorList() {
        actuators = new ArrayList<>();
    }
}
