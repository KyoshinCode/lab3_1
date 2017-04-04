package lab3_1;




import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;

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

	@Test
	public void testInvoiceRequestWithOneItemReturnInvoiceWithOneItem() {
		
		//Given
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
		InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "John Doe"));
		ProductData productData = new ProductData(Id.generate(), new Money(10), "item", ProductType.DRUG, new Date());
		
		when(mockedTaxPolicy.calculateTax(productData.getType(), productData.getPrice())).thenReturn(new Tax(new Money(5), "Fake"));
		 
		RequestItem requestItem = new RequestItem(productData, 5, productData.getPrice());
		invoiceRequest.add(requestItem);
		
		//When
		Invoice invoice = bookKeeper.issuance(invoiceRequest, mockedTaxPolicy);
		//Then
		
		Assert.assertThat(invoice.getItems().size(), is(equalTo(1)));
		
	}
	
	
	@Test
	public void testInvoiceWithTwoItemsUseCalculateTaxMethodTwoTimes() {
		
		//Given
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
		InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "John Doe"));
		ProductData productData = new ProductData(Id.generate(), new Money(10), "item", ProductType.DRUG, new Date());
		
		when(mockedTaxPolicy.calculateTax(productData.getType(), productData.getPrice())).thenReturn(new Tax(new Money(5), "Fake"));
		 
		RequestItem requestItem = new RequestItem(productData, 5, productData.getPrice());
		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);
		
		//When
		Invoice invoice = bookKeeper.issuance(invoiceRequest, mockedTaxPolicy);
		
		//Then
		verify(mockedTaxPolicy, times(2)).calculateTax(productData.getType(), productData.getPrice());
	}
	
	
	
	
}
