package com.bell.arc.mongotest.tdragg.dao;

import com.bell.arc.mongotest.tdragg.model.DataUsageDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface DataUsageDocumentRepository extends MongoRepository<DataUsageDocument, Long> {
    //    @Query(value="{'BAN':?0}",fields = "{'TransactionId':1,'ComponentId':1,'TransactDateTime':1,TransactType':1,'TransactDescription':1,'APN':1,'ServiceId':1,'RatingGroup':1,'NAG':1,'BAN':1,'MSISDN':1,'SGSNIP':0,'IMSI':0,'IMEI':0,'ESN':0,'MEID':0,'EventStartTime':1,'EventEndTime':1,'TerminationCause':0,'RoamingIndicator':0,'SubscriberType':0,'SubscriberLookup':0,'UsageType':1,'NAI':0,'BytesIn':1,'BytesOut':1,'Duration':0,'ChargeAmount':0,'HOMEID':0,'DOMAIN':0,'X-Correlation-ID':1}")
    @Query(value="{'tdrBAN':?0}")
    List<DataUsageDocument> findByTdrBAN(Long ban);
}
