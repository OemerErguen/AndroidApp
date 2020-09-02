# Glossar

Zusätzlich zum [SoPra-Begriffslexikon](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-doku-entwickler/blob/master/Begriffslexikon.md)
halten wir hier weitere teamweit definierte Begriffe fest.

## CI
Continuous Integration

## CI-Test
Die Java-Testklassen, die entweder automatisch oder durch manuelles Anstoßen auf dem Gitlab CI Server laufen.

## CI-Pipeline
Dies ist der Durchlauf einer Continuous Integration "Pipeline", deren konkreten Ablauf in der Datei *.gitlab-ci.yml* festgehalten ist.
Diese war so vorgegeben im Projekt.  

Aktuell läuft automatisch:
* *build* (Die Software wird gebaut und das Artefakt (=apk) für die weiteren Schritte wird kompiliert.
* *test* Alle junit und android instrumentation Tests laufen

Optional (manueller Trigger in der [Gitlab-CI-Oberfläche](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/pipelines))):
* Instrumentation Tests für SDK 25, 26, 27
* Code Coverage Report

