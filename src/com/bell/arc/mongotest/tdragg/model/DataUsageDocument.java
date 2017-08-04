package com.bell.arc.mongotest.tdragg.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zhifenxu on 7/24/2017.
 */
@Document(collection = "data_usage")
public class DataUsageDocument {

    private String tdrTransactionId;

    private Long tdrBAN;


/*
    Transaction Id 	 0005-diamproxy.gy.B0033CnBPGV3.bell.ca;385829359;377113422;596cf367-4e02	String
    APN 	 pda2.bell.ca	String
    Rating Group 	220	Number
    BAN 	516145029	Number
    MSISDN 	16046447741	Number
    IMSI 	302610011889351	Number
    Event Start Time 	20170717132703	Number
    Event End Time 	20170717153825	Number
    Roaming Indicator 	 H	Char
    Usage Type 	 PPU	String
    Bytes In 	31559	Number
    Bytes Out 	32110	Number
    Duration 	7882	Number
    X-Correlation-ID		HEX String
*/
    private Long tdrBytesIn;

    private Long tdrBytesOut;

    private String tdrComponentId;

    private Long tdrTransactDateTime;

    private String tdrTransactType;

    private String tdrTransactDescription;

    private String tdrAPN;

    private Long tdrServiceId;

    private Long tdrRatingGroup;

    private String tdrNAG;

    private Long tdrMSISDN;

    private String tdrSGSNIP;

    private Long tdrIMSI;

    private Long tdrIMEI;

    private String tdrESN;

    private String tdrMEID;

    private Long tdrEventStartTime;

    private Long tdrEventEndTime;

    private String tdrTerminationCause;

    private String tdrRoamingIndicator;

    private Long tdrSubscriberType;

    private Long tdrSubscriberLookup;

    private String tdrUsageType;

    private String tdrNAI;

    private Long tdrDuration;

    private Long tdrChargeAmount;

    private Long tdrHomeId;

    private Long tdrDomain;

    private String xCorrelationId;

    public String getTdrTransactionId() {
        return tdrTransactionId;
    }

    public String getTdrComponentId() {
        return tdrComponentId;
    }

    public Long getTdrTransactDateTime() {
        return tdrTransactDateTime;
    }

    public String getTdrTransactType() {
        return tdrTransactType;
    }

    public String getTdrTransactDescription() {
        return tdrTransactDescription;
    }

    public String getTdrAPN() {
        return tdrAPN;
    }

    public Long getTdrServiceId() {
        return tdrServiceId;
    }

    public Long getTdrRatingGroup() {
        return tdrRatingGroup;
    }

    public String getTdrNAG() {
        return tdrNAG;
    }

    public Long getTdrMSISDN() {
        return tdrMSISDN;
    }

    public String getTdrSGSNIP() {
        return tdrSGSNIP;
    }

    public Long getTdrIMSI() {
        return tdrIMSI;
    }

    public Long getTdrIMEI() {
        return tdrIMEI;
    }

    public String getTdrESN() {
        return tdrESN;
    }

    public String getTdrMEID() {
        return tdrMEID;
    }

    public Long getTdrEventStartTime() {
        return tdrEventStartTime;
    }

    public Long getTdrEventEndTime() {
        return tdrEventEndTime;
    }

    public String getTdrTerminationCause() {
        return tdrTerminationCause;
    }

    public String getTdrRoamingIndicator() {
        return tdrRoamingIndicator;
    }

    public Long getTdrSubscriberType() {
        return tdrSubscriberType;
    }

    public Long getTdrSubscriberLookup() {
        return tdrSubscriberLookup;
    }

    public String getTdrUsageType() {
        return tdrUsageType;
    }

    public String getTdrNAI() {
        return tdrNAI;
    }

    public Long getTdrDuration() {
        return tdrDuration;
    }

    public Long getTdrChargeAmount() {
        return tdrChargeAmount;
    }

    public Long getTdrHomeId() {
        return tdrHomeId;
    }

    public Long getTdrDomain() {
        return tdrDomain;
    }

    public String getxCorrelationId() {
        return xCorrelationId;
    }

    public Long getTdrBAN() {
        return tdrBAN;
    }

    public Long getTdrBytesIn() {
        return tdrBytesIn;
    }

    public Long getTdrBytesOut() {
        return tdrBytesOut;
    }

    public void setTdrBytesIn(Long tdrBytesIn) {
        this.tdrBytesIn = tdrBytesIn;
    }

    public void setTdrBytesOut(Long tdrBytesOut) {
        this.tdrBytesOut = tdrBytesOut;
    }
}
