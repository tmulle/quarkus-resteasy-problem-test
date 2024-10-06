package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.resteasy.problem.HttpProblem;
import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
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
    void badRequest();

    @GET
    @Path("/hello")
    String getGreeting();

    // Uncomment this to have it rebuild the error message
    // and at least return a full json response
    // but the details section is the full message string of the incoming
    // HttpProblem response. Not really what I want
    @ClientExceptionMapper
    static RuntimeException toException(Response response) {

        // Read the custom media type (if any)
        MediaType mediaType = response.getMediaType();

        // If we have a HttpProblem type response

        if (mediaType.equals(HttpProblem.MEDIA_TYPE)) {
            System.out.println("We have an HttpProblem format");


            // This would normally be passed into us by Quarkus
            ObjectMapper mapper = new ObjectMapper();

            // Holds the original
            HttpProblemRaw parsedOriginal;
            
            try {

                // Read the original HttpProblem
                // Normally this would be the real instance but it uses a Builder
                // and isn't parseable by Jackson at the moment
                parsedOriginal = mapper.readValue(response.readEntity(String.class), HttpProblemRaw.class);

                // Build the new message with the original fields
                HttpProblem newProblem = HttpProblem.builder()
                .withDetail(parsedOriginal.getDetail())
                .withInstance(parsedOriginal.getInstance())
                .withStatus(parsedOriginal.getStatusCode())
                .withTitle(parsedOriginal.getTitle())
                .withType(parsedOriginal.getType())
                .build();

                return newProblem;
            } catch (Exception e) {
                e.printStackTrace();
                return new RuntimeException(e);
            }
        }

        // This would be the normal handling of NON HttpProblem formatted messages
        switch (response.getStatus()) {
            case 404: return new NotFoundException(response);
            case 400: return new BadRequestException(response);
            default: return new ServerErrorException(response);
        }
    }
}
