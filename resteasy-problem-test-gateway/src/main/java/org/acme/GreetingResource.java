package org.acme;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Gateway which will call a remote endpoint and
 * return the results
 */
@Path("/gateway")
public class GreetingResource {

    @Inject
    @RestClient
    MyRemoteService remoteService;

    /**
     * Call the remote service which will return a good response
     * 
     * @return
     */
    @Path("/hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return remoteService.getGreeting();
    }

    /**
     * Call the remote service which will throw a NotFound 
     * error during processing simulating a record not being
     * found
     */
    @Path("/notFound")
    @POST
    public void notFound() {
        remoteService.notFound();
    }
    
}
