# oh-balcony-server
Web Service for [Oh, Balcony!](http://oh-balcony.github.io/)

It is developed using the [Spring Boot](https://projects.spring.io/spring-boot/) Framework and stores sensor data in the time-series database [InfluxDB](https://www.influxdata.com/time-series-platform/influxdb/). Visualization of the stored sensor data can be done using [Grafana](http://grafana.org/).

## Building

- Install Java 8
- Install [Maven 3](https://maven.apache.org/)

Clone the repository:

    git clone https://github.com/oh-balcony/oh-balcony-server.git

Build and run in development mode:

    mvn spring-boot:run
