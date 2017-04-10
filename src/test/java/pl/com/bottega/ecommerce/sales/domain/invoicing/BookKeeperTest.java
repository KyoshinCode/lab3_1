package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Piotr on 21.03.2017.
 */
public class BookKeeperTest {

    final ClientData CLIENT_DATA = new ClientData(Id.generate(),"name");
    final InvoiceRequest INVOICE_REQUEST_WITH_ONE_ELEMENT = new InvoiceRequest(CLIENT_DATA);
    final InvoiceRequest INVOICE_REQUEST_WITH_TWO_ELEMENT = new InvoiceRequest(CLIENT_DATA);
    final InvoiceRequest INVOICE_REQUEST = new InvoiceRequest(CLIENT_DATA);
    final Money MONEY = new Money(1);
    final RequestItem REQUEST_ITEM = new RequestItem(new ProductData(Id.generate(), MONEY, "name", ProductType.DRUG, new Date()),0 ,MONEY);

    private BookKeeper bookKeeper;
    private InvoiceFactory mockInvoiceFactory;
    private TaxPolicy mockTaxPolicy;

    @Before
    public void setUp() {
        mockInvoiceFactory = mock(InvoiceFactory.class);
        when(mockInvoiceFactory.create(CLIENT_DATA)).thenReturn(new Invoice(Id.generate(), CLIENT_DATA));

        bookKeeper = new BookKeeper(mockInvoiceFactory);
        mockTaxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(MONEY, "description");

        when(mockTaxPolicy.calculateTax(Mockito.any(ProductType.class),Mockito.any(Money.class))).thenReturn(tax);
    }

    @Test
    public void invoiceWithOneItem() throws Exception {
        INVOICE_REQUEST_WITH_ONE_ELEMENT.add(REQUEST_ITEM);

        Invoice resultInvoice = bookKeeper.issuance(INVOICE_REQUEST_WITH_ONE_ELEMENT,mockTaxPolicy);

        assertThat(resultInvoice.getItems(),hasSize(1));
    }

    @Test
    public void callCalculateTaxTwice() {
        INVOICE_REQUEST_WITH_TWO_ELEMENT.add(REQUEST_ITEM);
        INVOICE_REQUEST_WITH_TWO_ELEMENT.add(REQUEST_ITEM);

        bookKeeper.issuance(INVOICE_REQUEST_WITH_TWO_ELEMENT,mockTaxPolicy);

        verify(mockTaxPolicy,times(2)).calculateTax(Mockito.any(ProductType.class),Mockito.any(Money.class));
    }

    @Test
    public void callCreateFromInvoiceFactoryOnce() {
        bookKeeper.issuance(INVOICE_REQUEST_WITH_TWO_ELEMENT,mockTaxPolicy);

        verify(mockInvoiceFactory,times(1)).create(CLIENT_DATA);
    }

    @Test
    public void checkMoney() {
        INVOICE_REQUEST_WITH_ONE_ELEMENT.add(REQUEST_ITEM);

       Invoice invoice =  bookKeeper.issuance(INVOICE_REQUEST_WITH_ONE_ELEMENT,mockTaxPolicy);

        assertThat(invoice.getItems().get(0).getTax().getAmount(), is(MONEY));
    }
}
