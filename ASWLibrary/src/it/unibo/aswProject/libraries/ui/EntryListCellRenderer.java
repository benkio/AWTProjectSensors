/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author enricobenini
 */
public class EntryListCellRenderer extends JPanel implements ListCellRenderer {

    /**
     * Creates new form EntryListCellRenderer
     */
    public EntryListCellRenderer() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        sensorNameLabel = new JLabel();
        sensorStatusLabel = new JLabel();
        panelSensorStatusCaption = new JLabel();
        panelSensorNameCaption = new JLabel();

        setPreferredSize(new java.awt.Dimension(940, 100));

        sensorNameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        sensorNameLabel.setText("Sensor");

        sensorStatusLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        sensorStatusLabel.setText("Status");

        panelSensorStatusCaption.setText("Sensor Status:");

        panelSensorNameCaption.setText("Sensor Name");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(297, 297, 297)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(panelSensorNameCaption)
                .addGap(38, 38, 38)
                .addComponent(sensorNameLabel, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSensorStatusCaption)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sensorStatusLabel, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(sensorNameLabel, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                        .addComponent(sensorStatusLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelSensorStatusCaption)
                        .addComponent(panelSensorNameCaption)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }


    // Variables declaration
    private JLabel sensorNameLabel;
    private JLabel sensorStatusLabel;
    private JLabel panelSensorStatusCaption;
    private JLabel panelSensorNameCaption;
    // End of variables declaration

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        //TODO get actual object
        String sensorName = null;
        String sensorStatus = null;

        if (value instanceof Object[]) {
            Object values[] = (Object[]) value;
            sensorName = (String) values[0];
            sensorStatus = (String) values[1];
        }
        if (sensorName == null) {
            sensorName = "";
        }
        if (sensorStatus == null) {
            sensorStatus = "";
        }
        
        sensorNameLabel.setText(sensorName);
        sensorStatusLabel.setText(sensorStatus);

        if (isSelected) {
            adjustColors(list.getSelectionBackground(),
                    list.getSelectionForeground(), this, sensorNameLabel, sensorStatusLabel);
        } else {
            adjustColors(list.getBackground(),
                    list.getForeground(), this, sensorNameLabel, sensorStatusLabel);
        }
        return this;
    }

    private void adjustColors(Color bg, 
            Color fg,
            EntryListCellRenderer aThis, 
            JComponent... components) {
        for (JComponent c : components) {
            c.setForeground(fg);
            c.setBackground(bg);
        }
    }
}
