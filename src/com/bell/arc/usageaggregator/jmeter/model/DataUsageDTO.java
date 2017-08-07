package com.bell.arc.usageaggregator.jmeter.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE,
    setterVisibility=JsonAutoDetect.Visibility.NONE, creatorVisibility= JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataUsageDTO {

    private String xCorrelationId;
    private Long triggerEventTimestamp;
    private Long ban;

    public DataUsageDTO() {
    }

    private Double banLvlCost;
    private Double banLvlCostRounded;
    private List<Subscriber> subscribers;

    public String getxCorrelationId() {
        return xCorrelationId;
    }

    @Override
    public String toString() {
        return "DataUsageDTO{" +
            "xCorrelationId='" + xCorrelationId + '\'' +
            ", triggerEventTimestamp=" + triggerEventTimestamp +
            ", ban=" + ban +
            ", banLvlCost=" + banLvlCost +
            ", banLvlCostRounded=" + banLvlCostRounded +
            '}';
    }

    public DataUsageDTO(String xCorrelationId, Long triggerEventTimestamp, Long ban, List<Subscriber> subscribers) {
        this.xCorrelationId = xCorrelationId;
        this.triggerEventTimestamp = triggerEventTimestamp;
        this.ban = ban;
        this.subscribers = subscribers;
    }
    public void setxCorrelationId(String xCorrelationId) {
        this.xCorrelationId = xCorrelationId;
    }

    public Long getTriggerEventTimestamp() {
        return triggerEventTimestamp;
    }

    public void setTriggerEventTimestamp(Long triggerEventTimestamp) {
        this.triggerEventTimestamp = triggerEventTimestamp;
    }

    public Long getBan() {
        return ban;
    }

    public void setBan(Long ban) {
        this.ban = ban;
    }

    public Double getBanLvlCost() {
        return banLvlCost;
    }

    public void setBanLvlCost(Double banLvlCost) {
        this.banLvlCost = banLvlCost;
    }

    public Double getBanLvlCostRounded() {
        return banLvlCostRounded;
    }

    public void setBanLvlCostRounded(Double banLvlCostRounded) {
        this.banLvlCostRounded = banLvlCostRounded;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public static class Subscriber{
        private Long msisdn;
        private Long totalBytes;
        public Subscriber(Long msisdn, Long totalBytes, Double cost) {
            this.msisdn = msisdn;
            this.totalBytes = totalBytes;
            this.cost = cost;
        }

        public Subscriber() {
        }

        private Double cost;

        public Long getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(Long msisdn) {
            this.msisdn = msisdn;
        }

        public Long getTotalBytes() {
            return totalBytes;
        }

        public void setTotalBytes(Long totalBytes) {
            this.totalBytes = totalBytes;
        }

        public Double getCost() {
            return cost;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }
    }


}
