package de.uni_stuttgart.informatik.sopra.sopraapp.query;

import org.snmp4j.smi.OID;

import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * table query interface to map fields one by one to a data structure
 */
public interface TableQuery extends SnmpQuery {
    /**
     * define columns as map
     * @return
     */
    public Map<String, OID> getColumnDefinition();

    /**
     * get header columns for table header
     *
     * @return
     */
    public String[] getHeaderColumns();

    /**
     * get processed content
     *
     * @return
     */
    public Map<String, Map<String, QueryResponse>> getContent();

    /**
     * method to generate a row title out of row data
     *
     * @param singleRow
     * @param index
     * @return
     */
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index);
}
