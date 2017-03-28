package lab3_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

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

	@Test
	public void testInvoiceRequestWithOnePositionShouldReturnInvoiceWithOnePosition() {
		//given
		InvoiceFactory invoiceFactory= new InvoiceFactory();
		BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
		ProductData productData = new ProductData(new Id("123"), new Money(3), "Cheese", ProductType.FOOD, new Date());
		RequestItem requestItem = new RequestItem(productData, 2, new Money(10));
		InvoiceRequest mockedInvoiceRequest = mock(InvoiceRequest.class);
		TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
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
		InvoiceFactory invoiceFactory= new InvoiceFactory();
		BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
		ProductData productData = new ProductData(new Id("123"), new Money(3), "Cheese", ProductType.FOOD, new Date());
		RequestItem requestItem = new RequestItem(productData, 2, new Money(10));
		InvoiceRequest mockedInvoiceRequest = mock(InvoiceRequest.class);
		TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
		final int testValue = 2;
		
		when(mockedInvoiceRequest.getItems()).thenReturn(Arrays.asList(requestItem, requestItem));
		when(mockedTaxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())).thenReturn(new Tax(new Money(0.5),"Cheese Tax"));
		
	}	
}
