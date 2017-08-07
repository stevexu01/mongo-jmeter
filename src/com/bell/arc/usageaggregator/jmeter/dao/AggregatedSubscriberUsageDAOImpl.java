package com.bell.arc.usageaggregator.jmeter.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.bell.arc.usageaggregator.jmeter.model.AggregatedSubscriberUsageDTO;
import com.bell.arc.usageaggregator.jmeter.model.DataUsageDocument;

//import com.bell.arc.mongo.tdragg.util.DateUtil;

/**
 * Created by maxchlam on 7/26/17.
 */
@Repository
public class AggregatedSubscriberUsageDAOImpl implements AggregatedSubscriberUsageDAO {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<AggregatedSubscriberUsageDTO> aggregateDataUsageBySubscriber(Long ban, String usageType) {
//        Long firstOfMonth = DateUtil.getFirstDateOfMonth();
//        Long lastOfMonth = DateUtil.getLastDateOfMonth();

        Aggregation agg = newAggregation(
                match(Criteria.where("tdrBAN").is(ban).andOperator(
                        Criteria.where("tdrUsageType").is(usageType),
                        Criteria.where("tdrRoamingIndicator").in("H", "C")
//                        Criteria.where("tdrEventStartTime").lte(lastOfMonth),
//                        Criteria.where("tdrEventStartTime").gte(firstOfMonth)
                )),
                group("tdrMSISDN").sum("tdrBytesIn").as("bytesInTotal").sum("tdrBytesOut").as("bytesOutTotal"),
                project("bytesInTotal", "bytesOutTotal").and("tdrMSISDN").previousOperation()
        );

        //Convert the aggregation result into a List
        AggregationResults<AggregatedSubscriberUsageDTO> groupResults
                = mongoTemplate.aggregate(agg, DataUsageDocument.class, AggregatedSubscriberUsageDTO.class);
        List<AggregatedSubscriberUsageDTO> result = groupResults.getMappedResults();

        return result;
    }
}
