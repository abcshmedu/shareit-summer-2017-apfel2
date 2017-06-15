/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.resource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import edu.hm.lipptobusch.shareit.businessLayer.MediaService;
import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceImpl;
import edu.hm.lipptobusch.shareit.filter.AuthenticationService;
import edu.hm.lipptobusch.shareit.filter.OAuthServiceCaller;
import edu.hm.lipptobusch.shareit.persistence.HibernatePersistence;
import edu.hm.lipptobusch.shareit.persistence.HibernatePersistenceImpl;

/**
 * Context Listener to enable usage of google guice together with jersey.
 *
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-06-08
 */
public class ShareitServletContextListener extends GuiceServletContextListener {

    private static final Injector injector = Guice.createInjector(new ServletModule() {
        @Override
        protected void configureServlets() {
            bind(MediaService.class).to(MediaServiceImpl.class);
            bind(AuthenticationService.class).to(OAuthServiceCaller.class);
            bind(HibernatePersistence.class).to(HibernatePersistenceImpl.class);
            //bind(MediaPersistence.class).to(MediaPersistenceImpl.class);
        }
    });

    @Override
    protected Injector getInjector() {
        return injector;
    }

    /**
    * This method is only required for the HK2-Guice-Bridge in the
    * Application class.
    * @return Injector instance.
    */
    public static Injector getInjectorInstance() {
        return injector;
    }
}
