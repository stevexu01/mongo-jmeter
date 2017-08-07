package com.bell.arc.usageaggregator.jmeter;

import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
		defaultParameters.addArgument(JMETER_PARAM_RABBIT_EXCHANGE, "arc");
		defaultParameters.addArgument(JMETER_PARAM_RABBIT_BINDKEY, "usageAggregator");
		defaultParameters.addArgument(JMETER_PARAM_RABBIT_MESSAGE, "Test message");
		
		return defaultParameters;
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		String textMessage = context.getParameter(JMETER_PARAM_RABBIT_MESSAGE);
		textMessage = textMessage.isEmpty()?"Test message from JMeter":textMessage;
		
		org.springframework.amqp.core.Message message = org.springframework.amqp.core.MessageBuilder
				.withBody(textMessage.getBytes()).build();
		
		SampleResult sampleResult = new SampleResult();
		sampleResult.sampleStart();
		
		rabbitTemplate(context.getParameter(JMETER_PARAM_RABBIT_HOST), 
				Integer.valueOf(context.getParameter(JMETER_PARAM_RABBIT_PORT)),
				context.getParameter(JMETER_PARAM_RABBIT_EXCHANGE),
				context.getParameter(JMETER_PARAM_RABBIT_BINDKEY)).send(message);
		
		getNewLogger().debug(">> Messag " + textMessage + " sent to Rabbit.");
		
		sampleResult.setSamplerData(textMessage);
		sampleResult.setSuccessful(true);
		sampleResult.sampleEnd();

		return sampleResult;
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
