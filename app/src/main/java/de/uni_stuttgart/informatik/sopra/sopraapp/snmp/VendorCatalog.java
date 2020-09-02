package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * This Singleton class manages vendor addresses
 */
public class VendorCatalog {

    private Map<Integer, String> vendorItems = new HashMap<>();
    private static final String TAG = VendorCatalog.class.getName();
    private static VendorCatalog instance;
    private final Context context;
    private final String oidPrefix = "1.3.6.1.4.1.";

    // constructor of VendorCatalog
    private VendorCatalog(Context context) {
        this.context = context;
        macScanner();
    }

    // accessor method
    public static VendorCatalog getInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new IllegalArgumentException("no null context allowed");
            }
            instance = new VendorCatalog(context);
        }
        return instance;
    }

    // scanner for enterprise_numbers
    // put the first split value in key and the second split value in value to the hashMap
    private void macScanner() {
        try(Scanner sc = new Scanner(new BufferedInputStream(context.getResources()
                .openRawResource(R.raw.enterprise_numbers))).useDelimiter(";")) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] split = line.split(";", 2);
                if (split.length != 2) {
                    Log.d(TAG, "line " + line + " skipped");
                    continue;
                }
                int key = Integer.parseInt(split[0]);
                String value = split[1];
                vendorItems.put(key, value);
            }
        }
        Log.d(TAG, "final map size: " + vendorItems.size());
    }

    // assign vendor name to ID
    public String getVendorById(String id) {
        String vendor = null;
        try {
            int parseInt = Integer.parseInt(id);
            vendor = getVendorById(parseInt);
        } catch (NumberFormatException e) {
        }
        return vendor;
    }
    // check key in hashMap and get the value from it.
    private String getVendorById(int id) {
        if (vendorItems.containsKey(id)) {
            return vendorItems.get(id);
        }
        return null;
    }

    /*
     Replace the OID Prefix with empty String and leave the ID
     ID assigned to vendor look above
      */
    public String getVendorByOID(String oid) {
        if(oid == null){
            return null;
        }
        if (oid.startsWith(oidPrefix)) {
            String vendor = oid.replace(oidPrefix, "");
            if (vendor.contains(".")) {
                // with suffix
                String firstPart = vendor.split("\\.")[0];
                return getVendorById(firstPart);
            }
            return getVendorById(vendor);
        }
        return null;
    }
}