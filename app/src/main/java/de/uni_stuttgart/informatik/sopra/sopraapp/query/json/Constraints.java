package de.uni_stuttgart.informatik.sopra.sopraapp.query.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * representing constraints node in oid_catalog.json
 */
@JsonIgnoreProperties
public class Constraints {
    @JsonProperty("range")
    private SizeConstraint[] range;

    @JsonProperty("size")
    private SizeConstraint[] size;

    @JsonProperty("enumeration")
    private EnumerationConstraint enumeration;

    public Constraints() {
    }

    @JsonProperty("range")
    public SizeConstraint[] getRange() {
        return range;
    }

    @JsonProperty("range")
    public void setRange(SizeConstraint[] range) {
        this.range = range;
    }

    @JsonProperty("size")
    public SizeConstraint[] getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(SizeConstraint[] size) {
        this.size = size;
    }

    @JsonProperty("enumeration")
    public EnumerationConstraint getEnumeration() {
        return enumeration;
    }

    @JsonProperty("enumeration")
    public void setEnumeration(EnumerationConstraint enumeration) {
        this.enumeration = enumeration;
    }
}
