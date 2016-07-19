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
    
    private static final String MEASUREMENT_MOISTURE = "moisture";
    private static final String MEASUREMENT_TANK = "tank";
    private static final String MEASUREMENT_PUMP = "pump";
    private static final String MEASUREMENT_VALVE = "valve";
    
    private static final String TAG_NAME = "name";
    private static final String FIELD_VALUE = "value";
    
    private final InfluxDB influxDB;

    public Store() {
        log.info("Store connecting to " + server);
        
        // TODO make configurable
        influxDB = InfluxDBFactory.connect(server, "root", "root");
        influxDB.createDatabase(dbName);
    }
    
    public void save(SensorData sensorData) {
        
        BatchPoints batchPoints = BatchPoints.database(dbName).build();

        long currentTimeMillis = System.currentTimeMillis();
        for (Entry<String, Double> entry : sensorData.moisture.entrySet()) {
            Point point = Point.measurement(MEASUREMENT_MOISTURE)
                                .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                                .tag(TAG_NAME, entry.getKey())
                                .addField(FIELD_VALUE, entry.getValue())
                                .build();
            batchPoints.point(point);
        }
        for (Entry<String, Double> entry : sensorData.tanks.entrySet()) {
            Point point = Point.measurement(MEASUREMENT_TANK)
                                .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                                .tag(TAG_NAME, entry.getKey())
                                .addField(FIELD_VALUE, entry.getValue())
                                .build();
            batchPoints.point(point);
        }
        for (Entry<String, Boolean> entry : sensorData.pumps.entrySet()) {
            Point point = Point.measurement(MEASUREMENT_PUMP)
                                .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                                .tag(TAG_NAME, entry.getKey())
                                .addField(FIELD_VALUE, entry.getValue())
                                .build();
            batchPoints.point(point);
        }
        for (Entry<String, Boolean> entry : sensorData.valves.entrySet()) {
            Point point = Point.measurement(MEASUREMENT_VALVE)
                                .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                                .tag(TAG_NAME, entry.getKey())
                                .addField(FIELD_VALUE, entry.getValue())
                                .build();
            batchPoints.point(point);
        }

        influxDB.write(batchPoints);
    }
}
