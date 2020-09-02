package de.uni_stuttgart.informatik.sopra.sopraapp.query;

import org.snmp4j.smi.OID;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;

/**
 * general interface which needs to be implemented by each query class
 *
 * @param <T>
 */
public interface QueryRequest<T extends SnmpQuery> {
    /**
     * a {@link DeviceConfiguration} instance
     *
     * @return
     */
    public DeviceConfiguration getDeviceConfiguration();

    /**
     * this query decides if we use #querySingle or #queryWalk to retrieve results
     *
     * @return
     */
    public boolean isSingleRequest();

    /**
     * the oid which is requested
     *
     * @return
     */
    public OID getOidQuery();

    /**
     * the concrete {@link SnmpQuery} implementation class
     * TODO use already defined generic instead of this method
     *
     * @return
     */
    public Class<T> getQueryClass();


    /**
     * indicates wheter this query should be kept in cache or not
     *
     * @return
     */
    public default boolean isCacheable() {
        return true;
    }

    /**
     * used to identify this query in a cache
     *
     * @return
     */
    public default String getCacheId() {
        return this.getClass().getSimpleName() + getDeviceConfiguration().getUniqueDeviceId();
    }
}
