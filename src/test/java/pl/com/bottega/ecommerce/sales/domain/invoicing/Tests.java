package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;

import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Calendar;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType.FOOD;

public class Tests {
    @Test
    public void InvoiceRequestWithOneInvoice(){
        ClientData clientData = new ClientData(Id.generate(), "Patryk");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = new ProductData(Id.generate(), new Money(20), "xy", FOOD, Calendar.getInstance().getTime());
        Money totalCost = new Money(100);
        RequestItem item = new RequestItem(productData,  10,  totalCost);

        invoiceRequest.add(item);
        TaxPolicy taxPolicy = mock(TaxPolicy.class);

        BookKeeper bookKeeper = mock(BookKeeper.class);
        when(bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size());
        Invoice invoice = bookKeeper.issuance(invoiceRequest,taxPolicy);

        int result = invoice.getItems().size();
        assertThat(result, equalTo(1) );
        //bookKeeper.issuance();
    }
}
