package com.bell.arc.mail;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;

public class MailSender extends AbstractJavaSamplerClient implements Serializable {

  private static final long serialVersionUID = 1L;
  final static private Logger logger = Logger.getAnonymousLogger();

  @Override
  public Arguments getDefaultParameters() {
    Arguments defaultParameters = new Arguments();
    //defaultParameters.addArgument("SMTP_SECTYPE", "SSL");
    defaultParameters.addArgument("SMTP_HOST", "smtp.gmail.com");
    defaultParameters.addArgument("SMTP_PORT", "465");
    defaultParameters.addArgument("SMTP_USERNAME", "");
    defaultParameters.addArgument("SMTP_PASSWORD", "");
    defaultParameters.addArgument("SUBJECT", "");
    defaultParameters.addArgument("BODY", "");

    defaultParameters.addArgument("RESP_CODE_EXPECTED", "200");
    defaultParameters.addArgument("RESP_MAX_TIMEMILLIS", "1000");  //10 seconds

    return defaultParameters;
  }

  @Override
  public SampleResult runTest(JavaSamplerContext context) {
    //message from jmeter for target http request sampler
    JMeterContext jmeterContext = JMeterContextService.getContext();
    final String targetProtocol = jmeterContext.getVariables().get("TARGET_PROTOCOL");
    final String targetHost = jmeterContext.getVariables().get("TARGET_HOST");
    final String targetPort = jmeterContext.getVariables().get("TARGET_PORT");
    final String targetPath = jmeterContext.getVariables().get("TARGET_PATH");
    final StringBuffer targetSb = new StringBuffer().append(targetProtocol)
        .append("://")
        .append(targetHost)
        .append(":")
        .append(targetPort)
        .append("/")
        .append(targetPath);

    final String responseCode = jmeterContext.getVariables().get("targePageRespCode");
    final String startTime = jmeterContext.getVariables().get("startTimeMillis");
    final String endTime = jmeterContext.getVariables().get("endTimeMillis");

    long startTimeMillis = 0l;
    long endTimeMillis = 0l;
    try {
      startTimeMillis = Long.valueOf(startTime);
      endTimeMillis = Long.valueOf(endTime);
    } catch (NumberFormatException e) {
      logger.log(Level.WARNING, "start/endtime was not recorded in sampler run.", e);
    }

    final long targeResponseTimeMillis = (endTimeMillis - startTimeMillis);

    logger.info(">>> target page: " + targetSb.toString());
    logger.info(">>> response code set in beanshell for target http sampler: " + responseCode);
    logger.info(">>> start time (millis) set in beanshell for target http sampler: " + startTime);
    logger.info(">>> end time (millis) set in beanshell for target http sampler: " + endTime);
    logger.info(">>> time taken by targe http request(milliseconds): " + targeResponseTimeMillis);

    // pull parameters
    //String secType = context.getParameter( "SMTP_SECTYPE" );
    final String host = context.getParameter("SMTP_HOST");
    final String port = context.getParameter("SMTP_PORT");
    final String username = context.getParameter("SMTP_USERNAME");
    final String password = context.getParameter("SMTP_PASSWORD");

    final String responseCodeExpected = context.getParameter("RESP_CODE_EXPECTED");
    final String responseTimeExpected = context.getParameter("RESP_MAX_TIMEMILLIS");
    long responseTimeMillisExpected = -1l;
    try {
      responseTimeMillisExpected = Long.valueOf(responseTimeExpected);
    } catch (NumberFormatException e) {
      logger.info(">>> response max time not set: " + e.getMessage());
    }

    //
    final String successfulMessage = "Successfully send email to " + username;

    SampleResult result = new SampleResult();
//        if(responseCodeExpected != null && responseCodeExpected.length() > 0){
//        	if(responseCode.equals(responseCodeExpected) /*&& (responseTimeMillisExpected == -1l || targeResponseTimeMillis < responseTimeMillisExpected)*/){
//        		result.setSuccessful( true );
//     	        result.setResponseMessage(successfulMessage );
//     	        result.setResponseData( successfulMessage );
//     	        result.setResponseCodeOK(); // 200 code
//     	        return result;
//        	}
//        }

    String subject = context.getParameter("SUBJECT");
    if (subject == null || subject.length() == 0) {
      subject = responseCode + " - " + targetSb.toString();
    }

    String body = context.getParameter("BODY");
    if (body == null || body.length() == 0) {
      body = "Page response with " + targeResponseTimeMillis + " milliseconds.";
    }

    final SecSendEmail mailSender = new SecSendEmail();
    mailSender.setHost(host);

    //if port is set, then take it over secType
    try {
      final int prt = Integer.parseInt(port);
      mailSender.setPort(prt);
    } catch (NumberFormatException e) {
      logger.log(Level.SEVERE, "smtp (ssl) port invalid.", e);
      throw new RuntimeException(e);
    }

    mailSender.setUsername(username);
    mailSender.setPassword(password);
    mailSender.init();

    result.sampleStart(); // start stopwatch

    try {
      mailSender.sendMessage(subject, body);
      result.sampleEnd(); // stop stopwatch
      result.setSuccessful(true);
      result.setResponseMessage(successfulMessage);
      result.setResponseData(successfulMessage);
      result.setResponseCodeOK(); // 200 code
    } catch (RuntimeException e) {
      logger.log(Level.SEVERE, "send message error.", e);

      java.io.StringWriter stringWriter = new java.io.StringWriter();
      e.printStackTrace(new java.io.PrintWriter(stringWriter));
      result.setResponseData(stringWriter.toString());
      result.setDataType(SampleResult.TEXT);
      result.setResponseCode("500");
    }

    return result;
  }

}
