package co.com.redhat.integration.consultaSaldoGateway;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = IntegrationUnitTest.class)
@UseAdviceWith
@SpringBootApplication
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class IntegrationUnitTest {

	@Autowired
	private ProducerTemplate template;
	
	@Autowired
	private CamelContext context;
	
	@EndpointInject(uri="mock:direct:amqNotificationProducerRoute")
	MockEndpoint mockNotificationEndpoint;

	@EndpointInject(uri="mock:direct:ProducerRoute")
	MockEndpoint mockProducerEndpoint;

	
	private final static String REQUEST_SUCCESS_MESSAGE = "src/test/resources/xmlIn/request_success.txt";
	private final static String REQUEST_FAILED_MESSAGE = "src/test/resources/xmlIn/request_failed.txt";
	private final static String RESPONSE_DB = "src/test/resources/xmlIn/reponse_db.csv";
	private String expectedSuccessMessage = "src/test/resources/expectedMessages/expected_success.txt";
	private String expectedFailedMessage = "src/test/resources/expectedMessages/expected_failed.txt";

	@Before
	public void ruoteConfiguration() throws Exception {



	}

	@Test
	public void successRequestTransformationTest() throws Exception 
	{

		context.start();
		assertTrue(context.getStatus().isStarted());
	
		File expectedSuccessFile = new File(REQUEST_SUCCESS_MESSAGE);
		File compareMessages = new File(expectedSuccessMessage);
		String expectedMessage = context.getTypeConverter().convertTo(String.class, compareMessages);
		mockProducerEndpoint.message(0).body(String.class).equals(expectedMessage);
		mockProducerEndpoint.assertIsSatisfied();
		
	}

	@Test
	public void failedRequestTransformationTest() throws Exception 
	{
		
		context.start();
		assertTrue(context.getStatus().isStarted());
		mockNotificationEndpoint.expectedMessageCount(1);

		File expectedFailedFile = new File(REQUEST_FAILED_MESSAGE);

		File compareMessages = new File(expectedFailedMessage);
		String expectedMessage = context.getTypeConverter().convertTo(String.class, compareMessages);
		mockNotificationEndpoint.message(0).body().toString().contains(expectedMessage);
		mockNotificationEndpoint.assertIsSatisfied();		
		mockProducerEndpoint.assertIsSatisfied();				
	}
}
