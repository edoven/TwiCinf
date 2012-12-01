package it.cybion.influence.graph;

import com.tinkerpop.blueprints.Graph;
import it.cybion.influence.model.User;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public interface UsersGraphFactory {
    Graph createGraph() throws GraphCreationException;

    //TODO: indexes...
    //TODO: public is for test..this has to be private
    boolean containsUser(User user);
}
