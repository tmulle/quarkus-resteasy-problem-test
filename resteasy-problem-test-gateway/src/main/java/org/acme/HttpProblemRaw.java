package org.acme;

import java.net.URI;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for the HttpProblem format when being returned from
 * a remote server
 * 
 * Normally you'd use the HttpProblem class itself but
 * it is a Builder pattern and not setup to work with Jackson
 * so this was just a test to prove my theory
 */
public class HttpProblemRaw {

    private URI type;
    private String title;

    @JsonProperty(value = "status")
    private int statusCode;

    private String detail;
    private URI instance;
    private Map<String, Object> parameters;
    private Map<String, Object> headers;

    public HttpProblemRaw() {
    }

    public URI getType() {
        return type;
    }

    public void setType(URI type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public URI getInstance() {
        return instance;
    }

    public void setInstance(URI instance) {
        this.instance = instance;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    

    
    
}
