package lab3_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {
	
	InvoiceFactory invoiceFactory;
	BookKeeper bookKeeper;
	ProductData productData;
	RequestItem requestItem;
	InvoiceRequest mockedInvoiceRequest;
	TaxPolicy mockedTaxPolicy;
	
	@Before public void initialize() {
		invoiceFactory = new InvoiceFactory();
		bookKeeper = new BookKeeper(invoiceFactory);
		productData = new ProductData(new Id("123"), new Money(3), "Cheese", ProductType.FOOD, new Date());
		requestItem = new RequestItem(productData, 2, new Money(10));
		mockedInvoiceRequest = mock(InvoiceRequest.class);
		mockedTaxPolicy = mock(TaxPolicy.class);
	}
	
	@Test
	public void testInvoiceRequestWithOnePositionShouldReturnInvoiceWithOnePosition() {
		//given
		final int testValue = 1;
		
		when(mockedInvoiceRequest.getItems()).thenReturn(Arrays.asList(requestItem));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())).thenReturn(new Tax(new Money(0.5),"Cheese Tax"));
		
		//when
		Invoice result = bookKeeper.issuance(mockedInvoiceRequest, mockedTaxPolicy);
		
		//then
		assertThat(result.getItems().size(), is(testValue));
	}
	
	@Test
	public void testInvoiceRequestWithTwoPositionsShouldCallcalculateTaxMethodTwice() {
		//given
		final int testValue = 2;
		
		when(mockedInvoiceRequest.getItems()).thenReturn(Arrays.asList(requestItem, requestItem));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())).thenReturn(new Tax(new Money(0.5),"Cheese Tax"));
		
		//when
		bookKeeper.issuance(mockedInvoiceRequest, mockedTaxPolicy);

		//then
		verify(mockedTaxPolicy, times(testValue)).calculateTax(ProductType.FOOD, requestItem.getTotalCost());
	}
	
	@Test
	public void testCreatingNewInvoiceShouldCallInvoiceFactoryCreateMethodOnce() {
		//given
		final int testValue = 1;
		ClientData clientData = new ClientData(Id.generate(),"Name");
		mockedInvoiceRequest = new InvoiceRequest(clientData);
		InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
		bookKeeper = new BookKeeper(mockedInvoiceFactory);

		
		when(mockedInvoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));
		
		//when
		bookKeeper.issuance(mockedInvoiceRequest, mockedTaxPolicy);
		
		//then
		verify(mockedInvoiceFactory,times(testValue)).create(clientData);
	}	
}
