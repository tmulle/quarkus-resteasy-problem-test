package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.logging.Logger;

import io.quarkiverse.resteasy.problem.HttpProblem;
import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * REST client to the backend service
 */
@RegisterRestClient(baseUri = "http://localhost:12000/server")
public interface MyRemoteService {

    
    
    @POST
    @Path("/notFound")
    void notFound();

    @POST
    @Path("/badRequest")
    void badReqeust();

    @GET
    @Path("/hello")
    String getGreeting();

    // Uncomment this to have it rebuild the error message
    // and at least return a full json response
    // but the details section is the full message string of the incoming
    // HttpProblem response. Not really what I want
    // @ClientExceptionMapper
    // static RuntimeException toException(Response response) {

    //     // REbuild the response
    //     HttpProblem problem = HttpProblem.builder()
    //     .withStatus(response.getStatus())
    //     .withDetail(response.readEntity(String.class))
    //     .withTitle(response.getStatusInfo().getReasonPhrase())
    //         .build();


    //     System.out.println("Got a client error " + response.readEntity(String.class));

    //     return problem;
        
    // }
    
}
