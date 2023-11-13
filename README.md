# aggregation-app

This service provides ability to fetch users from different data sources.

## Getting Started

To run the service you will need _Java 17+_. For local deployment, you can start the database in docker container by running:

` docker-compose up`

Since the service uses Maven, it can be built and run with the following command:

` mvn package && java -jar target/aggregation-app-0.0.1-SNAPSHOT.jar`

## Built With

* [Spring Boot](https://start.spring.io/)
* [Maven](https://maven.apache.org/) - Dependency Management