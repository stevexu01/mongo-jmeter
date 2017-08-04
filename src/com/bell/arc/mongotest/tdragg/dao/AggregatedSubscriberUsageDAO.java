package com.bell.arc.mongotest.tdragg.dao;

import com.bell.arc.mongotest.tdragg.model.AggregatedSubscriberUsageDTO;
import java.util.List;

/**
 * Created by maxchlam on 7/26/17.
 */
public interface AggregatedSubscriberUsageDAO {

    List<AggregatedSubscriberUsageDTO> aggregateDataUsageBySubscriber(Long ban, String usageType);
}
