package com.adm.config.initializers;

import com.adm.config.RootConfig;
import com.adm.config.WebConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by Milan_Verheij on 15-08-16.
 *
 * Dispatcher Servlet uit Spring boek pg 134
 */
public class HarrieWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private String uploadFolder = "c:/harrie/uploads/"; // Windows
//    private String uploadFolder = "/tmp/harrie/uploads"; // Unix-Based


    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    // Set active profile

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
//        servletContext.setInitParameter("spring.profiles.active", "development");
//        servletContext.setInitParameter("spring.profiles.active", "qa");
        servletContext.setInitParameter("spring.profiles.active", "production");
    }

    // Multi-part file upload
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(
                new MultipartConfigElement(uploadFolder, 20197152, 4194304, 0));
    }
}
