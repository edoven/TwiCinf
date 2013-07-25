package it.cybion.influencers.cache.web.exceptions;

public class ProtectedUserException extends UserHandlerException
{
    private static final long serialVersionUID = 5703709181538677912L;

    public ProtectedUserException(String message) {

        super(message);
    }

    public ProtectedUserException(String message, Throwable cause) {

        super(message, cause);
    }
}
