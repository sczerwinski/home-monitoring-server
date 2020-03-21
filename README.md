![Full Build](https://github.com/sczerwinski/home-monitoring-server/workflows/Full%20Build/badge.svg)

# Home Monitoring – Server

## Server Configuration

Create file `application.properties` to set up server port and context path:

```properties
server.port=8081
server.servlet.context-path=/api
```

### H2

With no additional configuration, the server will connect to H2 memory database.

### Postgres

To use PostgreSQL database, add the following lines to `application.properties` file:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/[DB_NAME]
spring.datasource.username=[DB_USERNAME]
spring.datasource.password=[DB_PASSWORD]
spring.datasource.driverClassName=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create
```

## Running Server

Start the server using command:

```shell script
java -jar home-monitoring-server-[VERSION].jar
```
