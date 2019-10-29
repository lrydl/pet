package com.lry.config;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//
//        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(AppConfig.class);
//        servletContext.addListener(new ContextLoaderListener(rootContext));
//
//        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
//        webContext.register(WebConfig.class);
//        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", new DispatcherServlet(webContext));
//        registration.setLoadOnStartup(1);
//        registration.addMapping("/");
//
//    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {

        return new Class[] {WebConfig.class};
    }


    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
 
}
