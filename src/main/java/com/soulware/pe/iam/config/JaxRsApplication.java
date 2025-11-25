package com.soulware.pe.iam.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class JaxRsApplication extends Application {
    // JAX-RS will automatically discover and register all @Path annotated classes
}
