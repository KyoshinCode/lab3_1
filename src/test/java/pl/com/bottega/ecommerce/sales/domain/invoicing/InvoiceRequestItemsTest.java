package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Rule;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.hamcrest.Matchers.*;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;


public class InvoiceRequestItemsTest {
	private InvoiceRequest invoiceRequest;
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	private RequestItem dummyRequestItem1;
	
	@Mock
	private RequestItem dummyRequestItem2;
	
	@Mock
	private RequestItem dummyRequestItem3;
	
	@Mock
	private ClientData dummyClientData;
	
	@Test
	public void testShouldProducListWithThreeProducts() {
		invoiceRequest = new InvoiceRequest(dummyClientData);
		invoiceRequest.add(dummyRequestItem1);
		invoiceRequest.add(dummyRequestItem2);
		invoiceRequest.add(dummyRequestItem3);
		
		assertThat(3, is(equalTo(invoiceRequest.getItems().size())));
	}
}
