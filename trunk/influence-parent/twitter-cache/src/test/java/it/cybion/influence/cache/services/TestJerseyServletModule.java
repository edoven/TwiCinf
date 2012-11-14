package it.cybion.influence.cache.services;

import com.google.inject.Scopes;
import com.google.inject.name.Names;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class TestJerseyServletModule extends JerseyServletModule {

    private static final Logger logger = Logger.getLogger(TestJerseyServletModule.class);

    @Override
    protected void configureServlets() {
        Map<String, String> initParams = new HashMap<String, String>();
        // TODO check if jersey wadl can be configured here
        // initParams.put(PackagesResourceConfig.PROPERTY_PACKAGES,
        // "eu.granatum.wp5.services");

        initParams.put(ServletContainer.RESOURCE_CONFIG_CLASS,
                ClasspathResourceConfig.class.getName());

//        Properties properties = PropertiesHelper
//                .readFromClasspath("/wp5services.properties");
        // binds the keynames as @Named annotations
//        Names.bindProperties(binder(), properties);

        // bind REST services
        bind(StatusService.class);

        // add bindings for Jackson json serialization
        bind(JacksonJaxbJsonProvider.class).asEagerSingleton();
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
        // Route all requests through GuiceContainer
        serve("/*").with(GuiceContainer.class);
        filter("/*").through(GuiceContainer.class, initParams);
        logger.debug("configured servlets module for tests");
    }
}
