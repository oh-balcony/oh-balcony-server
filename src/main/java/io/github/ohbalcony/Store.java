package io.github.ohbalcony;

import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Store {
    
    private Logger log = LoggerFactory.getLogger(Store.class);
    
    // TODO make configurable
    private static final String server = "http://lavendulus:8086";
    private static final String dbName = "ohdb_dev";
    
    private final InfluxDB influxDB;

    public Store() {
        log.info("Store connecting to " + server);
        
        // TODO make configurable
        influxDB = InfluxDBFactory.connect(server, "root", "root");
        influxDB.createDatabase(dbName);
    }
    
    public void save(SensorData sensorData) {
        
        BatchPoints batchPoints = BatchPoints.database(dbName).build();

        for (Entry<String, Double> entry : sensorData.sensorData.entrySet()) {
            
            // TODO do we want to use timestamp from raspberry pi or from server?
            
            Point point = Point.measurement("moisture")
                                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                                .tag("sensor", entry.getKey())
                                .addField("value", entry.getValue())
                                .build();
            batchPoints.point(point);
        }

        influxDB.write(batchPoints);
    }
}
