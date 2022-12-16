# curl-test
test curl on spring security 6

Requires Java 17

run `gradlew bootRun`

> this authenticates and returns 200 but does not create a session

    `curl -kv --user admin:admin http://localhost:9000/login`

> this creates an anonymous session and returns 401 (no credentials were passed)
> then passing the session id back on the second call associates it with the username.
> It seems like session creation ONLY works if we make a preflight request

    `curl -kv -b cookies.txt -c cookies.txt -X OPTIONS http://localhost:9000/login`
    `curl -kv -b cookies.txt -c cookies.txt --user admin:admin http://localhost:9000/login`
    `cat cookies.txt`

