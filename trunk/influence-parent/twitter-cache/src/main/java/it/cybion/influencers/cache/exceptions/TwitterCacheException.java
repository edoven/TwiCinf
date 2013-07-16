package it.cybion.influencers.cache.exceptions;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class TwitterCacheException extends Exception {

    public TwitterCacheException() {

    }

    public TwitterCacheException(String message) {

        super(message);
    }

    public TwitterCacheException(String message, Throwable cause) {

        super(message, cause);
    }
}
