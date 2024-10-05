# Issue

I posted a ticket over at: https://github.com/quarkiverse/quarkus-resteasy-problem/issues/429

Just putting the ticket body here as well...

I ran into an issue when developing a project for our company set to go live in a few days. So I'm hoping this is a simple issue.

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

Here is the output I get from `Insomnium` when looking at the network request. Something looks wrong at the end of the stream.

```
* Preparing request to http://localhost:12001/gateway/notFound
* Current time is 2024-10-05T02:27:16.161Z
* Enable automatic URL encoding
* Using default HTTP version
* Enable timeout of 120000ms
* Enable SSL validation
* Enable cookie sending with jar of 6 cookies
* Found bundle for host localhost: 0x1cd002bb0f30 [serially]
* Can not multiplex, even if we wanted to!
* Re-using existing connection! (#135) with host localhost
* Connected to localhost (127.0.0.1) port 12001 (#135)

> POST /gateway/notFound HTTP/1.1
> Host: localhost:12001
> User-Agent: insomnium/0.2.3-a
> Accept: */*
> Content-Length: 0

* Mark bundle as not supporting multiuse

< HTTP/1.1 404 Not Found
< content-length: 94
< Content-Type: application/problem+json
< Content-Type: application/problem+json


* Received 185 B chunk
* Excess found in a read: excess = 91, size = 94, maxdownload = 94, bytecount = 0
* Closing connection 135
```

For comparison, here is the network request when calling the service directly.

```
* Preparing request to http://localhost:12000/hello/notFound
* Current time is 2024-10-05T03:12:31.574Z
* Enable automatic URL encoding
* Using default HTTP version
* Enable timeout of 120000ms
* Enable SSL validation
* Enable cookie sending with jar of 6 cookies
* Found bundle for host localhost: 0x1cd002bd1c20 [serially]
* Can not multiplex, even if we wanted to!
* Re-using existing connection! (#139) with host localhost
* Connected to localhost (127.0.0.1) port 12000 (#139)

> POST /hello/notFound HTTP/1.1
> Host: localhost:12000
> User-Agent: insomnium/0.2.3-a
> Accept: */*
> Content-Length: 0

* Mark bundle as not supporting multiuse

< HTTP/1.1 404 Not Found
< content-length: 93
< Content-Type: application/problem+json


* Received 93 B chunk
* Connection #139 to host localhost left intact
```

If you need anymore info, let me know..

