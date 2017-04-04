package lab3_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs; 

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceLine;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceLineBuilder;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItemBuilder;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {
	
	private InvoiceFactory invoiceFactory;
	private BookKeeper bookKeeper;
	private ProductData productData;
	private RequestItem requestItem;
	private InvoiceRequest mockedInvoiceRequest;
	private TaxPolicy mockedTaxPolicy;
	private ClientData clientData;
	private Tax tax;
	
	@Before public void initialize() {
		invoiceFactory = new InvoiceFactory();
		bookKeeper = new BookKeeper(invoiceFactory);
		productData = new ProductDataBuilder()
				.withId(new Id("123"))
				.withPrice(new Money(3))
				.withName("Cheese")
				.withType(ProductType.FOOD)
				.build();
		requestItem = new RequestItemBuilder()
				.withproductData(productData)
				.withQuantity(2)
				.withTotalCost(new Money(10))
				.build();
		mockedInvoiceRequest = mock(InvoiceRequest.class);
		mockedTaxPolicy = mock(TaxPolicy.class);
		clientData = new ClientData(Id.generate(),"Name");
		tax = new Tax(new Money(0.5),"descritpion");
	}
	
	@Test
	public void testInvoiceRequestWithOnePositionShouldReturnInvoiceWithOnePosition() {
		//given
		final int testValue = 1;
		
		when(mockedInvoiceRequest.getItems()).thenReturn(Arrays.asList(requestItem));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())).thenReturn(tax);
		
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
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())).thenReturn(tax);
		
		//when
		bookKeeper.issuance(mockedInvoiceRequest, mockedTaxPolicy);

		//then
		verify(mockedTaxPolicy, times(testValue)).calculateTax(ProductType.FOOD, requestItem.getTotalCost());
	}
	
	@Test
	public void testCreatingNewInvoiceShouldCallInvoiceFactoryCreateMethodOnce() {
		//given
		final int testValue = 1;
		mockedInvoiceRequest = new InvoiceRequest(clientData);
		InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
		bookKeeper = new BookKeeper(mockedInvoiceFactory);

		when(mockedInvoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));
		
		//when
		bookKeeper.issuance(mockedInvoiceRequest, mockedTaxPolicy);
		
		//then
		verify(mockedInvoiceFactory,times(testValue)).create(clientData);
	}	
	
	@Test
	public void testToGetInvoiceLineWithProperValues() {
		//given
		InvoiceLine testValue = new InvoiceLineBuilder()
				.withProductData(productData)
				.withQuantity(requestItem.getQuantity())
				.withNet(requestItem.getTotalCost())
				.withTax(tax)
				.build();
		
		when(mockedInvoiceRequest.getItems()).thenReturn(Arrays.asList(requestItem));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())).thenReturn(tax);
		
		//when
		Invoice result = bookKeeper.issuance(mockedInvoiceRequest, mockedTaxPolicy);
		
		//then
		assertThat(result.getItems().get(0), samePropertyValuesAs(testValue));
	}
	
}
