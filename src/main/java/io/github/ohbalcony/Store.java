package io.github.ohbalcony;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.ohbalcony.model.ControllerState;
import io.github.ohbalcony.model.Instructions;
import io.github.ohbalcony.model.SensorData;

@Service
public class Store {
    
    private static final Logger log = LoggerFactory.getLogger(Store.class);
    
    // TODO make configurable
    private static final String server = "http://gerty:8086";
    private static final String dbName = "ohdb";
    
    private static final String MEASUREMENT_MOISTURE = "moisture";
    private static final String MEASUREMENT_TEMPERATURE = "temperature";
    private static final String MEASUREMENT_TANK = "tank";
    private static final String MEASUREMENT_PUMP = "pump";
    private static final String MEASUREMENT_VALVE = "valve";
    
    private static final String MEASUREMENT_GENERAL = "measure";

    private static final String TAG_NAME = "name";
    private static final String TAG_CONTROLLER_ID = "controller";
    
    private static final String FIELD_VALUE = "value";
    
    private InfluxDB influxDB;
    
    @PostConstruct
    public void init() {
        log.info("Store connecting to " + server);
        
        // TODO make configurable
        influxDB = InfluxDBFactory.connect(server, "root", "root");
        influxDB.createDatabase(dbName);
    }
    
    public void save(SensorData sensorData, Instructions instructions) {
        
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
        for (Entry<String, Double> entry : sensorData.temperature.entrySet()) {
            Point point = Point.measurement(MEASUREMENT_TEMPERATURE)
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
        for (Entry<String, Boolean> entry : instructions.pumps.entrySet()) {
            Point point = Point.measurement(MEASUREMENT_PUMP)
                                .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                                .tag(TAG_NAME, entry.getKey())
                                .addField(FIELD_VALUE, booleanToInteger(entry.getValue()))
                                .build();
            batchPoints.point(point);
        }
        for (Entry<String, Boolean> entry : instructions.valves.entrySet()) {
            Point point = Point.measurement(MEASUREMENT_VALVE)
                                .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                                .tag(TAG_NAME, entry.getKey())
                                .addField(FIELD_VALUE, booleanToInteger(entry.getValue()))
                                .build();
            batchPoints.point(point);
        }

        influxDB.write(batchPoints);
    }
    
    public double getCurrentTankLevel(String tankName)
    {
        String q = "SELECT " + FIELD_VALUE + 
                   " FROM " + MEASUREMENT_TANK +
                   " WHERE \"" + TAG_NAME + "\" = '" + removeQuotes(tankName) + "'" +
                   " ORDER BY time DESC" +
                   " LIMIT 1";
        
        double defaultValue = 0.0; // if no results, assume tank empty
        return querySingleDoubleValue(q, defaultValue);
    }

    private double querySingleDoubleValue(String q, double defaultValue) {
        Query query = new Query(q, dbName);
        QueryResult result = influxDB.query(query);
        
        List<Result> results = result.getResults();
        if(results == null || results.size() == 0) {
            return defaultValue;
        }
            
        Result firstResult = results.get(0);
        List<Series> series = firstResult.getSeries();
        if(series == null || series.size() == 0) {
            return defaultValue;
        }
            
        Series firstSeries = series.get(0);
        
        List<Object> values = firstSeries.getValues().get(0);
        double value = (double)values.get(1);

        return value;
    }

    // basic prevention of Query Language Injection
    private String removeQuotes(String s) {
        return s.replace("\"", "").replace("'", "");
    }

    private int booleanToInteger(boolean value) {
        return value ? 1 : 0;
    }

    public void save(ControllerState state) {

        String controllerId = Objects.requireNonNull(state.controllerId);
        
        // TODO currently only used for Aquarium (which actually has nothing to do with Oh, Balcony)
        BatchPoints batchPoints = BatchPoints.database("home").build();

        long currentTimeMillis = System.currentTimeMillis();

        for (Entry<String, Double> entry : state.values.entrySet()) {
            
            Point point = Point.measurement(MEASUREMENT_GENERAL)
                    .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                    .tag(TAG_CONTROLLER_ID, controllerId)
                    .tag(TAG_NAME, entry.getKey())
                    .addField(FIELD_VALUE, entry.getValue()).build();
            batchPoints.point(point);
        }

        influxDB.write(batchPoints);
    }
}
