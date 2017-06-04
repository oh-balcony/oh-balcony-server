# oh-balcony-server
Server for [Oh, Balcony!](http://oh-balcony.github.io/)

It is developed using the [Spring Boot](https://projects.spring.io/spring-boot/) Framework and stores sensor data in the time-series database [InfluxDB](https://www.influxdata.com/time-series-platform/influxdb/). Visualization of the stored sensor data can be done using [Grafana](http://grafana.org/), and soon hopefully also using the [Web User Interface](https://github.com/oh-balcony/oh-balcony-web), which is under development.

## Prerequisites

- Install Java 8
- Install [Maven 3](https://maven.apache.org/)
- Install and start InfluxDB by following the [Installation instructions](https://docs.influxdata.com/influxdb/latest/introduction/installation/) (Tested with version 1.2, but newer versions should work too)

Clone this repository:

    git clone https://github.com/oh-balcony/oh-balcony-server.git


## Building

Build and run in development mode:

    mvn spring-boot:run
    
Build for production mode:

    mvn clean install
    
Run in production mode:

    java -jar target/oh-balcony-server-*.jar

## RESTful API

After starting the Web Service have a look at the Swagger UI to see available REST endpoints:

http://localhost:8080/swagger-ui.html

## Installation as a service

Refer to the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-systemd-service):

Create a file called `/etc/systemd/system/oh-balcony.service`:

```
[Unit]
Description=oh-balcony-service
After=influxd.service

[Service]
User=pi
ExecStart=/home/pi/software/oh-balcony-server/target/oh-balcony-server-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

Adapt `User`, `ExecStart` as needed. Note that it is configured to start after the InfluxDB service.

Startup the service now:

    sudo systemctl start oh-balcony.service

Enable automatic service startup (after reboot):

    sudo systemctl enable oh-balcony.service

To follow the log file after installing as a systemd service:

    sudo journalctl -u oh-balcony.service -f
