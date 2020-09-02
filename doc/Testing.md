# Testing

Dieses Dokument beschreibt das Test-Setup für (automatisierte) Tests der App auf den Zielplattformen:

- SDK 25
- SDK 26
- SDK 27

Allgemein verwendet das Projekt die Testbibliotheken *JUnit*, *Espresso* und *Mockito*.

Die Ausführungsparameter der Tests auf dem Sopra Gitlab sind in der Datei *.gitlab-ci.yml*.

Android Links:

- https://developer.android.com/training/testing/fundamentals

```yaml
ext.mockitoVersion = "2.22.0"
ext.androidTestVersion = "1.0.2"
ext.espressoVersion = "3.0.2"
...
`testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"`
```


## JUnit Tests
*./app/src/test/java/de/uni_stuttgart/informatik/sopra/sopraapp*
**Package:** *de.uni_stuttgart.informatik.sopra.sopraapp*<

Ganz normale Junit-Tests. Z.B.: *de.uni_stuttgart.informatik.sopra.sopraapp.BooleanObservableTest*.

## AndroidTests + Espresso
*./app/src/androidTest/java/de/uni_stuttgart/informatik/sopra/sopraapp*
**Package:** *de.uni_stuttgart.informatik.sopra.sopraapp*<

Oberflächen-Tests. Z.B.: *de.uni_stuttgart.informatik.sopra.sopraapp.CockpitMainActivityTest*.