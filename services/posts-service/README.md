# Posts Service

Handles post creation and storage of user posts.

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

### Build Posts Service locally

To build the service, go to microblogging-platform\services\posts-service\ and run:
```
mvnw clean package
```
This will build the service and run unit tests.


### Start Posts Service using Docker compose

Go to microblogging-platform\services\posts-service\

To run service:

```
docker compose -f docker-compose.posts.yml up --build
```
To stop service:

```
docker compose -f docker-compose.posts.yml down
```

## Testing the Service

When all services are running, you can do REST calls using the postman collection in microblogging-platform\postman folder.