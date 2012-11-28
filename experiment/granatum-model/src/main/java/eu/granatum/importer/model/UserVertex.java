package eu.granatum.importer.model;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import eu.granatum.importer.model.relations.Relationships;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class UserVertex implements User {
	
    public static final String CLASS_NAME = "User";

    public class Properties {
        public static final String id = "uuid";
        public static final String name = "name";
        public static final String vertexClass = "vertexClass";
    }

    private final Vertex underlyingVertex;

    public UserVertex(final Vertex vertex) {
        this.underlyingVertex = vertex;
    }

    public UserVertex(int id, String name, Vertex vertex) {
        this.underlyingVertex = vertex;
        this.underlyingVertex.setProperty(Properties.id, id);
        this.underlyingVertex.setProperty(Properties.name, name);
        this.underlyingVertex.setProperty(Properties.vertexClass, CLASS_NAME);
    }

    public Vertex getUnderlyingVertex() {
        return this.underlyingVertex;
    }

    @Override
    public int getId() {
        return (Integer) underlyingVertex.getProperty(Properties.id);
    }

    @Override
    public String getName() {
        return this.underlyingVertex.getProperty(Properties.name).toString();
    }

    public String getVertexClass() {
        return (String) underlyingVertex.getProperty(Properties.vertexClass);
    }

    @Override
    public List<User> getSocialNetwork() {
        Iterator<Edge> iterator = underlyingVertex.getEdges(
                Direction.OUT,
                Relationships.User.hasCollaborator).iterator();
        List<User> users = new ArrayList<User>();
        User currentUser;
        Vertex currentVertex;

        while (iterator.hasNext()) {
            currentVertex = iterator.next().getVertex(Direction.IN);
            currentUser = new UserVertex(currentVertex);
            users.add(currentUser);
        }
        return users;
    }
}
