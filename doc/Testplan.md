# App Testplan

## Sicherheitsmodus Testing
- Nach frischer App-Installation darf das aktuelle Netzwerk nicht akzeptiert werden.
- Nach Geräte-Verbindung und dann Netzwechsel, muss das Gerät entfernt werden nach einem Verbindungs-Timeout.


## QR Code Testing

#### Codeliste (korrekte Codes):

**Variation 1:** mit abweichendem(, falschen) Port
**Variation 2:** mit IPv4
**Variation 3:** mit IPv6

1. v1 mit IP und implizitem Port 161 je mit Community *public*
2. v1 mit IP und abweichendem Port je mit Community *public*
3. v3 mit IP für sysadmin1
4. v3 mit IP für sysadmin2
5. v3 mit IP für sysadmin3
6. v3 mit IP für sysadmin4
7. Overflow der einzelnen Integer-Felder


#### Codeliste (falsche Codes)

**Variation 1:** mit abweichendem(, falschen) Port
**Variation 2:** mit IPv6


1. F v1 mit falscher IPv4-Addresse
2. F v1 mit falschem Port
3. F v1 mit leerem Benutzer


