# Coding Guidelines Team 22

Folgende Liste ist ein Vorschlag für die gemeinsamen Coding Standards des Projekts:

**Entwurf und noch nicht gemeinsam besprochen**

## Versionskontrolle
* In keinem Fall mit git force-pushen
* Alle Aufgaben werden in Branches entwickelt, die dann wieder in den master Branch gemerged werden
* Jeder Merge-Request wird gereviewed von einem anderen Teammitglied und die Kommentarfunktion im Gitlab wird verwendet
* Wenn der Code in Ordnung ist und die Fragen/Anmerkungen/Nachbesserungen geklärt sind, wird gemerged durch die/den AutorIn
* Nach einem erfolgreichen Merge wird der alte Branch gelöscht
* Optionale Schweifklammern werden immer gesetzt
* Keine unnötigen Debug-Ausgaben im master Branch
* Kein instabiler Stand oder kritische Änderungen am Code direkt im master Branch


## Coding Style
* Sprechende Variablenbezeichner in lowerCamelCase
* ~20% Kommentare/Lines of Code
* Auto-Formatierung vor Commits in Android Studio: *Code* > *Reformat Code*
* Java Style Guide

## Leitprinzipien:
* Keep it simple
* Don't repeat yourself
* Fail fast
* Single responsibility principle
    * Eine technische Aufgabe = eine Codestelle
