package it.cybion.influencers.cache.persistance.exceptions;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class PersistenceFacadeException extends Exception {

    public PersistenceFacadeException() {

    }

    public PersistenceFacadeException(String message) {

        super(message);
    }

    public PersistenceFacadeException(String message, Throwable cause) {

        super(message, cause);
    }
}
