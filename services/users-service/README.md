# Users Service

This service Handles user lifecycle and social graph (follow/unfollow).

## Running the Service

To run this service, you have to have installed:
- Java JDK 21
- Docker

Also, to execute this service you need to run platform infrastructure.

### Infrastructure

To run this service, you need to start infrastructure using docker compose. \
This will run PostgreSQL database, Kafka, Redis and Cassandra instances.

Go to microblogging-platform\infra\microblogging-platform-local\ folder and run: 

```
docker compose -f docker-compose.infra.yml up -d
```

To stop infrastructure:

```
docker compose -f docker-compose.infra.yml down
```

### Start Users Service locally using IntelliJ IDEA

After cloning the repo, open it in IntelliJ IDEA and you will see all run configuration available in the **Run/Debug Configurations** dropdown. \

To build the service, go to microblogging-platform\services\users-service\ and run:
```
mvnw clean package
```
This will build the service and run unit tests.

Then, you can run **users-service** to execute this service.

### Start Users Service using Docker compose
   
Go to microblogging-platform\services\users-service\

To run service:

```
docker compose -f docker-compose.users.yml up --build
```

To stop service:

```
docker compose -f docker-compose.users.yml down
```