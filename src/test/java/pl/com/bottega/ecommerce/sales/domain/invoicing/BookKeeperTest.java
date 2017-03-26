package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by 195016 on 3/21/2017.
 */
public class BookKeeperTest {

    private final ClientData ANY_CLIENT_DATA = new ClientData(Id.generate(), "anyName");
    private final InvoiceRequest INVOICE_REQUEST_WITH_ONE_ELEMENT = new InvoiceRequest(ANY_CLIENT_DATA);
    private final InvoiceRequest INVOICE_REQUEST_WITH_TWO_ELEMENTS = new InvoiceRequest(ANY_CLIENT_DATA);
    private final Money ANY_MONEY = new Money(0);
    private final RequestItem ANY_REQUEST_ITEM = new RequestItem(new ProductData(Id.generate(), ANY_MONEY, "name", ProductType.FOOD, new Date()),0,ANY_MONEY);

    @Test
    public void shouldReturnInvoiceWithOneItem() throws Exception {
        //given
        INVOICE_REQUEST_WITH_ONE_ELEMENT.add(ANY_REQUEST_ITEM);

        InvoiceFactory mockInvoiceFactory = mock(InvoiceFactory.class);
        when(mockInvoiceFactory.create(ANY_CLIENT_DATA)).thenReturn(new Invoice(Id.generate(), ANY_CLIENT_DATA));

        BookKeeper bookKeeper = new BookKeeper(mockInvoiceFactory);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(ANY_MONEY, "anyDescription");

        when(mockTaxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(tax);

        //when
        Invoice resultInvoice = bookKeeper.issuance(INVOICE_REQUEST_WITH_ONE_ELEMENT, mockTaxPolicy);

        //then
        assertThat(resultInvoice.getItems(),hasSize(1));

    }

    @Test
    public void shouldCallCalculateTaxTwice() throws Exception {
        //given
        INVOICE_REQUEST_WITH_TWO_ELEMENTS.add(ANY_REQUEST_ITEM);
        INVOICE_REQUEST_WITH_TWO_ELEMENTS.add(ANY_REQUEST_ITEM);

        InvoiceFactory mockInvoiceFactory = mock(InvoiceFactory.class);
        when(mockInvoiceFactory.create(ANY_CLIENT_DATA)).thenReturn(new Invoice(Id.generate(), ANY_CLIENT_DATA));

        BookKeeper bookKeeper = new BookKeeper(mockInvoiceFactory);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(ANY_MONEY, "anyDescription");

        when(mockTaxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(tax);

        //when
        bookKeeper.issuance(INVOICE_REQUEST_WITH_TWO_ELEMENTS, mockTaxPolicy);

        //then
        verify(mockTaxPolicy, times(2)).calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));

    }
}