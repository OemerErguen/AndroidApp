# Sprint Report (2)

In diesem Sprint wurden die bis zum [Meilenstein 5, 23.01.2019](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/milestones/2) erledigten User Stories abgearbeitet. Während der Vorlesungszeit und wenn nicht durch anderweitig besprochen, fanden wöchentliche Treffen des Teams statt.

## Verbesserte Dokumente

### Product Backlog
- Das [Board](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/boards) wird abgearbeitet von links nach rechts und das Team entscheidet die Priorisierung gemeinsam.
- Neue Issues, v.a. Tasks und Bugs, wurden angelegt
- Hinweis: Das Backlog enthielt noch den Meilenstein *4.5*, der auf die Kundenbesprechung 2 datiert ist und als Planungshilfe diente.
- Genaue Zeitangaben wurden eingetragen, wo vorhanden.

### Entwurf
- Starke Aktualisierungen für den aktuellen Stand.
- Komponentenplan überarbeitet und vorerst finalisiert

### Defintion of Done
- Keine Änderungen.

### Zeitabrechnung
Die Zeiten aller Gruppenmitglieder wurden aktualisiert.

### Readme
- Ausführliche **Features und -beschreibungen** wurden hinzugefügt.
- Verwendete Bibliotheken und Lizenzen für die App wurde aufgelistet
- Die Lizenz wurde festgelegt auf GPL v3. Hinweise in der *Readme.md* beachten.

### QR Code Guide
- Es gibt ein Dokument, das beschreibt, wie die QR Codes zusammengesetzt sein müssen/sollen.

### Sonstiges
- Screenshots der App werden ständig hinzugefügt in den Ordner `./doc/Screenshots`

### Aktivitäten an Critical Features
- [**\#17 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/17) SNMP-Verbindung mit mehrere Geräten gleichzeitig
- [**\#15 (Epic)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/15) Functional Ready
- [**\#19 (Internal Requirement)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/19) Geräte-Architektur Modellierung abschließen. Es war zunächst ziemlich knifflig alle SNMP-Verbindungen über einen v3- und einen v1/v2c-Socket zu leiten in einer Multithreading-Umgebung mit den Android *Tasks*. Dies war sehr testintensiv.
- [**\#18 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/18) Aufbau der **Geräte-Detailansicht** mit Tabs und 8 Standard-Tabellen aus dem RFC 1213.
- [**\#24 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/24) Eigene Abfragen hinzufügen, entfernen, bearbeiten und mit **Hardware-Tags** (alias *Kategorien*) versehen.
- [**\#51 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/51) Verbindungs-Parameter anzeigen als Dialog aus dem Optionsmenü in der **Geräte-Detailansicht**.
- [**\#55 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/55) Benutzerhinweis, wenn noch keine Verbindungen existieren.
- [**\#61 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/61) Volle Untersützung für SNMPv3 Verbindungsparameter, auch und insbesondere im QR-Code.
- [**\#64 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/64) AnwenderIn kann wählen, ob eine SNMP-Verbindung mit Version v1 oder v2c verwendet wird.


### Aktivitäten an Additional Features
- [**\#25 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/25) Erweitere Abfrage-Ansicht. Ausbau der **Geräte-Detailansicht** mit verschiedenen (und eigenen) Tabs. Optisches Aufpolieren des MIB-Katalogs.
- [**\#56 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/56) Kundenindividueller Splash-Screen für die App
- [**\#53 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/53) SNMP-Timeout und Retries konfigurierbar machen
- [**\#59 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/59) Benutzersitzungs-Timeout. Konfigurierbar in Minuten in den Einstellungen.
- [**\#54 (Feature)**](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/issues/54) IANA Enterprise Nummern (erste 5000 + alle mit "cisco" und "bsd" im Namen) sind in der App und werden angezeigt hinter der **sysObjectId** in den Ansichten.


Die Einträge des Changelogs mit Datumsangaben finden sich [hier](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/blob/master/CHANGELOG.md).


### Allgemein

- Der Code ist kommentiert, so dass es für jeden verständlich sein sollte.
- Der Code ist kompilierbar und die App stürzt nicht ab.
- Der Code wurde mit Sonarqube einer statischen Codeanalyse unterzogen sowie `gradlew lint` geprüft und optimiert.
- Die [Screenshots](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/tree/master/doc/Screenshots) sind chronologisch abgelegt und können als Entwicklungshistorie betrachtet werden.


## Tests/Testprotokolle/Nachweis der Testabdeckung

Es existiert ein Skript zu Generierung von richtigen (und falschen) Geräte-QR-Codes. Funktioniert nur mit dem Paket *qrencode* installiert: [abnahme_codes.sh](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/blob/master/doc/Testnetz/abnahme_codes.sh)

Ein aktuelles (*Jacoco*) Testprotokoll vom 23.01.2019 ist im Ordner [*./doc/coverage/*](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/tree/master/doc/coverage/reports_23_01_2019.zip) als gezippte Datei zu finden.

Daraus geht auch die aktuelle Testabdeckung hervor.

**Hinweis:** Aufgrund großer technischer Hürden, ist es sehr aufwändig, die SNMP-, Query- und Netzwerkklassen realitätsnah unit-testen zu können. Um alle geplanten Critical und Additional Features umsetzen zu können, haben wir als Team die Erstellung von integrierten Espresso-Tests nicht sehr hoch priorisiert.
Als Ausgleich dazu, fanden ausführliche Treffen statt, bei denen gemeinsam die App manuell getestet wurde. Daraus ist ein erster Entwurf für einen [manuellen Testplan](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/blob/master/doc/Testplan.md) entstanden.
In der Zeit bis M6 sollen noch weitere Tests zur App hinzugefügt werden, um die Stabilität abszusichern.


[Hier ist das Ergebnis](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/blob/master/doc/sonar_ergebnis_23_01_19.png) einer statischen Codeanalyse mit **Sonarqube**.

Immer wieder können wir beobachten, dass CI-Tests Random failen und bspw. mit `java.lang.RuntimeException: Failure from system` enden.
[Beispiel](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/-/jobs/63379) die anderen beiden Pipelines funktionieren jedoch, wie [hier](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/pipelines/15141) ersichtlich wird.



