package com.github.devmix.sample.exi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * @author Sergey Grachev
 */
@XmlRootElement(name = "sensor")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SensorType")
public class Sensor {

    @XmlElement(name = "id", required = true)
    private String id;

    @XmlElement(name = "temperature", required = true)
    private Double temperature;

    @XmlElement(name = "timestamp", required = true)
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + id + '\'' +
                ", temperature=" + temperature +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
