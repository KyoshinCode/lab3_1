package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
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
    @Test
    public void invoiceWithOneItem() throws Exception {
        INVOICE_REQUEST_WITH_ONE_ELEMENT.add(REQUEST_ITEM);
        InvoiceFactory mockInvoiceFactory = mock(InvoiceFactory.class);
        when(mockInvoiceFactory.create(CLIENT_DATA)).thenReturn(new Invoice(Id.generate(), CLIENT_DATA));

        BookKeeper bookKeeper = new BookKeeper(mockInvoiceFactory);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(MONEY, "description");

        when(mockTaxPolicy.calculateTax(Mockito.any(ProductType.class),Mockito.any(Money.class))).thenReturn(tax);

        Invoice resultInvoice = bookKeeper.issuance(INVOICE_REQUEST_WITH_ONE_ELEMENT,mockTaxPolicy);

        assertThat(resultInvoice.getItems(),hasSize(1));
    }

    @Test
    public void callCalculateTaxTwice() {
        InvoiceFactory mockInvoiceFactory = mock(InvoiceFactory.class);
        when(mockInvoiceFactory.create(CLIENT_DATA)).thenReturn(new Invoice(Id.generate(), CLIENT_DATA));

        BookKeeper bookKeeper = new BookKeeper(mockInvoiceFactory);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(MONEY, "description");
        when(mockTaxPolicy.calculateTax(Mockito.any(ProductType.class),Mockito.any(Money.class))).thenReturn(tax);

        bookKeeper.issuance(INVOICE_REQUEST_WITH_TWO_ELEMENT,mockTaxPolicy);

        verify(mockInvoiceFactory,times(1)).create(CLIENT_DATA);
    }
}
