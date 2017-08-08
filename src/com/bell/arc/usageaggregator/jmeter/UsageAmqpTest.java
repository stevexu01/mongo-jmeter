package com.bell.arc.usageaggregator.jmeter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by zhifenxu on 8/6/2017.
 */
public class UsageAmqpTest extends AbstractJavaSamplerClient implements Serializable {
	private static final String JMETER_PARAM_RABBIT_HOST = "RABBIT_HOST";
	private static final String JMETER_PARAM_RABBIT_PORT = "RABBIT_PORT";
	private static final String JMETER_PARAM_RABBIT_EXCHANGE = "RABBIT_EXCHANGE";
	private static final String JMETER_PARAM_RABBIT_BINDKEY = "RABBIT_BINDINGKEY";
	private static final String JMETER_PARAM_RABBIT_MESSAGE = "RABBIT_MESSAGE";

	private AnnotationConfigApplicationContext applicationContext;
	
	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument(JMETER_PARAM_RABBIT_HOST, "localhost");
		defaultParameters.addArgument(JMETER_PARAM_RABBIT_PORT, "5672");
		defaultParameters.addArgument(JMETER_PARAM_RABBIT_EXCHANGE, "arc.usage.ingest");
		defaultParameters.addArgument(JMETER_PARAM_RABBIT_BINDKEY, "usageAggregator");
		
		String tdrMessage = "{ \"xCorrelationId\":  \"a3d5b2489ec53943862ab93b62535485baba0d6f\",  \"triggerEventTimestamp\": 20170725150001,  \"ban\": 123456789}";
		defaultParameters.addArgument(JMETER_PARAM_RABBIT_MESSAGE, tdrMessage);
		
		return defaultParameters;
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		String textMessage = context.getParameter(JMETER_PARAM_RABBIT_MESSAGE);
				
		JMeterContext jmeterContext = JMeterContextService.getContext();
		final String varBan = jmeterContext.getVariables().get("ban");
		if (getNewLogger().isInfoEnabled()) {
			getNewLogger().info("BAN passed from context: " + varBan);
		}
		
//		if(!validateMessage(textMessage) && varBan.isEmpty()){
//			throw new RuntimeException("Message entered is invalid and there's no BAN passed from JMeter.");
//		}
//		
//		//Override message entered or default message
//		if(!varBan.isEmpty()){
//			try {
//				final ObjectMapper mapper = new ObjectMapper();
//				Map<String, Object> textMessageRebuilt = mapper.readValue(textMessage, Map.class);
//				textMessageRebuilt.put("ban", varBan);
//				textMessage = mapper.writeValueAsString(textMessageRebuilt);
//			} catch (IOException e) {
//				getNewLogger().error(e.toString());
//			}
//		}
		
		
		org.springframework.amqp.core.Message message = org.springframework.amqp.core.MessageBuilder
				.withBody(textMessage.getBytes()).build();
		
		SampleResult sampleResult = new SampleResult();
		sampleResult.sampleStart();
		
		rabbitTemplate(context.getParameter(JMETER_PARAM_RABBIT_HOST), 
				Integer.valueOf(context.getParameter(JMETER_PARAM_RABBIT_PORT)),
				context.getParameter(JMETER_PARAM_RABBIT_EXCHANGE),
				context.getParameter(JMETER_PARAM_RABBIT_BINDKEY)).send(message);
		
		getNewLogger().info(">> Messag '" + textMessage + "' sent to Rabbit.");
		
		sampleResult.setSamplerData(textMessage);
		sampleResult.setSuccessful(true);
		sampleResult.sampleEnd();

		return sampleResult;
	}

	private boolean validateMessage(String textMessage) {
		// TODO Auto-generated method stub
		return true;
	}

	private PropertySourcesPlaceholderConfigurer propConfig() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[] { new ClassPathResource("application-test.properties") };
		pspc.setLocations(resources);
		pspc.setIgnoreUnresolvablePlaceholders(true);

		return pspc;
	}
	
	private RabbitTemplate rabbitTemplate(String rabbitmqHost, int rabbitmqPort, String exchangeName, String bindingKey) {
		RabbitTemplate r = new RabbitTemplate(connectionFactory(rabbitmqHost, rabbitmqPort));
	    r.setExchange(exchangeName);
	    r.setRoutingKey(bindingKey);
	    
	    return r;
	}

	private ConnectionFactory connectionFactory(String rabbitmqHost, int rabbitmqPort) {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);
		connectionFactory.setPublisherConfirms(true);
		connectionFactory.setPublisherReturns(true);

		return connectionFactory;
	}
}
