package com.bell.arc.usageaggregator.jmeter.dao;

import java.util.List;

import com.bell.arc.usageaggregator.jmeter.model.AggregatedSubscriberUsageDTO;

/**
 * Created by maxchlam on 7/26/17.
 */
public interface AggregatedSubscriberUsageDAO {

    List<AggregatedSubscriberUsageDTO> aggregateDataUsageBySubscriber(Long ban, String usageType);
}
