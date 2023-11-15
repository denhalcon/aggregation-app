# aggregation-app

This service provides ability to fetch users from different data sources. Supported db providers are 
PostgreSQL and MySQL. 

The example for data source configuration:

```yaml
data-sources:
    - name: database-1
      strategy: postgres
      url: jdbc:postgresql://localhost:5432/agg_app_db
      table: users
      user: test_user
      password: test_pass
      mapping:
          id: user_id
          username: login
          name: first_name
          surname: last_name
```

Where mapping is the mapping of the db table to the `User` entity:
```java
public record User(String id, String username, String name, String surname){}
```

## Getting Started

To run the service you will need _Java 17+_. For local deployment, you can start MySQL and PostgreSQL db instances in docker containers by running:

` docker-compose up`

Since the service uses Maven, it can be built and run with the following command:

` mvn package && java -jar target/aggregation-app-0.0.1-SNAPSHOT.jar`