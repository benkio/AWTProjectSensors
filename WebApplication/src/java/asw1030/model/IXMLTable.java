/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

/**
 *
 * @author Farneti Thomas
 */
public interface IXMLTable <T>{
    int addRecord(T item);
    
    void removeRecord(int index);
    
}
