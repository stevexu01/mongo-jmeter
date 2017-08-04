package com.bell.arc.mongotest.tdragg;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.bell.arc.mongotest.tdragg.model.AggregatedSubscriberUsageDTO;
import com.bell.arc.mongotest.tdragg.model.DataUsageDocument;
import com.bell.arc.mongotest.tdragg.util.DateUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import java.io.Serializable;
import java.util.List;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * Created by zhifenxu on 8/3/2017.
 */
public class TDRAggregatorTest extends AbstractJavaSamplerClient implements Serializable {
	private static final String JMETER_PARAM_MONGO_HOST = "MONGODB_HOST";
	private static final String JMETER_PARAM_MONGO_PORT = "MONGODB_PORT";
	private static final String JMETER_PARAM_MONGO_USER = "MONGODB_USER";
	private static final String JMETER_PARAM_MONGO_PASSWORD = "MONGODB_PASSWORD";

	private static final String JMETER_PARAM_MONGO_DATABASE = "MONGODB_DATABASE";
	private static final String JMETER_PARAM_MONGO_COLLECTION = "MONGODB_COLLECTION";

	private static final String JMETER_PARAM_MONGO_TDR_BAN = "MONGODB_TDR_BAN";
	private static final String JMETER_PARAM_MONGO_TDR_USAGE_TYPE = "MONGODB_TDR_USAGE_TYPE";

	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument(JMETER_PARAM_MONGO_HOST, "localhost");
		defaultParameters.addArgument(JMETER_PARAM_MONGO_PORT, "27017");
		// defaultParameters.addArgument(JMETER_PARAM_MONGO_USER, "guest");
		// defaultParameters.addArgument(JMETER_PARAM_MONGO_PASSWORD, "guest");

		defaultParameters.addArgument(JMETER_PARAM_MONGO_DATABASE, "usage");
		defaultParameters.addArgument(JMETER_PARAM_MONGO_COLLECTION, "data_usage");

		defaultParameters.addArgument(JMETER_PARAM_MONGO_TDR_BAN, "");
		defaultParameters.addArgument(JMETER_PARAM_MONGO_TDR_USAGE_TYPE, "");

		return defaultParameters;
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		final String mongoHost = context.getParameter(JMETER_PARAM_MONGO_HOST);
		final String mongoPort = context.getParameter(JMETER_PARAM_MONGO_PORT);
		// final String username =
		// context.getParameter(JMETER_PARAM_MONGO_USER);
		// final String password =
		// context.getParameter(JMETER_PARAM_MONGO_PASSWORD);

		final String databaseName = context.getParameter(JMETER_PARAM_MONGO_DATABASE);
		final String collectionName = context.getParameter(JMETER_PARAM_MONGO_COLLECTION);

		String ban = context.getParameter(JMETER_PARAM_MONGO_TDR_BAN);
		final String usageType = context.getParameter(JMETER_PARAM_MONGO_TDR_USAGE_TYPE);

		SampleResult sampleResult = new SampleResult();

		// BAN variable passed in from test context in JMeter
		JMeterContext jmeterContext = JMeterContextService.getContext();
		final String varBan = jmeterContext.getVariables().get("ban");
		if (getNewLogger().isInfoEnabled()) {
			getNewLogger().info("BAN passed from context: " + varBan);
		}

		if (!StringUtils.isEmpty(varBan)) {
			ban = varBan;
		}

		if (getNewLogger().isWarnEnabled()) {
			if (StringUtils.isEmpty(ban)) {
				getNewLogger().warn(">> BAN must not be empty.");
				return sampleResult;
			}
			if (StringUtils.isEmpty(usageType)) {
				getNewLogger().warn(">> Usage type must not be empty.");
				return sampleResult;
			}
		}

		sampleResult.sampleStart();

		List<AggregatedSubscriberUsageDTO> subscriberUsageDto = aggregateDataUsageBySubscriber(mongoHost,
				Integer.valueOf(mongoPort), databaseName, Long.valueOf(ban), usageType);

		if (!subscriberUsageDto.isEmpty()) {
			for (AggregatedSubscriberUsageDTO dto : subscriberUsageDto) {
				getNewLogger().info(
						">> MSISDN, bytes in, bytes out, bytes total: " + dto.getTdrMSISDN() + "," + dto.getBytesInTotal() + ","
								+ dto.getBytesOutTotal() + "," + (dto.getBytesInTotal() + dto.getBytesOutTotal()));
			}
		} else {
			getNewLogger().warn(">> TDR aggregator return empty result.");
		}

		sampleResult.sampleEnd();
		sampleResult.setSuccessful(true);

		if (getNewLogger().isInfoEnabled()) {
			getNewLogger().info(">> sample result: " + sampleResult);
		}

		return sampleResult;
	}

	private List<AggregatedSubscriberUsageDTO> aggregateDataUsageBySubscriber(String mongoHost, int mongoPort,
			String databaseName, Long ban, String usageType) {
		Long firstOfMonth = DateUtil.getFirstDateOfMonth();
		Long lastOfMonth = DateUtil.getLastDateOfMonth();

		Aggregation agg = newAggregation(
				match(Criteria.where("tdrBAN").is(ban).andOperator(Criteria.where("tdrUsageType").is(usageType),
						Criteria.where("tdrRoamingIndicator").in("H", "C"),
						Criteria.where("tdrEventStartTime").lte(lastOfMonth),
						Criteria.where("tdrEventStartTime").gte(firstOfMonth))),
				group("tdrMSISDN").sum("tdrBytesIn").as("bytesInTotal").sum("tdrBytesOut").as("bytesOutTotal"),
				project("bytesInTotal", "bytesOutTotal").and("tdrMSISDN").previousOperation());

		// Convert the aggregation result into a List
		AggregationResults<AggregatedSubscriberUsageDTO> groupResults = new MongoTemplate(
				mongoClient(mongoHost, mongoPort), databaseName).aggregate(agg, DataUsageDocument.class,
						AggregatedSubscriberUsageDTO.class);
		List<AggregatedSubscriberUsageDTO> result = groupResults.getMappedResults();

		return result;
	}

	private static MongoClient mongoClient(String mongoHost, int mongoPort) {
		MongoClientOptions options = MongoClientOptions.builder()
				// .socketTimeout(socketTimeout)
				// .connectTimeout(connectionTimeout)
				// .serverSelectionTimeout(serverSelectionTimeout)

				.build();
		//
		// List<MongoCredential> creds = new ArrayList<MongoCredential>();
		// creds.add(MongoCredential.createCredential(username, databaseName,
		// password.toCharArray()));
		//
		return new MongoClient(new ServerAddress(mongoHost, mongoPort), /* creds, */options);
	}
}
