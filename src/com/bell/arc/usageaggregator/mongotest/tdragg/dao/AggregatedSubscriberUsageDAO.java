package com.bell.arc.usageaggregator.mongotest.tdragg.dao;

import java.util.List;

import com.bell.arc.usageaggregator.mongotest.tdragg.model.AggregatedSubscriberUsageDTO;

/**
 * Created by maxchlam on 7/26/17.
 */
public interface AggregatedSubscriberUsageDAO {

    List<AggregatedSubscriberUsageDTO> aggregateDataUsageBySubscriber(Long ban, String usageType);
}
