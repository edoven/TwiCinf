package it.cybion.influencers.cache.exceptions;

import it.cybion.influencers.cache.web.exceptions.UserHandlerException;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class LimitExceededException extends UserHandlerException {

    public LimitExceededException(String message) {

        super(message);
    }

    public LimitExceededException(String message, Throwable cause) {

        super(message, cause);
    }
}
