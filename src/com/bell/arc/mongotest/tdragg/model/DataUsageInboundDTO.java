package com.bell.arc.mongotest.tdragg.model;

/**
 * Created by maxchlam on 7/26/17.
 */
public class DataUsageInboundDTO {

    private Long ban;

    private Long triggerEventTimestamp;

    private String xCorrelationId;

    public Long getBan() {
        return ban;
    }

    public void setBan(Long ban) {
        this.ban = ban;
    }

    public Long getTriggerEventTimestamp() {
        return triggerEventTimestamp;
    }

    public void setTriggerEventTimestamp(Long triggerEventTimestamp) {
        this.triggerEventTimestamp = triggerEventTimestamp;
    }

    public String getxCorrelationId() {
        return xCorrelationId;
    }

    public void setxCorrelationId(String xCorrelationId) {
        this.xCorrelationId = xCorrelationId;
    }
}
