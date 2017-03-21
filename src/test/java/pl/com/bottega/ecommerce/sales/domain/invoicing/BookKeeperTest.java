package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by pebuls on 21.03.17.
 */
public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private Money money;
    private ProductData productData;
    private Invoice invoice;

    @Mock
    private TaxPolicy taxPolicy;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void beforeRun() throws Exception    {
        bookKeeper = new BookKeeper(new InvoiceFactory());
    }


    @Test
    public void demandInvoiceWithOnePositionReturnInvoiceWithOnePosition()  {

        invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Name"));
        money = new Money(25,"PLN");
        productData = new ProductData(Id.generate(), money, "productName", ProductType.DRUG, new Date());


        when(taxPolicy.calculateTax(productData.getType(), money)).thenReturn(new Tax(new Money(5, "PLN"), "Invalid tax"));

        invoiceRequest.add(new RequestItem(productData, 1, money));

        invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is(1));

    }

}