/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package awt.applet;

import awt.HTTPClient;
import awt.ManageXML;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.w3c.dom.*;

/**
 *
 * @author enricobenini
 */
public class SensorsControlPanel extends JApplet {

    private ManageXML mngXML;
    private HTTPClient hc;
    private AppletGUI appletGUI;
    private int CurrentSensorIDSelected;

    @Override
    public void init() {
        try {
            initHTTPClient();
            CurrentSensorIDSelected = -1;
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    appletGUI = new AppletGUI();
                    appletGUI.fillContainer(getContentPane());
                }
            });

            new SensorDownloadWorker().execute();
        } catch (Exception e) {
            System.err.println("createGUI non eseguito con successo");
            Logger.getLogger(SensorsControlPanel.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void initHTTPClient() throws MalformedURLException {
        // represent the path portion of the URL as a file
        URL url = getDocumentBase();
        File file = new File(url.getPath());
        // get the parent of the file
        String parentPath = file.getParent();
        // construct a new url with the parent path
        URL parentUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), parentPath);
        hc.setBase(parentUrl);
    }

    private class SensorDownloadWorker extends SwingWorker<Void, NodeList> {

        @Override
        protected Void doInBackground() throws Exception {
            NodeList sensors = getSensors();
            publish(sensors);
            return null;
        }

        @Override
        protected void process(java.util.List<NodeList> chunks) {
            NodeList sensorsList = chunks.get(0);
            appletGUI.model.removeAllElements();
            for (int i = sensorsList.getLength() - 1; i >= 0; i--) {
                Element sensorElem = (Element) sensorsList.item(i);
                String listElem = sensorElem.getElementsByTagName("sensor").item(0).getTextContent();
                appletGUI.model.addElement(listElem);
            }
        }

        private NodeList getSensors() throws Exception {
            // prepare the request xml
            Document data = mngXML.newDocument();
            Element rootReq = data.createElement("getSensors");
            String sensorsOfUsername = getParameter("sensorsOfUsername");
            if (sensorsOfUsername != null) {
                Element sensorsOfElem = data.createElement("sensorsOfUsername");
                sensorsOfElem.appendChild(data.createTextNode(sensorsOfUsername));
                rootReq.appendChild(sensorsOfElem);
            }
            data.appendChild(rootReq);
            //showDocument(data);
            Document answer = hc.execute("sensors", data);
            //showDocument(answer);
            NodeList sensorsList = answer.getElementsByTagName("sensors");
            return sensorsList;
        }
    }

    private class AppletGUI {

        private JButton restoreSensors;
        private JScrollPane jScrollPane1;
        private JList sensorsList;
        private JProgressBar sensorProgressBar;
        private JButton removeSensor;
        private JLabel sensorValue;
        private JPanel sensorPanel;
        private Container contentPane;
        public DefaultListModel<String> model;

        public void fillContainer(Container cp) {
            cp.setLayout(new GridBagLayout());
            restoreSensors = new JButton();
            model = new DefaultListModel<String>();
            sensorsList = new JList(model);
            jScrollPane1 = new JScrollPane(sensorsList);
            sensorProgressBar = new JProgressBar();
            sensorValue = new JLabel("0%");
            removeSensor = new JButton();
            sensorPanel = new JPanel();
            removeSensor.setText("Remove");
            restoreSensors.setText("Restore Sensors List");

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(10, 10, 10, 10);  //padding

            sensorPanel.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.PAGE_START;
            c.gridx = 0;
            c.gridy = 0;
            sensorPanel.add(sensorProgressBar, c);
            c.gridx = 1;
            c.gridy = 0;
            sensorPanel.add(sensorValue, c);
            c.gridx = 2;
            c.gridy = 0;
            sensorPanel.add(removeSensor, c);

            c.anchor = GridBagConstraints.LINE_START;
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            cp.add(restoreSensors, c);
            c.gridx = 0;
            c.gridy = 1;
            c.weighty = 2.0;
            cp.add(jScrollPane1, c);
            c.gridx = 1;
            c.gridy = 1;
            c.weighty = 1.0;
            cp.add(sensorPanel, c);
            contentPane = cp;
            
            sensorsList.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent evt) {
                    JList list = (JList) evt.getSource();
                    if (evt.getClickCount() == 2) { // User has double-clicked
                        int index = list.locationToIndex(evt.getPoint());
                        String row = (String) model.getElementAt(index);
                        //TODO: call single sensor download worker
                        //new TweetDeleteWorker(row[0], row[1]).execute();
                    }
                }
            });
        }       
    }

    private class CometUpdaterThread extends Thread {
        
        private final int sensorID;
        private boolean runComet = true;

        public CometUpdaterThread(int sensorID){
            this.sensorID = sensorID;
        }
        
        public synchronized void stopComet(){
            runComet = false;
        }
        
        @Override
        public void run() {
            while (runComet) {
                try {
                    // prepare the request xml
                    Document data = mngXML.newDocument();
                    Element rootReq = data.createElement("waitForUpdate");
                    Element sensorIDTag = data.createElement("sensorID");
                    Text sensorIDTextNode = data.createTextNode(String.valueOf(sensorID));
                    data.appendChild(rootReq);
                    Document answer = hc.execute("sensorValue", data);
                } catch (Exception e) {
                }
            // I need to trigger a sensor refresh
            }
            
            if (runComet){
                //TODO: Call single sensor worker update
                //new DownloadWorker().execute();
            }
        }
    }
    }
