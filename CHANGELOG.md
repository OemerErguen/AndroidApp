# Change Log

All notable changes to this project will be documented in this file.
This project adheres currently NOT REALLY to [Semantic Versioning](http://semver.org/) despite the formatting of the version numbers.

## released 0.6.0 - 30.01.2019
+ small crash fixes
+ doc changes + more tests

## unreleased 0.5.0 - 23.01.2019
+ document updates
+ m5 release ready
- several crash fixes
+ about screen
+ vendor cataloglist (5000+ vendors included)

## unreleased 0.4.13 - 21-01-2019
+ a lot of (code) quality improvements

## unreleased 0.4.12 - 17-01-2019
+ add custom query ui + hard ware tags ui and crud for both

## unreleased 0.4.11 - 15-01-2019
+ add connection timeout + retries user pref

## unreleased 0.4.10 - 14-01-2019
+ add new snmp fields. context, secLevel.
- connection test skip if qr code supplied auth + priv protocol information
+ qr docs

## unreleased 0.4.9 - 06-01-2019
+ session timeout + connection timeout implemented + works on any activity
+ auto-update user switch preference
+ regular ui updates possible
+ several fixes and improvements

## unreleased 0.4.8
+ general device handling finished. create and delete devices.
+ snmp performance optimization + connection handling.
+ ux/ui improvements

## unreleased 0.4.7 - 30-12-2018
+ add start screen for empty device list

## unreleased 0.4.6 - 29-12-2018
+ add all genua mibs to app + update catalog + tree

## unreleased 0.4.5 - 28-12-2018
+ v3 connections: aes + des + md5 tested and implemented, several other encryption options added snmp4j supports
+ connection isolation
+ try all possible priv and auth combinations + ui for it
+ cockpit query view has a loading spinner now
+ refresh methods should work now
- several snmp connection fixes

## unreleased 0.4.4 - 23-12-2018
+ display all snmp tables of rfc1213 in tabbed device section
+ added dummy tab section for custom queries (next big feature!)
+ proper refresh functionality implemented for devices + details
+ device detail view has tabs + info now
+ generic query views for lists and tables

## unreleased 0.4.3 - 20-12-2018
+ add splash screen to app startup process in customer design
+ change app logo
+ more test data

## Unreleased 0.4.2 - 17-12-2018
- login activity can be orientation changed now and is scrollable
- main activity has an own state to handle orientation changes
- framgent device item is restorable on re-create
- several fixes on network security state + preferences change events

## Unreleased 0.4.1 - 16-12-2018
- first stable device handling added
- fix major bug in network security check (wrong security check)

## Unreleased 0.3.8 - 05-12-2018

### Added

- Most dhcp network information is shown in app
- network security + debug opt-outs implemented + observable behaviour

## Unreleased 0.3.7 - 05-12-2018

### Added

- Mostly working CI Testing Environment + Setup + First (Instrumented) Tests
- GUI for connection detail input

## Unreleased 0.3.5 - 27-11-2018

### Added

- WIFI (only!) QR Code Scanner + basic network change
- full rfc 1213 oid catalog tree view added
- WifiManagerKlasse
- SNMP v1 und v3 Adapter und Verbindungsklassen

### Changed

- Several UI changes and design of drawer layout