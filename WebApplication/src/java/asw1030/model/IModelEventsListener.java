/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import asw1030.beans.enums.ModelEventType;

/**
 * Interfaccia per i listener del model
 * @author Farneti Thomas
 */
public interface IModelEventsListener {
    void modelEventHandler(ModelEventType type, Object arg);
}
