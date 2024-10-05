# Issue
I'm seeing the JSON output from resteasy-problem being truncated in the scenario when I make a rest call from one service to another service and that service throws an error (404 in this case).


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

