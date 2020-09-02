# Informationen zum Testgerät

WLAN WPA2 Zugangsdaten Testgerät:

![WIFI Code](./wifi_testcode_raspi_netz.png)

**SSID:** SoPra-SNMP-RZ-NET
**PW:** RZManagementWLAN
**Hostname:** snmprouter
**Netz:** 192.168.161.1/24

Connect with: `ssh pi@192.168.161.1`

## Hinweise
Für SSH: Ins obige Netzwerk verbinden und dann:
```bash
ssh pi@192.168.161.1
```
PW: ahV5oog3baoDe8Qu

Das Gerät hat zwei WLAN-Schnittstellen (wlan0 (Hotspot), wlan1 (USB-Stick Adapter)). Eine für das WLAN-Netz und das andere,
um in ein anderes Netz (z.B. das Heimnetz) zu verbinden. HTTP/S Traffic wird weitergeleitet und der Pi ist eine Art Router.

Die SNMP Konfiguration liegt im Repo hier (*./doc/config/raspbian.conf*) und auf dem Pi hier: */home/pi/docker-snmpd/*
Hinweise in der [SNMP Debugging Doku](./../Konzept_SNMP_Abfragen.md)

### Eigenes WLAN hinzufügen

**Option 1:** Zusätzlichen Netzwerkeintrag in Datei */etc/wpa_supplicant/wpa_supplicant.conf* vornehmen per SSH.

**Option 2:** HDMI Kabel + Monitor + Maus + Tastatur anschließen und nach dem Hochfahren erscheint ein Desktop, kann aber einige Minuten gehen..
Dann rechts oben auf das Netzwerksymbol klicken und mit wlan1 ans gewünschte Netz verbinden, Passwort eingeben und das Gerät läuft als Mini-Router.


## Geräte QR Codes:

Zum Erzeugen:
```bash
qrencode -o output.png 'QR-String'
```

### SNMPv1 (Port 161, UDP):

![Testgerät 1](./testgeraet_1.png)

```json
{
    "user": "public",
    "pw": "",
    "enc": "",
    "naddr": {
        "IPv4": "192.168.161.1:161",
        "IPv6": "fe80::643c:daf2:b95a:fc05"
    }
}
```

*IPv6 noch nicht getestet/eingerichtet!*

### SNMPv3 (Port 162, UDP):

![Testgerät 2](./testgeraet_2.png)

```json
{
    "user": "batmanuser",
    "pw": "batmankey3",
    "enc": "batmankey3",
    "naddr": {
        "IPv4": "192.168.161.1:162",
        "IPv6": "[fe80::643c:daf2:b95a:fc05]:162"
    }
}
```
*IPv6 noch nicht getestet/eingerichtet!*

## VirtualBox Netz

### SNMP V1 VirtualBox, ohne Port
```json
{
    "user": "public",
    "pw": "",
    "enc": "",
    "naddr": {
        "IPv4": "192.168.178.154",
        "IPv6": ""
    }
}
```

`{"user": "public","pw": "","enc": "","naddr": {"IPv4": "192.168.178.154","IPv6": ""}}`

### SNMP V1 VirtualBox, mit Port
```json
{
    "user": "public",
    "pw": "",
    "enc": "",
    "naddr": {
        "IPv4": "192.168.178.154:161",
        "IPv6": ""
    }
}
```

`{"user": "public","pw": "","enc": "","naddr": {"IPv4": "192.168.178.154:161","IPv6": ""}}`

### SNMP V3 VirtualBox, ohne Port
```json
{
    "user": "batmanuser",
    "pw": "batmankey3",
    "enc": "batmankey3",
    "naddr": {
        "IPv4": "192.168.178.155",
        "IPv6": ""
    }
}
```
{"user": "batmanuser","pw": "batmankey3","enc": "batmankey3","naddr": {"IPv4": "192.168.178.155","IPv6": ""}}


{
    "user": "batmanuser",
    "pw": "batmankey3",
    "enc": "0;0;0;;batmankey3",
    "naddr": {
        "IPv4": "192.168.178.158",
        "IPv6": ""
    }
}


Getestete Wifi Strings:  

WIFI:S:<SSID>;T:WPA2;P:<PW>;;