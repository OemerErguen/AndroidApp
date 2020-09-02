package de.uni_stuttgart.informatik.sopra.sopraapp.query;


import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * each snmp query class must implement this interface
 */
public interface SnmpQuery {

    /**
     * you get the results into this method and have to process/your query class with the data you want
     *
     * @param results
     */
    public void processResult(List<QueryResponse> results);
}
