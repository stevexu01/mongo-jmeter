package com.bell.arc.usageaggregator.mongotest.tdragg.model;

/**
 * Created by maxchlam on 7/26/17.
 */
public class AggregatedSubscriberUsageDTO {

    private Long bytesInTotal;
    private Long bytesOutTotal;
    private String tdrMSISDN;

    public Long getBytesInTotal() {
        return bytesInTotal;
    }

    public void setBytesInTotal(Long bytesInTotal) {
        this.bytesInTotal = bytesInTotal;
    }

    public Long getBytesOutTotal() {
        return bytesOutTotal;
    }

    public void setBytesOutTotal(Long bytesOutTotal) {
        this.bytesOutTotal = bytesOutTotal;
    }

    public String getTdrMSISDN() {
        return tdrMSISDN;
    }

    public void setTdrMSISDN(String tdrMSISDN) {
        this.tdrMSISDN = tdrMSISDN;
    }
}
