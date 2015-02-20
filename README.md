Applicazione e servizi web - Unibo - 2014-2015
==============================================

Titolo
======
Sensors Manager

Autori
======
Enrico Benini - 701013 - enrico.benini5@studio.unibo.it

Thomas Farneti -  - thomas.farneti@studio.unibo.it

DESCRIZIONE DEL SERVIZIO OFFERTO DAL SITO
=========================================
Pannello di controllo di una serie di sensori ed eventualmente di attuatori che agiscono sui primi. In particolare il valore dei sensori variera' in maniera pseudo-random e notificheranno il loro valore e stato. Mentre tramite la modifica del valore degli attuatori si avra' un effetto sul valore dei sensori. La gestione e visualizzazione dei sensori e attuatori e' influenzata dalla tipologia di utenti.

UTILIZZO DEL SITO
=================
Tipologie di utenti:

1. Utenti non registrati
  * E' possibile registrarsi.
  * Non e' possibile visualizzare nulla di sensori e attuatori.
  
2. Utenti registrati
  * Login/Logout.
  * Visualizzazione dei sensori, il loro stato e il loro valore che varia nel tempo.
  
3. Utenti amministratori
  * Tutto quello previsto per gli utenti registrati.
  * Agire sugli attuatori.

REALIZZAZIONE DEL SITO - Sommario
=================================
Computazione lato client
------------------------
Consiste in diverse RIA eseguite come applet e Javascript, incorporate in pagine web. In particolare:

1. SensorsControlPanel, che si occupa di tutta la parte relativa al monitoraggio dei sensori e alla loro visualizzazione.
2. All'interno di actuators.jsp sono contenute le funzioni javascript che realizzano la RIA apposita per la visualizzazione degli attuatori, dopo aver effettuato l'apposita richiesta al server.

Computazione lato server
------------------------
È compresa nelle seguenti JSP:

1. index.jsp, Semplice pagina di benvenuto, incorpora le parti comuni di navbar e header.
2. login.jsp, che verifica le credenziali dell'utente e lo redirige alla pagina principale, in caso di successo.
3. sensors.jsp, contiene la applet apposita per la visualizzazione e monitoring dei sensori. Tramite questa si e' in grado di visualizzare i sensori che si e' abilitati a visualizzare e cotrollare il loro valore/stato.
4. actuators.jsp, cosente di visualizzare gli attuatori e di modificarne il valore.

Le servlet utilizzate sono:

1. RegistrationServlet, che provvede alla registrazione di un nuovo utente
2. LoginServlet, si occupa del login e logout dell'utente.

Informazioni memorizzate sul server e scambiate sulla rete
----------------------------------------------------------

Le informazioni memorizzate sul server sono quelle realtive a:

1. gli utenti registrati (users.xml).

Le informazioni scambiate sono principalmente suddivisibili in:

1. xml presente nelle request, generalmente formato da un nodo radice che contiene il nome dell'operazione richiesta e opzionalmente altri nodi, contenenti parametri da elaborare (es: li nuovo valore di un attuatore).
2. xml presente nelle response, generalmente formato da una lista di elementi (es: lista di sensori)


REALIZZAZIONE DEL SITO - Tecnologie
===================================

Computazione lato client
------------------------

(spiegazione di applet e RIA javascript)

Computazione lato server
------------------------
1. novbar.jspf,fragments inglobato in tutte le pagine che si occupa di renderizzare la barra di navigazione. Se l'utente è loggato, mostra all'utente i link le pagine per visualizzare sensori e attuatori. Se l'utente non è loggato, mostro i link per le pagine di registrazione e login.
2. login.jsp, che verifica le credenziali dell'utente e lo redirige alla pagina principale.

Le servlet utilizzate sono:

1. RegistrationServlet, che riceve i dati dal form di registrazione e provvede a salvarli (db xml e directory contenente le immagini del profilo degli utenti).

3. LoginServlet, che a seconda dell'url pattern matchato, provvede a loggare o sloggare l'utente dal sito.
 
(Aggiungere descrizioni del servizio)
