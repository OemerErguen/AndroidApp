package de.uni_stuttgart.informatik.sopra.sopraapp.query;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents generic list queries
 */
public interface ListQuery extends SnmpQuery {

    public abstract List<QueryResponse> getListItems();
}
