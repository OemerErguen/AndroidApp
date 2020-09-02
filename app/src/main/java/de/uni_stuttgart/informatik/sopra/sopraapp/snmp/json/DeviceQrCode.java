package de.uni_stuttgart.informatik.sopra.sopraapp.snmp.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * this class represents a device qr code and is used by jackson json databind to encode qr code input
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user",
        "pw",
        "enc",
        "naddr"
})
public class DeviceQrCode {

    @JsonProperty("user")
    private String user;
    @JsonProperty("pw")
    private String pw;
    @JsonProperty("enc")
    private String enc;
    @JsonProperty("naddr")
    private QrCodeAddressPart naddr;

    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty("pw")
    public String getPw() {
        return pw;
    }

    @JsonProperty("pw")
    public void setPw(String pw) {
        this.pw = pw;
    }

    @JsonProperty("enc")
    public String getEnc() {
        return enc;
    }

    @JsonProperty("enc")
    public void setEnc(String enc) {
        this.enc = enc;
    }

    @JsonProperty("naddr")
    public QrCodeAddressPart getNaddr() {
        return naddr;
    }

    @JsonProperty("naddr")
    public void setNaddr(QrCodeAddressPart naddr) {
        this.naddr = naddr;
    }

    public boolean hasIpv4Port() {
        return naddr.getIPv4().contains(":");
    }

    public boolean hasIpv6Port() {
        //TODO
        return false;
    }

    public int getPortv4() {
        if(hasIpv4Port()){
            return Integer.parseInt(naddr.getIPv4().split(":")[1]);
        }
        return 161;
    }

    @Override
    public String toString() {
        return "DeviceQrCode{" +
                "user='" + user + '\'' +
                ", pw='" + pw + '\'' +
                ", enc='" + enc + '\'' +
                ", naddr=" + naddr +
                '}';
    }
}

