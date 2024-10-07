package org.acme;

import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.resteasy.problem.HttpProblem;
import io.quarkiverse.resteasy.problem.HttpProblem.Builder;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * Provider which will look for a REST response formatted in the HttpProblem format
 * by looking for the MEDIA-TYPE header.
 * 
 * If it finds it, it will reparse it and rebuild the HttpProblem to rethrow.
 */
@Provider
public class HttpProblemExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    @Inject
    Logger log;

    @Inject
    ObjectMapper mapper;


    @Override
    public RuntimeException toThrowable(Response response) {

       // Read the custom media type (if any)
        MediaType mediaType = response.getMediaType();

        // If we have a HttpProblem type response

        if (mediaType != null && mediaType.equals(HttpProblem.MEDIA_TYPE)) {
            log.infof("We have an HttpProblem format");

            // Holds the original
            HttpProblemRaw parsedOriginal;
            
            try {

                // Read the original HttpProblem
                // Normally this would be the real instance but it uses a Builder
                // and isn't parseable by Jackson at the moment
                parsedOriginal = mapper.readValue(response.readEntity(String.class), HttpProblemRaw.class);
                
                // Build the new message with the original fields
                Builder newProblem = HttpProblem.builder()
                .withDetail(parsedOriginal.getDetail())
                .withInstance(parsedOriginal.getInstance())
                .withStatus(parsedOriginal.getStatusCode())
                .withTitle(parsedOriginal.getTitle())
                .withType(parsedOriginal.getType());
                
                // Add in the headers back in
                // Using for loop to get around lambda effectively final warning
                Map<String, Object>  headers = parsedOriginal.getHeaders();
                if (headers != null) {
                    for (Entry<String,Object> entry : headers.entrySet()) {
                        newProblem.withHeader(entry.getKey(), entry.getValue());
                    }
                }

                // Add params back in
                Map<String, Object>  params = parsedOriginal.getParameters();
                if (params != null) {
                    for (Entry<String,Object> entry : params.entrySet()) {
                        newProblem.with(entry.getKey(), entry.getValue());
                    }
                }

                return newProblem.build();

            } catch (Exception e) {
                log.error("Could not parse HttpProblem message", e);
                return new RuntimeException(e);
            }
        }

        // Let the others handle non-http formats
        return null;
    }

    /**
     * We want this to run first before the other HttpProblem provider 
     */
    @Override
    public int getPriority() {
        return Priorities.USER - 100;
    }
}