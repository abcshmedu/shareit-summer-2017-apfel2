/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import edu.hm.lipptobusch.shareit.businessLayer.MediaService;
import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceImpl;

/**
 * Context Listener to enable usage of google guice together with jersey.
 *
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class ShareitServletContextListener extends GuiceServletContextListener {

    private static final Injector injector = Guice.createInjector(new ServletModule() {
        @Override
        protected void configureServlets() {
            bind(MediaService.class).to(MediaServiceImpl.class);
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
    static Injector getInjectorInstance() {
        return injector;
    }
}
