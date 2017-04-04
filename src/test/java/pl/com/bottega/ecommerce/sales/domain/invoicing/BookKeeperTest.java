package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private TaxPolicy taxPolicy;
    private RequestItem requestItem;
    private ProductData productData;

    @Before
    public void setUp() throws Exception {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        taxPolicy = mock(TaxPolicy.class);
        productData = mock(ProductData.class);
    }

    @Test
    public void testInvoiceOneItem() throws Exception {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(30), "mock"));
        requestItem = new RequestItem(productData, 1, new Money(30));
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Janusz Tracz"));
        invoiceRequest.add(requestItem);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is(equalTo(1)));
    }

    @Test
    public void testInvoiceTwoItem() throws Exception {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(30), "mock"));
        RequestItem requestItem1 = new RequestItem(productData, 1, new Money(2));
        RequestItem requestItem2 = new RequestItem(productData, 1, new Money(3));

        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Janusz Tracz"));
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));

    }
    @Test
    public void testInvoiceZeroItem() throws Exception {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(30), "mock"));

        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Stanisław Wokulski"));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(equalTo(0)));
    }
    @Test
    public void testInvoiceZeroItemAsBehaviour() throws Exception {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(30), "example"));
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Ignacy Rzecki"));
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(0)).calculateTax(any(ProductType.class), any(Money.class));
    }

}