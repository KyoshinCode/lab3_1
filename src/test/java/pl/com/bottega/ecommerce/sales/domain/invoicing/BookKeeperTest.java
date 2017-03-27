package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {
	
	@Test
	public void testInvoiceRequestWithOneItemShouldReturnInvoiceWithOneLine() {
		
		// given
		InvoiceFactory factory = new InvoiceFactory();
		BookKeeper bookKeeper = new BookKeeper(factory);
		
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
		fail("Not yet implemented");
	}

}
