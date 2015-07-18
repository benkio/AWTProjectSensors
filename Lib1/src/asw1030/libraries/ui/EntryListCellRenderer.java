/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.libraries.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author enricobenini
 */
public class EntryListCellRenderer extends javax.swing.JPanel implements ListCellRenderer {

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

        panelSensorNameCaption = new javax.swing.JLabel();
        sensorNameLabel = new javax.swing.JLabel();
        panelSensorStatusCaption = new javax.swing.JLabel();
        sensorStatusLabel = new javax.swing.JLabel();
        sensorValueProgressBar = new javax.swing.JProgressBar();
        sensorValueLabel = new javax.swing.JLabel();
        panelSensorValueCaption = new javax.swing.JLabel();
        panelSensorTypeLabel = new javax.swing.JLabel();
        sensorTypeLabel = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(940, 100));

        panelSensorNameCaption.setText("Sensor Name: ");

        sensorNameLabel.setFont(new java.awt.Font("Ubuntu", 0, 24));
        sensorNameLabel.setText("Sensor");

        panelSensorStatusCaption.setText("Sensor Status:");

        sensorStatusLabel.setFont(new java.awt.Font("Ubuntu", 0, 24));
        sensorStatusLabel.setText("Status");

        sensorValueLabel.setText("0%");

        panelSensorValueCaption.setText("Sensor Value:");

        panelSensorTypeLabel.setText("Sensor Type:");

        sensorTypeLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        sensorTypeLabel.setText("Type");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelSensorNameCaption, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sensorNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSensorStatusCaption, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelSensorValueCaption, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sensorStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelSensorTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sensorTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sensorValueProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(sensorValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(panelSensorStatusCaption, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(sensorStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(panelSensorTypeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sensorTypeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sensorValueProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                            .addComponent(sensorValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelSensorValueCaption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(sensorNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSensorNameCaption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }


    @Override
       public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        //TODO get actual object
        String sensorName = null;
        String sensorStatus = null;
        String sensorValue = null;
        String sensorType = null;
        int sensorValueInt = 0;

        if (value instanceof Object[]) {
            Object values[] = (Object[]) value;
            sensorName = (String) values[0];
            sensorStatus = (String) values[1];
            sensorValue = (String) values[2];
            sensorType = (String) values[3];
        }
        if (sensorName == null) {
            sensorName = "";
        }
        if (sensorStatus == null) {
            sensorStatus = "";
        }
        if (sensorType == null)
            sensorType = "";
        if (sensorValue == null) {
            sensorValue = "";
        }
        else sensorValueInt = Integer.parseInt(sensorValue);
        
        sensorNameLabel.setText(sensorName);
        sensorStatusLabel.setText(sensorStatus);
        sensorValueLabel.setText(sensorValue+"%");
        sensorValueProgressBar.setValue(sensorValueInt);
        sensorTypeLabel.setText(sensorType);

        if (isSelected) {
            adjustColors(list.getSelectionBackground(),
                    list.getSelectionForeground(), this, panelSensorNameCaption, sensorStatusLabel, sensorValueLabel);
        } else {
            adjustColors(list.getBackground(),
                    list.getForeground(), this, panelSensorNameCaption, sensorStatusLabel, sensorValueLabel);
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

    private javax.swing.JLabel panelSensorNameCaption;
    private javax.swing.JLabel panelSensorStatusCaption;
    private javax.swing.JLabel panelSensorTypeLabel;
    private javax.swing.JLabel panelSensorValueCaption;
    private javax.swing.JLabel sensorNameLabel;
    private javax.swing.JLabel sensorStatusLabel;
    private javax.swing.JLabel sensorTypeLabel;
    private javax.swing.JLabel sensorValueLabel;
    private javax.swing.JProgressBar sensorValueProgressBar;
}
