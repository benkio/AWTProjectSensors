/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thomas
 * @param <T>
 */
@XmlRootElement(name="Table")
public class ListWrapper <T> {
     
    private List<T> items;

    public ListWrapper() {
        items = new ArrayList<T>();
    }

    public ListWrapper(List<T> items) {
        this.items = items;
    }

    @XmlAnyElement(lax=true)
    public List<T> getItems() {
        return items;
    }
 }
