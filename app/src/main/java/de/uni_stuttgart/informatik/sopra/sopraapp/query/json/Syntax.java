package de.uni_stuttgart.informatik.sopra.sopraapp.query.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * representing a syntax node in oid_catalog.json
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "class"
})
@JsonIgnoreProperties
public class Syntax {

    @JsonProperty("type")
    private String type;
    @JsonProperty("class")
    private String _class;

    @JsonIgnore
    @JsonProperty("constraints")
    private Constraints constraints;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("class")
    public String getClass_() {
        return _class;
    }

    @JsonProperty("class")
    public void setClass_(String _class) {
        this._class = _class;
    }

    @JsonProperty("constraints")
    public Constraints getConstraints() {
        return constraints;
    }

    @JsonProperty("constraints")
    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }
}