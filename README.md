Applicazione e servizi web - Unibo - 2014-2015
==============================================

Titolo
======
Sensors Manager

Autori
======
Enrico Benini - 701013 - enrico.benini5@studio.unibo.it

Thomas Farneti - 710232 - thomas.farneti@studio.unibo.it

DESCRIZIONE DEL SERVIZIO OFFERTO DAL SITO
=========================================
Pannello di controllo di una serie di sensori ed attuatori che agiscono sui primi. In particolare il valore dei sensori variera' al variare del valore degli attuatori e notificheranno il loro valore e stato.
La gestione e visualizzazione dei sensori e attuatori e' influenzata dalla tipologia di utenti, si veda la sezione seguente.

UTILIZZO DEL SITO
=================
Tipologie di utenti:

1. Utenti non registrati
    *  E' possibile registrarsi.
    *  Non e' possibile visualizzare nulla di sensori e attuatori.
  
2. Utenti registrati
    * Login/Logout.
    * Visualizzazione dei sensori: il loro stato,tipo e il loro valore.
    * Agire sul valore degli attuatori.
  
3. Utente amministratore (Singolo)
    * Tutto quello previsto per gli utenti registrati.
    * Agire sull'abilitazione dei sensori. (Abilitare e Disabilitare)
    * Aggiungere e Rimuovere sensori.

REALIZZAZIONE DEL SITO - Sommario
=================================
Computazione lato client
------------------------
Consiste in diverse RIA eseguite come applet e Javascript, incorporate in pagine web. In particolare:

1. *sensorsControlPanel* si occupa di tutta la parte relativa al monitoraggio dei sensori e alla loro visualizzazione, quindi una RIA javascript che consente l'abilitazione e l'aggiunta/rimozione. E' disponibile solamente all'utente amministratore.
2. *actuators* contiene la RIA javascript che realizza la visualizzazione degli attuatori e la modifica dei loro valori. Accedibile da tutti gli utenti.
3. *sensors* contiene l'applet che visualizza tutto quello che riguarda i sensori attualmente presenti nel sistema.

le funzioni javascript utilizzate si trovano nella cartella apposita per i file javascript all'interno della Web application. Si possono trovare quindi tutte le funzioni utilizzate per creare i vari file XML e per effettuare le richieste e gestire il comportamento lato client delle varie RIA. Si scendera' nel dettagli di come e' la struttura delle varie RIA nella sezione apposita.

Computazione lato server
------------------------
È compresa nelle seguenti JSP:

1. index.jsp, Semplice pagina di benvenuto, incorpora le parti comuni di navbar e header.
2. login.jsp, che verifica le credenziali dell'utente e lo redirige alla pagina principale, in caso di successo.
3. sensors.jsp, contiene la applet apposita per la visualizzazione e monitoring dei sensori. Tramite questa si e' in grado di visualizzare i sensori che si e' abilitati a visualizzare e cotrollare il loro valore/stato/tipo.
4. actuators.jsp, cosente di visualizzare gli attuatori e di modificarne il valore.
5. sensorsControlPanel.jsp, consente di gestire i vari sensori, in particolare l'abilitazione e l'aggiunta.

Le servlet utilizzate sono:

1. RegistrationServlet, che provvede alla registrazione di un nuovo utente
2. LoginServlet, si occupa del login e logout dell'utente.
3. ActuatorService, servizio che mette a disposizione una serie di operazioni per la gestione dei attuatori consentendo alla computazione lato client di effettuare le varie richieste per effettuare le modifiche.
4. SensorService, servizio che mette a disposizione una serie di operazioni per la gestione dei sensori consentendo alla computazione lato client di effettuare le varie richieste per effettuare le modifiche.

Informazioni memorizzate sul server e scambiate sulla rete
----------------------------------------------------------

Le informazioni memorizzate sul server sono quelle realtive a:

1. Gli utenti registrati. (users.xml)
2. Gli attuatori presenti nel sistema. (actuators.xml)
3. I sensori presenti nel sistema. (sensors.xml)

Le informazioni scambiate sono principalmente suddivisibili in:

1. xml presente nelle request, generalmente formato da un nodo radice che contiene il nome dell'operazione richiesta e opzionalmente altri nodi, contenenti parametri da elaborare (es: li nuovo valore di un attuatore).
2. xml presente nelle response, generalmente formato da una lista di elementi (es: lista di sensori)


REALIZZAZIONE DEL SITO - Tecnologie
===================================

Computazione lato client
------------------------

### Applet ###
L'applet si divide in varie parti per la gestione della concorrenza e per il corretto aggiornamento della GUI. Tutto comunque si trova condensato all'interno di un'unico file che contiene la classe che eredita da JApplet e che al suo interno contiene varie altre classi private. Inoltre alcune cose sono state spostate all'intero di una libreria esterna perche' si riteneva che potessero essere utili nella costruzione di altre parti del sistema come i servizi e/o altre applet future.
Le varie parti che compongono l'applet sono:

*  SensorsControlPanel: nome della classe che eredita da JApplet e che contiene l'override dei principali metodi, per controllare il ciclo di vita dell'applet stessa.
*  SensorDownloadWorker: SwingWorker che consente di scaricare i dati relativi ai sensori dal server.Si e' deciso di utilizzare uno swing worker per aggiornare correttamente la GUI una volta che avviene la risposta del server alla chiamata.
*  AppletGUI: Classe interna che contiene tutto cio' che riguarda la parte grafica dell'applet. Per comporre la parte grafica si utilizza anche la classe *EntryListCellRenderer* che riguarda lo scheletro del singolo elemento di renderizzazione che conterra' i dati dei sensori, un istanza per sensore.
* CometValueUpdaterThread: Classe thread che consente di gestire le notifiche che lato server arrivano all'applet.

Gli oggetti attivi vengono poi gestiti dall'applet negli appositi metodi di start e stop in modo da non lasciare computazioni inutili in esecuzione.

### RIA Javascript ###
Per la realizzazione delle RIA Javascript si e' utilizzata la libreria esterna *JQuery* vista a lezione che facilita e pulisce il codice per effettuare varie operazioni. Inoltre si sono predisposti vari file javascript che contengono le funzioni, generiche o peculiari, per la realizzazione di ogni RIA, in particolare:

*  *requestBuilder*: Contiene le funzioni generiche condivise da tutte le RIA, in particolare quella per effettuare le chiamate AJAX verso il server e la funzione di logging per controllare le risposte e richieste.
*  *sensorControlPanel*: Contiene le funzioni specifiche per la gestione del comportamento della RIA. Nello specifico gli event handlers e le callback relative.
*  *sensorControlPanelXmlBuilder*: Contiene le funzioni specifiche per la creazione delle richieste al servizio del sensori che servono alla RIA per effettuare la computazione.
*  *actuatorsControlPanel*: Contiene le funzioni specifiche per la gestione del comportamento della RIA. Nello specifico gli event handlers e le callback relative.
*  *actuatorsControlPanelXmlBuilder*: Contiene le funzioni specifiche per la creazione delle richieste al servizio degli attuatori che servono alla RIA per effettuare la computazione.


Computazione lato server
------------------------
Lista dei fragments utilizzati:

1. *navbar.jspf*: fragments incluso in tutte le pagine che si occupa di renderizzare la barra di navigazione. Se l'utente è loggato, mostra all'utente i link le pagine per visualizzare sensori e attuatori. Se l'utente non è loggato, mostro i link per le pagine di registrazione e login. Fa distinzione anche da utente amministratore a utente non amministratore utilizzando la sessione.
2. **
 
(Aggiungere descrizioni del servizio)