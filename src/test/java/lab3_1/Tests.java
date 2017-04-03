package lab3_1;
 
 import static org.junit.Assert.*;
 
 import java.util.Date;
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.Mockito;
 import static org.hamcrest.Matchers.*;
 
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
 
 public class Tests{
 
	private TaxPolicy taxPolicy;
 	private ProductData productData;
 	private RequestItem requestItem;
 	private BookKeeper bookKeeper;
 	private InvoiceRequest invoiceRequest;
 	
 	@Before
 	public void setupTests() {
 		taxPolicy = Mockito.mock(TaxPolicy.class);
 		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class),
  				Mockito.any(Money.class))).thenReturn(new Tax(new Money(0), "mockowanie"));
 		productData = new ProductData(Id.generate(), new Money(123.45), "Tomato", ProductType.FOOD, new Date());
 		requestItem = new RequestItem(productData, 1, new Money(10.25));	
 		bookKeeper = new BookKeeper(new InvoiceFactory());
 		invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "user"));
 	
 	}
 	@Test
 	public void onePositionInvoice() {
  		invoiceRequest.add(requestItem);
  		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
  		assertThat(invoice.getItems().size(), is(equalTo(1)));
  	}
 	
 	@Test
	public void twoCalculateTaxCalls() {
 		invoiceRequest.add(requestItem);
 		invoiceRequest.add(requestItem);
 		bookKeeper.issuance(invoiceRequest, taxPolicy);
 		Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(ProductType.class),
 				Mockito.any(Money.class));
 	}
 }