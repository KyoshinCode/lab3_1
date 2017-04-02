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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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

}