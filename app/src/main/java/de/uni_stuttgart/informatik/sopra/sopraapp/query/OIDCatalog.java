package de.uni_stuttgart.informatik.sopra.sopraapp.query;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.json.JsonCatalogItem;

/**
 * central singleton class to manage oid catalog items read by a json file
 */
public class OIDCatalog {
    public static final String TAG = OIDCatalog.class.getName();
    private static OIDCatalog instance;
    private final Context context;
    // oid to catalog item
    private ConcurrentHashMap<String, JsonCatalogItem> mapOidKey = new ConcurrentHashMap<>();
    // asn to catalog item
    private ConcurrentHashMap<String, JsonCatalogItem> mapAsnKey = new ConcurrentHashMap<>();

    private OIDCatalog(Context context) {
        this.context = context;
        initData();
    }

    /**
     * singleton access method
     *
     * @param context
     * @return
     */
    public static OIDCatalog getInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new IllegalArgumentException("null context given");
            }
            instance = new OIDCatalog(context);
        }
        return instance;
    }

    /**
     * load data from file into internal catalog
     */
    private void initData() {
        Log.d(TAG, "reading oid catalog file into catalog");
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try (BufferedInputStream resourceAsStream
                     = new BufferedInputStream(context.getResources().openRawResource(R.raw.oid_catalog))) {
            Map<String, JsonCatalogItem> map
                    = om.readValue(resourceAsStream, new TypeReference<Map<String, JsonCatalogItem>>() {
            });
            for (JsonCatalogItem ci : map.values()) {
                mapOidKey.put(ci.getOid(), ci);
                mapAsnKey.put(ci.getName(), ci);
            }
            Log.d(TAG, "filled oid catalog with " + mapAsnKey.size() + " values");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
        }
    }

    public ConcurrentHashMap<String, JsonCatalogItem> getMapOidKey() {
        return mapOidKey;
    }

    public ConcurrentHashMap<String, JsonCatalogItem> getMapAsnKey() {
        return mapAsnKey;
    }

    /**
     * get asn name of an oid and strip last number (usually index) of oid for catalog lookup
     *
     * @param oid
     * @return
     */
    public String getAsnByOidStripLast(String oid) throws OIDNotInCatalogException {
        String key = oid.substring(0, oid.lastIndexOf('.'));
        if (!mapOidKey.containsKey(key)) {
            // try to strip last 4 numbers (ip addr index of snmp)
            // TODO this is not elegant here!
            key = oid.substring(0, oid.lastIndexOf('.'));
            key = oid.substring(0, key.lastIndexOf('.'));
            if (mapOidKey.containsKey(key)) {
                return mapOidKey.get(key).getName();
            }
            key = oid.substring(0, key.lastIndexOf('.'));
            key = oid.substring(0, key.lastIndexOf('.'));
            if (!mapOidKey.containsKey(key)) {
                key = oid.substring(0, key.lastIndexOf('.'));
            }
            if (!mapOidKey.containsKey(key)) {
                throw new OIDNotInCatalogException("could not find oid: " + key);
            }
        }
        JsonCatalogItem jsonCatalogItem = mapOidKey.get(key);
        return jsonCatalogItem.getName();
    }

    /**
     * get oid by asn name
     *
     * @param asnName
     * @return
     */
    public String getOidByAsn(String asnName) {
        if (mapAsnKey.containsKey(asnName)) {
            return mapAsnKey.get(asnName).getOid();
        }
        return null;
    }
}
