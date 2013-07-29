package it.cybion.influencers.cache.web.exceptions;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class UserHandlerException extends Exception {

    public UserHandlerException(String message) {

        super(message);
    }

    public UserHandlerException(String message, Throwable cause) {

        super(message, cause);
    }
}
