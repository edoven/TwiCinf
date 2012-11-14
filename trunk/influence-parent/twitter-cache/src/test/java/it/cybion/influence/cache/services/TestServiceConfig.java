package it.cybion.influence.cache.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class TestServiceConfig extends GuiceServletContextListener {

    private static final Logger logger = Logger.getLogger(TestServiceConfig.class);

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //teardown stuff
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector( new TestJerseyServletModule() );
    }
}
