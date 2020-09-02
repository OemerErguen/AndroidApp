# Product Backlog

Hier werden **alle** Anforderungen in Form von **User Stories** geordnet aufgelistet.

**Hinweis:** Die Aufteilung dieses Product Backlogs ist primär nach der folgenden Idee ausgerichtet:   
**Epic > Internal Requirement/Feature = User Story > Task**

Der Begriff des *Users* der Software schließt explizit auch die EntwicklerInnen mit ein. 

*Implementable Stories* sind hier in Storyform die *Features* oder schlichtweg *Tasks* der *Features*.  

Dabei soll weiterhin gelten:
 - *Epics* haben EntwicklerInnen- und KundInnen-Sicht  
 - *Features* und *Tasks* haben AnwenderInnen- und/oder EntwicklerInnen-Sicht oder sind selbstklärend durch ihren Titel
 - Die Mehrzahl der *Internal Requirements* sind selbstklärend durch Akzeptanzkriterien oder Titel
 - Die Anordnung bzw. Nummerierung der Epics entspricht ihrer (vorläufigen) zeitlichen/logischen Abfolge im Projekt
 - Tasks werden final geschätzt mit Arbeitsstunden-Zeiträumen (+-2 h) während des Plannings, in dessen Sprint sie bearbeitet werden.
   Auch deshalb haben einige Features (noch) keine finalen Akzeptanzkriterien enthalten.

**Hinweis: Dieses Dokument ist bis MS 3 (9.11.18) das zentrale Dokument in Sachen Aufgabenplanung des Teams 22, unmittelbar danach übernimmt der Gitlab Issue Tracker des Projekts.**

Hier ist das Product Backlog hierarchisch als Mind Map visualisiert mit Informationen zur Zeitplanung:
Erste Ebene: *Epics*  
Zweite Ebene: *Features* und *Internal Requirements*  
Dritte Ebene: *Tasks*

![Visual product backlog mind map in file backlog.svg](./backlog.svg)*Struktur des Product Backlogs Team 22*

** Hinweis: ** In Epic 1 sind einige Stories schon entfernt worden, da sie zeitlich schon vorüber sind (siehe MindMap).


Zielplattform: Android targetSdk 27


## Epic 1: Grundsystem

#### *Stories*
> Als KundIn will ich, dass die Abarbeitung der Critical Features meines Auftrags losgehen kann.

> Als EntwicklerIn möchte ich mit einem spezifizierten und definierten Projekt-Fahrplan in die Arbeit der nächsten Monate einsteigen und deshalb benötige ich eine Übersicht.


Ausführliche Beschreibung:
### Feature 1.1 WLAN-QR ready
##### *Story*
> Als AnwenderIn möchte ich einen standardisierten WLAN QR-CODE mit der Handykamera einscannen können, um mich mit dem WLAN meines Vertrauens zu identifizieren.
    
    - Aufwandsschätzung: L
    - Akzeptanzkriterien:
        - JedeR AnwenderIn hat die Möglichkeit aus der App heraus einen WLAN-QR-Code mit der Handykamera einzuscannen, um sich in einem WLAN anzumelden
        - Wenn der gelesene QR-Code nicht dem erwarteten Format entspricht, 
          erscheint eine entsprechende Fehlermeldung für den/die AnwenderIn
        - Wenn die Anmeldung am WLAN nicht erfolgreich ist, erscheint eine 
          entsprechende Fehlermeldung für den/die AnwenderIn 
 
### Internal Requirement 1.2 Test-Infrastruktur und Dokumentation
    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
        - JedeR EntwicklerIn hat eine Einführung in den grundsätzlichen Aufbau der 
          (CI-)Tests erhalten durch die verantwortliche Rolle
        - JedeR EntwicklerIn kann die CI Pipeline steuern und zum Debuggen nutzen

### Feature 1.3 Settings-Komponente
##### *Stories*
> Als AnwenderIn möchte ich grundsätzlich zentrale Ausführungsparameter der App selbst bestimmen und regeln können.

> Als EntwicklerIn möchte ich zentrale Ausführungsparameter der App leicht in alle kommenden Features integrieren können.

    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
        - JedeR NutzerIn kann im Hauptmenü der App das Einstellungs-Menü öffnen
        - JedeR EntwicklerIn ist informiert, wie appweite Einstellungen implementiert werden durch die verantwortliche Rolle.
        - Es existieren Unit-Tests für die Funktionalität

### Internal Requirement 1.4 Clickdummy und Design Vorgaben
    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
        - Die grundlegende Struktur der App liegt als Prototyp vor sowie wesentliche gestalterische Elemente und Strukturen stehen fest
        - Der Clickdummy entspricht dem (abgegebenen) Entwurfsdokument doc/Entwurf.md
        - targetSdk = 27
        - Android X
        - Material Themes + Icons

### Internal Requirement 1.5 UI-Review
##### *Story*
    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
    	- Der Clickdummy wurde vom gesamten Team begutachtet, Feedback wird in neue Issues überführt

### Internal Requirement 1.6 Alle Anwendungsfälle definieren
> Als EntwicklerIn möchte ich eine Liste an Anwendungsfällen haben zur verbesserten Orientierung für Test und Implementierung

    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
    	- Alle vorläufig fest geplanten Anwendungsfälle der App sind in der Dokumentation
    	  definiert

## Epic 2: Durchstich (App -> SNMP -> Hardware)
#### *Stories*
> Als KundIn möchte ich wissen, ob und wie die Grundlagen der Anforderungen meines Auftrages technisch prinzipiell umsetzbar sind.

> Als EntwicklerIn möchte ich frühzeitig im Projekt wissen, wie genau die *Kritischen Features* des Prokjekts umgesetzt werden und was die Zielarchitektur ist. 

Ausführliche Beschreibung:
### Feature 2.1 Menüstruktur/Benutzerführung
##### *Story*
> Als AnwenderIn möchte ich eine klare und möglichst intuitive Menü- und Bedienungsstruktur der App.

    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
    	- Material Design Guidelines sind im Großen und Ganzen eingehalten.
    	- Keine Benutzerfunktion ist doppelt in der App vorhanden/erreichbar.
    	- Bedienungsoberfläche ist intuitiv erschließbar.

### Feature 2.2 Networking/Secure WLAN ready
##### *Story*
> Als AnwenderIn möchte ich niemals Operationen der App in einer unsicheren WLAN-Umgebung ausführen.

    - Aufwandsschätzung: L
    - Akzeptanzkriterien:
        - Bei sich ändernden WLAN-Zugangsdaten (Änderung Passwort, aber 
          Access-Point-Name und MAC gleich) sind keine weiteren 
          Benutzer-Interaktionen (außer QR-Code scannen) nötig.
        - Die Netzwerkparameter des WLANs (IPv4-, IPv6-Adresse, Netzmaske, 
          DNS-Server-IP, Gateway-IP) inkl. DHCP-Zuweisung werden dargestellt.
        - In unverschlüsselte WLANs bucht sich die App nicht ein, auch nicht im Fehlerfall.
        - Beim Wechsel in ein unverschlüsseltes WLAN wird eine Fehlermeldung 
          angezeigt und es werden keine Daten übertragen.
        - Beim Wechsel in das gesicherte WLAN wird der Betrieb der Anwendung 
          wieder im ursprünglichen Zustand aufgenommen.
        - Prüfung: Ist für den W-Lan Schlüssel ein Ablaufdatum hinterlegbar?

### Internal Requirement 2.3 Abfrage-Modell konzeptionieren
    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
        - Es liegt ein zentrales Dokument für alle zugreifbar vor für das nächste 
          Epic
          
### Feature 2.4 Anmelde-Maske für manuelle Verbindung mit der Hardware
##### *Story*
> Als AnwenderIn möchte ich mich mit einer Hardware verbinden können durch Eingabe der im QR-Code definierten Parameter, um mich mit dem Gerät zu verbinden.

    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
    	- Manuelle Verbindung mit einem Gerät im Menü für den benutzer sichtbar.
    	- Alle in der Dokumentation definierten Geräte-/QR-Code-Parameter, können
    	  manuell eingegeben werden.
### Feature 2.5 SNMP ready zur Verwendung mit einem einzelnen entfernen Gerät
##### *Story*
> Als AnwenderIn möchte ich mich mit einem Gerät per SNMP verbinden können, um dann später Abfragen durchführen zu können.

    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
    	- Die Verbindung zu einem Gerät kann per SNMP hergestellt werden, über die
    	  manuelle Eingabe.
        - Es wird an erster Stelle mindestens die IP des verbundenen Geräts
          angezeigt.

## Epic 3: Functional Ready
#### *Stories*
> Als KundIn möchte ich mir sicher sein, dass die wesentlichen technischen Projektziele umgesetzt werden und ausgestaltbar sind.

> Als EntwicklerIn möchte ich die kritischen Features implementieren, testen und die weiteren Grundlagen schaffen für die geplanten App-Features.

Ausführliche Beschreibung:
### Feature 3.1 Geräte QR-Code lesbar
##### *Story*
> Als AnwenderIn möchte ich einen standardisierten Hardware QR-CODE mit der Handykamera einscannen können, um mich mit einem Gerät per SNMP zu verbinden.

    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
        - Einlesen der Geräte-Zugangsdaten ist via QR-Code möglich.
        - Man kann - sofern mit einem sicheren WLAN verbunden - die QR-Code-Scanner Funktion aufrufen und einen Geräte-QR-Code einscannen
        - Die App verarbeitet in diesem Modus ausschließlich den in der 
          Dokumentation definierten standardisierten QR-Code weiter
        - Nach dem Einscannen eines standardisierten Geräte-Codes ist die App 
          sichtbar per SNMP mit dem erwarteten Ziel-Gerät verbunden
        - Wenn der QR-Code nicht lesbar oder nicht dem erwarteten codierten 
          JSON-Format entspricht, wird eine entsprechende Fehlermeldung angezeigt

### Feature 3.2 SNMP-Verbindung mit mehreren Geräten gleichzeitig
##### *Story*
> Als AnwenderIn möchte ich mich mit 1+x Geräten gleichzeitig verbinden können.

    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
        - Das mehrfache Einscannen von Geräte-Codes ist möglich.
        - Es können mindestens 3 Geräte problemlos zur App hinzugefügt werden.
        - Bei einem Neustart der App sind die Geräte nicht mehr verbunden.
        - Einsicht der Verbunden Geräte und Trennung der einzelnen/allen Geräten.

### Feature 3.3 Standard-Abfragen-Templates
##### *Story*
> Als AnwenderIn möchte ich Abfragen von Standard Netzwerk- und Hardware-Parametern per SNMP ausführen können.

    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
        - Standard-Abfragen (1.3.6.1.2.1.1) werden vom Gerät abgefragt und 
          angezeigt.
        - In der Dokumentation zu definierende Standard-Geräteparameter werden auf 
          der Benutzeroberfläche für jedes per SNMP verbundene Gerät
        - Es werden per SNMP lediglich Informationen gelesen, in keinem Fall geschrieben

### Feature 3.4 Geräte-Architektur Modellierung abschließen
##### *Story*
> Als AnwenderIn benötige ich im Alltag eine stabile Funktion, um eins bis mehrere Geräte gleichzeitig zu beobachten.

    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
        - Per SNMP verbundene Geräte können in der Oberfläche entfernt werden und 
          erneut über einen QR-Code Scan verbunden werden
        - Gestartete Abfragen werden periodisch erneut abgefragt
        - Das Zeitintervall ist in Sekunden zwischen 0 und 180 Sekunden in den
          Einstellungen festlegbar und wird sofort in der App übernommen
        
### Internal Requirement 3.5 Definierte Anwendungsfälle der Dokumentation sind manuell test und durchführbar
    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
    	- Jedes Critical Feature lässt sich in der App grundsätzlich wie zu erwarten
    	  ausführen.

### Internal Requirement 3.6 Projektplanungs-Retrospektive
    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
    	- Es fand ein gemeinsames Treffen aller Teammmitglieder statt und die 
    	  bisherige Projektplanung wurde kritisch evaluiert.

### Internal Requirement 3.7 Monitoring-Konzept
    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
    	- Es liegt ein Dokument vor, für das nächste Epic, mit den geplanten
    	  Standard-SNMP-Monitoring-Abfragen.
    	- Jedes Teammitglied ist informiert über die geplante technische Umsetzung des
    	  Monitoring-Konzepts (Feature 3.7 Monitoring-Konzept) im kommenden Sprint.

## Epic 4: Abfrage-Bibliothek
#### *Stories*
> Als KundIn möchte ich das Projekt mit meinem Know-How und Wissen anreichern und liefere fachlichen Input, um dem Produkt mehr Nutzen zu verleihen.

> Als EntwicklerlIn möchte ich den NutzerInnen vielfältige Möglichkeiten bieten, SNMP-Abfragen zu verwalten und Monitoring zu betreiben bzw. bei Arbeitsprozessen unterstützende Informationen zu erhalten.

Ausführliche Beschreibung:
### Feature 4.1 Abfragen CRUD
##### *Stories*
> Als AnwenderIn möchte ich eigene SNMP-Abfragen selbst **erstellen** können zur späteren Wiederverwendung.

> Als AnwenderIn möchte ich eigene SNMP-Abfragen selbst **betrachten** können.

> Als AnwenderIn möchte ich eigene SNMP-Abfragen **bearbeiten** können.

> Als AnwenderIn möchte ich eigene SNMP-Abfragen selbst **entfernen** können.

    - Aufwandsschätzung: M-XL
    - Akzeptanzkriterien:
        - Abfragen können verwaltet (neu angelegt, bearbeitet, angesehen und 
          gelöscht) werden.
        - Abfragen enthalten mindestens eine Bezeichnung, eine OID 
          (min. Dot-Notation) den erwarteten Rückgabewert-Typ.
        - Abfragen können keiner, einer order mehreren Kategorien (Art der Hardware)
          zugeordnet werden.
        - Abfragen können gestartet werden und das Result angezeigt werden.

### Feature 4.2 Erweiterte Abfragen-Templates
##### *Story*
> Als AnwenderIn möchte ich neben den bereits vorhandenen Standardabfragen auch komplexere fest in der App integrierte Abfragen starten können. 

    - Aufwandsschätzung: S-L
    - Akzeptanzkriterien:
    	- Das Monitoring-Konzept ist technisch umgesetzt. 

### Internal Requirement 4.3 Komponentenplan und externe Bibliotheken sind definiert
    - Aufwandsschätzung: S-M
    - Akzeptanzkriterien:
        - Alle externen Bibliotheken sind wie im Sopra-Projekt definiert 
          dokumentiert und wurden genehmigt.
        - Ein übersichtlicher Komponentenplan liegt vor

### Internal Requirement 4.4 UI/UX-Review and Finetuning
    - Aufwandsschätzung: S-M
    - Akzeptanzkriterien:
        - tbd

## Epic 5: Finalisierung
#### *Stories*
> Als KundIn möchte ich das Projekt ausgeliefert und fertig zum Einsatz in den Händen halten.

> Als EntwicklerIn möchte ich das Projekt abschließen und die erste Version veröffentlichen.


Ausführliche Beschreibung:
### Feature 5.1 Benutzerdokumentation
##### *Story*
> Als AnwenderIn möchte ich nachlesen können, was das Bedienkonzept der App ist und welche Funktionen und Konfigurationen die App bietet.

    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
        - Eine zielgruppengerechte Benutzerdokumentation 1-5 Seiten liegt vor

### Feature 5.2 Security-Review
##### *Story*
> Als AnwenderIn möchte ich eine App verwenden, die keine Dritte in Berührung mit den verwendeten Daten und Informationen kommen lässt.

    - Aufwandsschätzung: S-L
    - Akzeptanzkriterien:

### Feature 5.3 Konzeption und Umsetzung Share-Funktion
##### *Story*
> Als AnwenderIn möchte ich meine aktuelle Beobachtung mit der App festhalten und einem anderen Benutzer mit "Teilen" übermitteln. 

    - Aufwandsschätzung: S-M
    - Akzeptanzkriterien:
        - muss noch definiert und geplant werden

### Internal Requirement 5.4 Alle Critical Features +x sind getestet
##### *Story*
> Als AnwenderIn/EntwicklerIn möchte ich, dass die kritischen Funktionen der App automatisiert unter Testbedingungen wie erwartet ausgeführt werden.

    - Aufwandsschätzung: S-L
    - Akzeptanzkriterien:
        - siehe Titel

### Feature 5.5 Performance-Analysierung und ggfs. Optimierung
##### *Story*
> Als AnwenderIn möchte ich eine möglichst reibungslose und performante App wahrnehmen, um sie nahtlos in meinen Arbeitsalltag integrieren zu können.

    - Aufwandsschätzung: M-L
    - Akzeptanzkriterien:

### Internal Requirement 5.6 Lizenz-Check und Copyright Header
    - Aufwandsschätzung: S
    - Akzeptanzkriterien:
        - Die wesentlichen Projektdateien, insbesondere alle Sourcefiles enthalten Copyright-Header sowie konkrete Lizenzinformationen
        - Das Projektrepo enthält Lizenzinformationen

### Internal Requirement 5.7 Abnahme/Release ready
    - Aufwandsschätzung: M
    - Akzeptanzkriterien:
        - tbd
