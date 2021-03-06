package de.uni_stuttgart.informatik.sopra.sopraapp.snmp.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * representing "nadr" object of device qr code
 * using jackson databind 2 annotations
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "IPv4",
        "IPv6"
})
public class QrCodeAddressPart {
    @JsonProperty("IPv4")
    private String iPv4;
    @JsonProperty("IPv6")
    private String iPv6;

    @JsonProperty("IPv4")
    public String getIPv4() {
        return iPv4;
    }

    @JsonProperty("IPv4")
    public void setIPv4(String iPv4) {
        this.iPv4 = iPv4;
    }

    @JsonProperty("IPv6")
    public String getIPv6() {
        return iPv6;
    }

    @JsonProperty("IPv6")
    public void setIPv6(String iPv6) {
        this.iPv6 = iPv6;
    }

    @Override
    public String toString() {
        return "QrCodeAddressPart{" +
                "iPv4='" + iPv4 + '\'' +
                ", iPv6='" + iPv6 + '\'' +
                '}';
    }
}
