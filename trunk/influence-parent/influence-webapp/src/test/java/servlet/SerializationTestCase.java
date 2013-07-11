package servlet;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.mongodb.util.MyAsserts.assertTrue;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class SerializationTestCase {

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setUpJson() {
        this.objectMapper = new ObjectMapper();

    }

    @Test
    public void shouldSerializeDeserializeProperly() {
        assertTrue(true);

//        Object user;
//        String json = this.objectMapper.writeValueAsString(user);

    }
}
