#!/usr/bin/env bash

# old cmd for rfc1213 mib:
#mibdump.py --destination-format=json --generate-mib-texts --build-index --mib-stub= RFC1213-MIB IP-MIB IP-FORWARD-MIB

mibdump.py --destination-format=json --no-python-compile --generate-mib-texts --build-index --mib-stub= --mib-source=file:///home/worker/workspaces/sopra-team-22/mib/orig/ GENUA-MIB GENUA-RENDEZVOUS-MIB GENUA-SNMPD-CONF OPENBSD-BASE-MIB OPENBSD-CARP-MIB OPENBSD-MEM-MIB OPENBSD-PF-MIB OPENBSD-RELAYD-MIB OPENBSD-SENSORS-MIB RFC1213-MIB