package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {
	
	private InvoiceFactory factory;
	private BookKeeper bookKeeper;
	
	@Before
	public void setUp() {
		factory = new InvoiceFactory();
		bookKeeper = new BookKeeper(factory);
	}
	
	@Test
	public void testInvoiceRequestWithOneItemShouldReturnInvoiceWithOneLine() {
		
		// given
		ProductData productData = new ProductData(new Id("999"), new Money(5), "Chicken", ProductType.FOOD, new Date());
		RequestItem item = new RequestItem(productData, 3, new Money(16));
		InvoiceRequest mockedRequest = mock(InvoiceRequest.class);
		TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
		
		when(mockedRequest.getItems()).thenReturn(Arrays.asList(item));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, item.getTotalCost())).thenReturn(new Tax(new Money(0.7),"Tax"));
		
		// when
		Invoice invoice = bookKeeper.issuance(mockedRequest, mockedTaxPolicy);
		
		//then
		Assert.assertThat(invoice.getItems().size(), is(equalTo(1)));
	}
	
	@Test
	public void testInvoiceRequestWithTwoItemsShouldInvokeCalculateTaxTwoTimes() {

		// given
		ProductData productData = new ProductData(new Id("999"), new Money(5), "Chicken", ProductType.FOOD, new Date());
		RequestItem item = new RequestItem(productData, 3, new Money(16));
		InvoiceRequest mockedRequest = mock(InvoiceRequest.class);
		TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
		
		when(mockedRequest.getItems()).thenReturn(Arrays.asList(item,item));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, item.getTotalCost())).thenReturn(new Tax(new Money(0.7),"Tax"));
		
		// when
		Invoice invoice = bookKeeper.issuance(mockedRequest, mockedTaxPolicy);
				
		//then
		verify(mockedTaxPolicy, times(2)).calculateTax(ProductType.FOOD, item.getTotalCost());
	}
	
	@Test
	public void testInvoiceRequestWithTwoItemsShouldReturnInvoiceWithNetBeingSumOfThoseItemsTotalCosts() {
		
		// given
		ProductData productData = new ProductData(new Id("999"), new Money(5), "Chicken", ProductType.FOOD, new Date());
		RequestItem item = new RequestItem(productData, 3, new Money(16));
		InvoiceRequest mockedRequest = mock(InvoiceRequest.class);
		TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
		
		when(mockedRequest.getItems()).thenReturn(Arrays.asList(item,item));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, item.getTotalCost())).thenReturn(new Tax(new Money(0.7),"Tax"));
		
		// when
		Invoice invoice = bookKeeper.issuance(mockedRequest, mockedTaxPolicy);
				
		//then
		Assert.assertThat(invoice.getNet(), is(equalTo(item.getTotalCost().add(item.getTotalCost()))));
	}
	
	@Test
	public void testInvoiceRequestShouldInvokeCalculateTaxWithTwoCorrectParameters() {
		
	}
}
