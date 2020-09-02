package de.uni_stuttgart.informatik.sopra.sopraapp.query;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * abstract class for list query
 */
public abstract class AbstractSnmpListQuery implements ListQuery {
    private List<QueryResponse> listItems = new ArrayList<>();

    @Override
    public List<QueryResponse> getListItems() {
        return listItems;
    }

    @Override
    public void processResult(List<QueryResponse> results) {
        listItems = results;
    }
}
