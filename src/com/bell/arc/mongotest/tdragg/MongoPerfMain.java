package com.bell.arc.mongotest.tdragg;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.bell.arc.mongotest.tdragg.model.AggregatedSubscriberUsageDTO;
import com.bell.arc.mongotest.tdragg.model.DataUsageDocument;
import com.bell.arc.mongotest.tdragg.util.DateUtil;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Created by zhifenxu on 8/3/2017.
 */
public class MongoPerfMain {

  public static void main(String[] args) {
//    justGo();
    List<AggregatedSubscriberUsageDTO> dtos =  aggregateDataUsageBySubscriber(527646843L, "PPU");
    if(!dtos.isEmpty()) {
      for(AggregatedSubscriberUsageDTO dto: dtos) {
        System.out.println(
            dto.getBytesInTotal() + "," + dto.getBytesOutTotal()); //95295,1062924
      }
    }
  }

  private static void justGo() {
    Block<Document> printBlock = new Block<Document>() {
      @Override
      public void apply(final Document document) {
        System.out.println(document.toJson());
      }
    };

    MongoClient mongoClient = new MongoClient("localhost", 27017);
    MongoDatabase database = mongoClient.getDatabase("usage");
    MongoCollection<Document> collection = database.getCollection("data_usage");

    collection.find().forEach(
        (Block<? super Document>) System.out::println
    );

    AggregateIterable<Document> agg = collection.aggregate(
        Arrays.asList(
            Aggregates.match(Filters.eq("tdrBAN", "517904624"))
            // , Aggregates.group("$tdrMSISDN", Accumulators.sum("tdrBytesIn", 1))
        )
    );
    //.forEach(printBlock);

    System.out.println(agg.first());
  }


  private static MongoClient mongoClient(){
    MongoClientOptions options = MongoClientOptions.builder()
//        .socketTimeout(socketTimeout)
//        .connectTimeout(connectionTimeout)
//        .serverSelectionTimeout(serverSelectionTimeout)

        .build();
//
//        List<MongoCredential> creds = new ArrayList<MongoCredential>();
//        creds.add(MongoCredential.createCredential(username, databaseName, password.toCharArray()));
//
    return new MongoClient(new ServerAddress("localhost",27017), /*creds, */options);
  }

    public static List<AggregatedSubscriberUsageDTO> aggregateDataUsageBySubscriber(Long ban, String usageType) {
      Long firstOfMonth = DateUtil.getFirstDateOfMonth();
      Long lastOfMonth = DateUtil.getLastDateOfMonth();

      Aggregation agg = newAggregation(
          match(Criteria.where("tdrBAN").is(ban).andOperator(
              Criteria.where("tdrUsageType").is(usageType),
              Criteria.where("tdrRoamingIndicator").in("H", "C"),
              Criteria.where("tdrEventStartTime").lte(lastOfMonth),
              Criteria.where("tdrEventStartTime").gte(firstOfMonth)
          )),
          group("tdrMSISDN").sum("tdrBytesIn").as("bytesInTotal").sum("tdrBytesOut").as("bytesOutTotal"),
          project("bytesInTotal", "bytesOutTotal").and("tdrMSISDN").previousOperation()
      );

      //Convert the aggregation result into a List
      AggregationResults<AggregatedSubscriberUsageDTO> groupResults
          = new MongoTemplate(mongoClient(), "usage").aggregate(agg, DataUsageDocument.class, AggregatedSubscriberUsageDTO.class);
      List<AggregatedSubscriberUsageDTO> result = groupResults.getMappedResults();

      return result;
    }

}

/*

collection.aggregate(Arrays.asList(match(eq("author", "Dave")),
                                   group("$customerId", sum("totalQuantity", "$quantity"),
                                                        avg("averageQuantity", "$quantity"))
                                   out("authors")));

 */