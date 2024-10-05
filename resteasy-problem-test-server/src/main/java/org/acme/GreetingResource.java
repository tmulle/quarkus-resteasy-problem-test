package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Simulates a backend service
 */
@Path("/server")
public class GreetingResource {

    /**
     * Return a good response
     * 
     * @return
     */
    @Path("/hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    /**
     * Simulate a POST that fails and throws a not found
     * exception which could represent a missing entity, etc
     */
    @Path("/notFound")
    @POST
    public void throwNowFound() {
        throw new NotFoundException();
    }
}
