import org.hamcrest.Matchers.*;
import org.junit.Test;

import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Tests {
    @Test
    public void InvoiceRequestWithOneInvoice(){

        InvoiceRequest invoiceRequest = mock(InvoiceRequest.class);
        RequestItem item = mock(RequestItem.class);
        invoiceRequest.add(item);
        TaxPolicy taxPolicy = mock(TaxPolicy.class);

        BookKeeper bookKeeper = mock(BookKeeper.class);
        //when(bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size());

        int result = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();
        assertThat(result, equalTo(1) );
        //bookKeeper.issuance();
    }
}
