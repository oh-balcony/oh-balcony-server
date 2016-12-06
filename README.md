# oh-balcony-server
Web Service for [Oh, Balcony!](http://oh-balcony.github.io/)

It is developed using the [Spring Boot](https://projects.spring.io/spring-boot/) Framework and stores sensor data in the time-series database [InfluxDB](https://www.influxdata.com/time-series-platform/influxdb/). Visualization of the stored sensor data can be done using [Grafana](http://grafana.org/).

## Prerequisites

- Install Java 8
- Install [Maven 3](https://maven.apache.org/)
- Install and start InfluxDB by following the [Installation instructions](https://docs.influxdata.com/influxdb/latest/introduction/installation/) (Tested with version 1.1)

Clone this repository:

    git clone https://github.com/oh-balcony/oh-balcony-server.git


## Building

Build and run in development mode:

    mvn spring-boot:run

## RESTful API

After starting the Web Service have a look at the Swagger UI to see available REST endpoints:

http://localhost:8080/swagger-ui.html
