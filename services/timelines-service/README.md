# Timelines Service

Handles timeline assembly and read-optimized view for users.

## Running the Service

To run this service, you have to have installed:
- Java JDK 21
- Docker

Also, to execute this service you need to run platform infrastructure.

### Infrastructure

To run this service, you need to start infrastructure using docker compose. \
This will run PostgreSQL database, Kafka, Redis and Cassandra instances.

Go to microblogging-platform\infra\microblogging-platform-local\ folder

To run infrastructure:

```
docker compose -f docker-compose.infra.yml up -d
```

To stop infrastructure:

```
docker compose -f docker-compose.infra.yml down
```

### Start Timelines Service locally using IntelliJ IDEA

After cloning the repo, open it in IntelliJ IDEA and you will see all run configuration available in the **Run/Debug Configurations** dropdown. \

To build the service, go to microblogging-platform\services\timelines-service\ and run:
```
mvnw clean package
```
This will build the service and run unit tests.

Then, you can run **timelines-service** to execute this service.

### Start Timelines Service using Docker compose

Go to microblogging-platform\services\timelines-service\

To run service:

```
docker compose -f docker-compose.timelines.yml up --build
```
To stop service:

```
docker compose -f docker-compose.timelines.yml down
```