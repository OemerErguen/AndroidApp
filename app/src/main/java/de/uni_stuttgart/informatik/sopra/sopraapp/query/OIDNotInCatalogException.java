package de.uni_stuttgart.informatik.sopra.sopraapp.query;

/**
 * exception to handle event if we have no asn name for an oid in catalog
 */
public class OIDNotInCatalogException extends Exception {
    public OIDNotInCatalogException() {
    }

    public OIDNotInCatalogException(String message) {
        super(message);
    }

    public OIDNotInCatalogException(String message, Throwable cause) {
        super(message, cause);
    }

    public OIDNotInCatalogException(Throwable cause) {
        super(cause);
    }
}