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
package asw1030.beans;

/**
 *
 * @author Enrico Benini
 */
import java.util.HashMap;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A list of users
 */
@XmlRootElement
public class ActuatorList {

    public HashMap<Integer,Actuator> actuators;
    
    public int index;

    public ActuatorList() {
        this.actuators = new HashMap<>();
        
        index=0;
    }
}