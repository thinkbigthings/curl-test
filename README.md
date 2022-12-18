# curl-test
test curl on spring security 6

Requires Java 17

run `gradlew bootRun`

# Without preflight

This authenticates and returns 200 but does not create a session.
Note in the logs "Failed to create a session, as response has been committed. 
Unable to store SecurityContext."

    `curl -kv --user admin:admin http://localhost:9000/login`

# Preflight

Here, the first call creates an anonymous session and returns 401 (no credentials were passed)
then passing the session id back on the second call associates that session with the username.
It seems like session creation only works if we make a preflight request

    `curl -kv -b cookies.txt -c cookies.txt -X OPTIONS http://localhost:9000/login`
    `curl -kv -b cookies.txt -c cookies.txt --user admin:admin http://localhost:9000/login`
    `cat cookies.txt`

We can configure Spring Security with `SessionCreationPolicy.IF_REQUIRED`
in which case that first call will work as expected 
(authenticating and creating a session with a single call).
But that is supposed to be the default policy anyway
so it doesn't make sense that setting to IF_REQUIRED changes the behavior.