# curl-test

This is a sample demo to test sessions on spring security 6
intended to demonstrate the spring security issue 
https://github.com/spring-projects/spring-security/issues/12431


Requires Java 17

run `gradlew bootRun`

# Without preflight

This authenticates and returns 200 but does not create a session.
Note in the logs "Failed to create a session, as response has been committed. 
Unable to store SecurityContext."

    curl -kv --user admin:admin http://localhost:9000/login

# With preflight

Here, the first call creates an anonymous session and returns 401 (no credentials were passed)
then passing the session id back on the second call associates that session with the username.
It seems like out of the box authenticated session creation only works if we make a preflight request

    curl -kv -b cookies.txt -c cookies.txt -X OPTIONS http://localhost:9000/login
    curl -kv -b cookies.txt -c cookies.txt --user admin:admin http://localhost:9000/login
    cat cookies.txt

# SessionCreationPolicy

SessionCreationPolicy seems to be `null` by default. 
We can toggle the line of code in this project's `WebSecurityConfig` to test this.
If we step into the configuration we see that the policy is `null`.

We can configure Spring Security with `SessionCreationPolicy.IF_REQUIRED` in 
which case that first call will work as expected (authenticating and creating 
a session with a single call). That is supposed to be the default policy 
anyway given top hits on Google, but I could not find Spring Security docs that
specifically declared the default value.

# The Problem

The out-of-the-box behavior is that unauthenticated calls trigger session creation
but authenticated calls do NOT trigger session creation. This seems backwards.

An attacker may potentially consume a lot of server memory 
via sending malicious http requests to create an unbounded number of sessions, 
so there is potential for a DoS attack. 

# The Solution

I suggest two things: 
that the default session creation policy be IF_REQUIRED instead of null,
and that unauthenticated calls should not have a session created.

