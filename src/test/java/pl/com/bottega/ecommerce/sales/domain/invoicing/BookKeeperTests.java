package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Arrays;
import java.util.Calendar;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class BookKeeperTests {
    RequestItem item;
    BookKeeper bookKeeper;
    @Before
    public void initItem() {
        ProductData productData = new ProductData(Id.generate(), new Money(20), "Ziemniaki", ProductType.FOOD, Calendar.getInstance().getTime());
        item = new RequestItem(productData, 10, new Money(100));

    }
    @Before
    public void initBookKeeper(){
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        bookKeeper = new BookKeeper(invoiceFactory);
    }

    @Test
    public void InvoiceRequestWithOneInvoice(){
        //given
        InvoiceRequest mockInvoiceRequest = mock(InvoiceRequest.class);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
        when(mockInvoiceRequest.getItems()).thenReturn(Arrays.asList(item));
        when(mockTaxPolicy.calculateTax(ProductType.FOOD, item.getTotalCost())).thenReturn(new Tax(new Money(5),"Desc"));

        //when
        Invoice invoice = bookKeeper.issuance(mockInvoiceRequest,mockTaxPolicy);

        //then
        assertThat(invoice.getItems().size(), equalTo(1) );

    }

    @Test
    public void CountCalculateTaxCalls(){
        //given
        InvoiceRequest mockInvoiceRequest = mock(InvoiceRequest.class);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
        when(mockInvoiceRequest.getItems()).thenReturn(Arrays.asList(item,item));
        when(mockTaxPolicy.calculateTax(ProductType.FOOD, item.getTotalCost())).thenReturn(new Tax(new Money(5),"Desc"));

        //when
        bookKeeper.issuance(mockInvoiceRequest,mockTaxPolicy);

        //then
        verify(mockTaxPolicy, times(2)).calculateTax(ProductType.FOOD,new Money(100));
    }


    @Test
    public void VerifyMethodCalls(){
        //given
        InvoiceRequest mockInvoiceRequest = mock(InvoiceRequest.class);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
        when(mockInvoiceRequest.getItems()).thenReturn(Arrays.asList(item));
        when(mockTaxPolicy.calculateTax(ProductType.FOOD, item.getTotalCost())).thenReturn(new Tax(new Money(5),"Desc"));

        //when
        bookKeeper.issuance(mockInvoiceRequest,mockTaxPolicy);

        //then
        verify(mockInvoiceRequest).getClientData();
        verify(mockTaxPolicy).calculateTax(ProductType.FOOD, new Money(100));
    }
    @Test
    public void InvoiceFactoryCreateCalls(){
        //given
        InvoiceFactory mockInvoiceFactory = mock(InvoiceFactory.class);
        bookKeeper = new BookKeeper(mockInvoiceFactory);
        InvoiceRequest mockInvoiceRequest = mock(InvoiceRequest.class);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
        ClientData client = new ClientData(Id.generate(),"Patryk");
        when(mockInvoiceFactory.create(client)).thenReturn(new Invoice(Id.generate(),client));

        //when
        bookKeeper.issuance(mockInvoiceRequest,mockTaxPolicy);

        //then
        verify(mockInvoiceFactory, times(1)).create(any(ClientData.class));

    }
}
