# QR Code Guide

`qrencode -o qr_code.png "<QR-String>"`

## Wifi QR-Code

**Schema:** `WIFI:S:<SSID>;T:WPA2;P:<PW>;;`
[Erklärung und Anleitung](https://github.com/zxing/zxing/wiki/Barcode-Contents#wi-fi-network-config-android-ios-11)


## Device QR-Code

**Schema:** 
```json
{
     "user": "",
     "pw": "",
     "enc": "",
     "naddr": {
         "IPv4": "",
         "IPv6": ""
     }
 }
```

- **user**:
    - *v1*: Community
    - *v3*: Benutzername
- **pw**:
    - *v1*: leer
    - *v3*: Auth-Passwort
- **enc**:
    - *v1*: leer
    - *v3 einfach*: Priv-Passwort
    - *v3 erweitert*: Schema: `SecurityLevel;AuthProtocol;PrivProtocol;Context;Password`
        - **SecurityLevel**: int 0-2
            - **0**: AUTH_PRIV
            - **1**: AUTH_NO_PRIV
            - **2**: NO_AUTH_NO_PRIV
        - **AuthProtocol**: int 0-5
            - **0**: SHA-1
            - **1**: MD5
            - **2**: HMAC128SHA224
            - **3**: HMAC192SHA256
            - **4**: HMAC256SHA384
            - **5**: HMAC384SHA512
        - **PrivProtocol**: int 0-4
            - **0**: AES-128
            - **1**: DES
            - **2**: AES-192
            - **3**: AES-256
            - **4**: 3DES
        - **Context**: leer oder String
        - **Password**: Priv-Passwort
- **naddr**:
    - *v1 und v3*:
        - **IPv4**: IPv4-Addresse eines SNMP-Daemons
            - **Spezieller Port**: mit ":\<Port\>" Addresse suffixen
        - **IPv6**: oder IPv6-Addresse



### Beispiele für Geräte-QR-Codes (falsch und richtig)
Siehe Datei [abnahme_codes.sh](https://sopra.informatik.uni-stuttgart.de/sopra-ws1819/sopra-team-22/blob/master/doc/Testnetz/abnahme_codes.sh).

