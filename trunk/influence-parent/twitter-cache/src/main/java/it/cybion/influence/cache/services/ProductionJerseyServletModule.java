package it.cybion.influence.cache.services;

import com.sun.jersey.api.core.ClasspathResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ProductionJerseyServletModule extends JerseyServletModule {

    public Properties properties;

    private static final Logger logger = Logger.getLogger(ProductionJerseyServletModule.class);

    @Override
    protected void configureServlets() {
        Map<String, String> initParams = new HashMap<String, String>();

        initParams.put(ServletContainer.RESOURCE_CONFIG_CLASS, ClasspathResourceConfig.class.getName());

//                logger.info("binding properties");
//        Properties properties = PropertiesHelper.readFromClasspath("/twitter-cache-services.properties");
        //binds the keynames as @Named annotations
//        Names.bindProperties(binder(), properties);
//        this.properties = properties;

        //bind REST services
        bind(StatusService.class);

        // add bindings for Jackson
        bind(JacksonJaxbJsonProvider.class).asEagerSingleton();
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
        // Route all requests through GuiceContainer
        serve("/rest/*").with(GuiceContainer.class);
        filter("/rest/*").through(GuiceContainer.class, initParams);
        logger.debug("configured servlets module for production");
    }
}
