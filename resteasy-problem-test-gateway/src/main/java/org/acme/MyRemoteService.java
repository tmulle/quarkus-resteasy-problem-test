package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 * REST client to the backend service
 */
@RegisterRestClient(baseUri = "http://localhost:12000/server")
public interface MyRemoteService {

    @POST
    @Path("/notFound")
    void notFound();

    @GET
    @Path("/hello")
    String getGreeting();
    
}
