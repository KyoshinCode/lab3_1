package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

public class InvoiceFactoryTest {
	private InvoiceFactory invoiceFactory;
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	ClientData dummyClientData;
	
	@Mock
	InvoiceFactory mockInvoiceFactory;
	
	@Test public void testCreateInvoiceShouldCreateInvoice() {
		invoiceFactory = new InvoiceFactory();
		Invoice invoice = invoiceFactory.create(dummyClientData);
		assertThat(invoice, instanceOf(Invoice.class));
	}
	
	@Test public void testCreateInvoiceShouldCallInvoiceConstructor() {
		invoiceFactory = new InvoiceFactory();
		Invoice invoice = mockInvoiceFactory.create(dummyClientData);
		verify(mockInvoiceFactory, times(1)).create(dummyClientData);
	}

}
