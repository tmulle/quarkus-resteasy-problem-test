# Issue
I'm seeing the JSON output from resteasy-problem being truncated in the scenario when I make a rest call from one service to another service and that service throws an error (404 in this case).

For example, I have a reproducer at: https://github.com/tmulle/quarkus-resteasy-problem-test

I have a simulated `gateway` and `server` service in their respective projects.

To Test:

1. Check out project
2. Start both the `server` and `gateway` projects in separate terminals..I started my server on port 12000 and gateway on 12001. I hardcoded the backend in the `RemoteService` class in the gateway project. If you change ports you'll need to modify the code.
3. Send a `POST` using curl or `postman/insomnium` to http://localhost:12000/hello/notFound and observe the correct JSON error  response as shown below. This is hitting the backend `server` service directly.
4. Send a `POST` using curl or `postman/insomnium` to http://localhost:12001/gateway/notFound and observe you get the truncated JSON error message as shown below. This is hitting the `gateway` which then calls the `server` and returns the response.

Correct Response - When direct call to the backend service
```json
{
	"status": 404,
	"title": "Not Found",
	"detail": "HTTP 404 Not Found",
	"instance": "/hello/notFound"
}
```
Truncated Response - When called through gateway (the output is missing the rest of the structure)
```json
{
	"status": 404,
	"title": "Not Found",
	"detail": "Received: 'Not Found, status code 404' when invoki
```

I don't get any other errors so I'm not sure why the json is being truncated. I found this issue because my Javascript web frontend I'm developing which is calling my GATEWAY->REMOTE_SERVICE threw an exception when parsing the JSON error from my Quarkus servers which then lead me to track down the issue to this library or something near it.

# Building
# resteasy-problem-test-gateway

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/resteasy-problem-test-gateway-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST Client ([guide](https://quarkus.io/guides/rest-client)): Call REST services
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- RESTeasy Problem ([guide](https://github.com/quarkiverse/quarkus-resteasy-problem/blob/main/README.md)): Problem Details for HTTP APIs (RFC-7807) implementation for Quarkus / RESTeasy.

## Provided Code

### REST Client

Invoke different services through REST with JSON

[Related guide section...](https://quarkus.io/guides/rest-client)

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
