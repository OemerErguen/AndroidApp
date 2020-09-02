package de.uni_stuttgart.informatik.sopra.sopraapp.query.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * representing a size constraint in oid_catalog.json
 */
public class SizeConstraint {

    @JsonProperty("min")
    private long min;

    @JsonProperty("max")
    private long max;

    @JsonProperty("min")
    public long getMin() {
        return min;
    }

    @JsonProperty("min")
    public void setMin(long min) {
        this.min = min;
    }

    @JsonProperty("max")
    public long getMax() {
        return max;
    }

    @JsonProperty("max")
    public void setMax(long max) {
        this.max = max;
    }
}
