package pl.com.bottega.ecommerce.sales.domain.invoicing;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Tests {
    @Test
    public void InvoiceRequestWithOneInvoice(){
        //given
        ProductData productData = new ProductData(Id.generate(), new Money(20), "Ziemniaki", ProductType.FOOD, Calendar.getInstance().getTime());
        RequestItem item = new RequestItem(productData,  10,  new Money(100));

        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        InvoiceRequest mockInvoiceRequest = mock(InvoiceRequest.class);
        TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);

        when(mockInvoiceRequest.getItems()).thenReturn(Arrays.asList(item));
        when(mockTaxPolicy.calculateTax(ProductType.FOOD, item.getTotalCost())).thenReturn(new Tax(new Money(5),"Desc"));

        //when
        Invoice invoice = bookKeeper.issuance(mockInvoiceRequest,mockTaxPolicy);

        //then
        assertThat(invoice.getItems().size(), equalTo(1) );

    }
}
