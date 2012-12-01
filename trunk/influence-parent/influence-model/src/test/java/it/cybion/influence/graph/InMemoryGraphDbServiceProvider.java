package it.cybion.influence.graph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class InMemoryGraphDbServiceProvider {

    protected GraphDatabaseService gdbService;

    @BeforeClass
    public void setupAbstract() {
        TestGraphDatabaseFactory factory = new TestGraphDatabaseFactory();
        this.gdbService = factory.newImpermanentDatabaseBuilder().newGraphDatabase();
    }

    @AfterClass
    public void tearDownAbstract() {
        this.gdbService.shutdown();
    }
}
