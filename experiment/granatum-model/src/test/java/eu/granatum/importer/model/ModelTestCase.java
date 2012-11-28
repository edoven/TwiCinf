package eu.granatum.importer.model;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import eu.granatum.importer.model.relations.Relationships;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Transaction;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ModelTestCase extends InMemoryNeo4jGraphDbServiceProvider {

    private Graph graph;

    private static final Logger logger = Logger.getLogger(ModelTestCase.class);

    @BeforeClass
    public void setUp() {
        this.graph = new Neo4jGraph(super.gdbService);
    }

    @Test
    public void shouldTestRelationships() {
        logger.info("populating users");

        UserVertex user1 = null;
        UserVertex user2 = null;

        Transaction tx = gdbService.beginTx();

        try {
            // populate Users
            Vertex u1 = graph.addVertex(null);
            user1 = new UserVertex(1, "marko", u1);

            Vertex u2 = graph.addVertex(null);
            user2 = new UserVertex(2, "peter", u2);

            //and their mutual relations
            graph.addEdge(null, user1.getUnderlyingVertex(), user2.getUnderlyingVertex(),
                    Relationships.User.hasCollaborator);
            graph.addEdge(null, user2.getUnderlyingVertex(), user1.getUnderlyingVertex(),
                    Relationships.User.hasCollaborator);

            tx.success();

        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.finish();
        }

        logger.info("testing properties");
        assertEquals(user1.getSocialNetwork().size(), 1);
        assertEquals(user2.getSocialNetwork().size(), 1);

        assertEquals(user1.getId(), 1);
        assertEquals(user1.getName(), "marko");

        assertEquals(user2.getId(), 2);
        assertEquals(user2.getName(), "peter");
    }
}
