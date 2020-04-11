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

### Swagger Base URL

If configured URL redirection (e.g. with nginx), it might be useful to change base URL:

```properties
swagger.api.base-url=[HOSTNAME|IP]/path/to/api
```

## Running Server

Start the server using command:

```shell script
java -jar home-monitoring-server-[VERSION].jar
```

## Running Server On System Boot

To start the server on boot and stop it on shutdown,
create three files (run `chmod +x` on each script):

### start-server.sh

This script starts the server and saves the logs to a file.

```shell script
#!/bin/bash

WORKING_DIR="$(dirname "$0")"
cd $WORKING_DIR

java -jar 'home-monitoring-server-[VERSION].jar' &
```

### stop-server.sh

This script stops the server.

Instead of `8081`, use the port your server runs on.

```shell script
#!/bin/bash
fuser 8081/tcp -k -TERM || true
```

### /etc/init.d/home-monitoring-server

This script defines how to start and stop the server.

Use `sudo` when creating/editing/changing access permissions for the script.

```shell script
#!/bin/sh
# /etc/init.d/home-monitoring-server
### BEGIN INIT INFO
# Provides: home-monitoring-server
# Required-Start: $local_fs $network
# Required-Stop: $local_fs $network
# Default-Start: 2 3 4 5
# Default-Stop: 0 1 6
# Short-Description: Home Monitoring Server
# Description: Home Monitoring RESTful Server
### END INIT INFO

SCRIPTS_PATH=/path/to/scripts

case $1 in
    start)
        /bin/bash ${SCRIPTS_PATH}/start-server.sh
    ;;
    stop)
        /bin/bash ${SCRIPTS_PATH}/stop-server.sh  
    ;;
    restart)
        /bin/bash ${SCRIPTS_PATH}/stop-server.sh
        /bin/bash ${SCRIPTS_PATH}/start-server.sh
    ;;
esac
exit 0
```

### Init Script Configuration

To run the script automatically on system boot, execute commands:

```shell script
sudo update-rc.d home-monitoring-server defaults
sudo update-rc.d home-monitoring-server enable
```
