package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

public class NoDeviceException extends RuntimeException {

    public NoDeviceException() {
        super();
    }

    public NoDeviceException(String message) {
        super(message);
    }

    public NoDeviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDeviceException(Throwable cause) {
        super(cause);
    }
}
