package de.uni_stuttgart.informatik.sopra.sopraapp.query.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * representing enumeration node in oid_catalog.json
 */
public class EnumerationConstraint {
    @JsonProperty("enumeration")
    private List<String> enumeration;
}
