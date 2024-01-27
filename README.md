# Sample Spring 3 app with OAuth2's PKCE Authorization flow in Kotlin

This is an excerpt from one of my side projects. It's a simple but feature-rich CRUD API for managing employees and companies. Feel free to use it or any part of it. Reasons you might find it useful:
* Spring documentation for how to use it with the latest Kotlin version is a bit outdated.
* Spring 3 being relatively new, many concerns aren't properly addressed in the documentation.
* A custom implementation of OAuth2's PKCE flow requires a lot of experimenting and fine-tuning.
* There are some cool and useful [features](#features) which work nicely together, which makes this repo a great place to see how it all blends together.
* It's a simple demo of how to deploy a Docker container locally as well as remotely.
* It demonstrates a useful _GitHub Workflow_ that builds and publishes Docker images to your _ghcr.io_ repository.




When running for the first time, run from project root:  
`docker compose -f docker-compose.yml up --build `

This repo includes Maven wrappers, so you can also build and run the project with Maven without downloading it.  
Access either module and run the following (resource-server depends on the authorization-server being live):

`./mvnw dependency:resolve` run before only when running for the first time   
`./mvnw spring-boot:run` for all subsequent runs.

The app is configured to run with MySQL, changing that is a matter of updating the config files, dockerfiles as well as the drivers.

If you wish to add a user with username: _user_ and password: _pass_ with no restrictions and under the assumption that your database name is _tulip_, run the following query before authenticating:


```
INSERT INTO tulip.user (id, tenant, email, password, role, rights)
VALUES (1, '', 'user', '$2a$12$nQOQdWWBbJyPApU/9/3HzehQu4vI.c6mNcDQ.I9ubx27Evt0gy3gK', 'USER', 2147483647);
```

#### Features:
* Auth2 PKCE Authorization flow
* Access token customization
* Custom (and cool) authorization system for endpoint access based on token details
* Database versioning
* Support for multi tenancy through DB design and Hibernate's Tenant support
* OpenAPI documented APIs Swagger
* Easily run through the `docker-compose` command
* Logger configured
* Specify build target and environment through env variables

**Swagger documentation**: {server:port}/swagger-ui/index.html

### Notes:
I have provided a login page for this example but be mindful not to use it for any other than educational purposes as the project is still underway.
Let me know if you need help, have any comments or if you found any of this helpful :)
