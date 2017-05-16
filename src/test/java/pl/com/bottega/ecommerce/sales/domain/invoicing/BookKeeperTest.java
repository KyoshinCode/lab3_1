package pl.com.bottega.ecommerce.sales.domain.invoicing;

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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
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
	public void testIssuanceShouldReturnInvoiceWithOneLineWhenRequestHasOneItem() {
		
		// given
		ProductDataBuilder productDataBuilder = new ProductDataBuilder();
		ProductData productData = productDataBuilder
			.withProductId(new Id("999"))
			.withPrice(new Money(5))
			.withName("Chicken")
			.withType(ProductType.FOOD)
			.withDate(new Date())
			.build();
		
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
	public void testIssuanceShouldInvokeCalculateTaxTwoTimesWhenRequestHasTwoItems() {

		// given
		ProductDataBuilder productDataBuilder = new ProductDataBuilder();
		ProductData productData = productDataBuilder
			.withProductId(new Id("999"))
			.withPrice(new Money(5))
			.withName("Chicken")
			.withType(ProductType.FOOD)
			.withDate(new Date())
			.build();
		
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
	public void testIssuanceShouldReturnInvoiceWithNetBeingSumOfTwoItemsTotalCostsWhenRequestHasTwoItems() {
		
		// given
		ProductDataBuilder productDataBuilder = new ProductDataBuilder();
		ProductData productData = productDataBuilder
			.withProductId(new Id("999"))
			.withPrice(new Money(5))
			.withName("Chicken")
			.withType(ProductType.FOOD)
			.withDate(new Date())
			.build();
		
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
	public void testIssuanceShouldInvokeCalculateTaxWithTwoCorrectParameters() {
		
		// given
		ProductDataBuilder productDataBuilder = new ProductDataBuilder();
		ProductData productData = productDataBuilder
			.withProductId(new Id("999"))
			.withPrice(new Money(5))
			.withName("Chicken")
			.withType(ProductType.FOOD)
			.withDate(new Date())
			.build();
		
		RequestItem item = new RequestItem(productData, 3, new Money(16));
		InvoiceRequest mockedRequest = mock(InvoiceRequest.class);
		TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
		
		when(mockedRequest.getItems()).thenReturn(Arrays.asList(item));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, item.getTotalCost())).thenReturn(new Tax(new Money(0.7),"Tax"));
		
		// when
		Invoice invoice = bookKeeper.issuance(mockedRequest, mockedTaxPolicy);
				
		//then
		ArgumentCaptor<ProductType> parameterOne = ArgumentCaptor.forClass(ProductType.class);
		ArgumentCaptor<Money> parameterTwo = ArgumentCaptor.forClass(Money.class);
		verify(mockedTaxPolicy).calculateTax(parameterOne.capture(),parameterTwo.capture());
		Assert.assertThat(ProductType.FOOD, is(equalTo(parameterOne.getValue())));
		Assert.assertThat(new Money(16), is(equalTo(parameterTwo.getValue())));
	}
}
