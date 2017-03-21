package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.hamcrest.generator.qdox.model.annotation.AnnotationAdd;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by 194974 on 3/21/2017.
 */
public class BookKeeperTest {

    private BookKeeper bookKeeper;

    @Mock
    private TaxPolicy taxPolicy;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        bookKeeper = new BookKeeper(new InvoiceFactory());
    }

    @Test
    public void test_OnePosition() throws Exception {
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Anna"));
        Money money = new Money(10, "PLN");
        ProductData productData = new ProductData(Id.generate(), money, "czekolada", ProductType.FOOD, new Date());
        when(taxPolicy.calculateTax(productData.getType(), money)).thenReturn(new Tax(new Money(5, "PLN"), "Falszywy podatek"));

        invoiceRequest.add(new RequestItem(productData, 2, money));

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is (1));
    }

    @Test
    public void test_TwoPositions() throws Exception {
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Karol"));
        Money money = new Money(150);
        ProductData productData = new ProductData(Id.generate(), money, "podklad", ProductType.DRUG, new Date());
        when(taxPolicy.calculateTax(productData.getType(), money)).thenReturn(new Tax(new Money(120), "Fikcyjna pozycja"));

        invoiceRequest.add(new RequestItem(productData, 1, money));
        invoiceRequest.add(new RequestItem(productData, 2, money));

        bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(productData.getType(), money);
    }

    @Test
    public void test_NoPosition() throws Exception {
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Napoleon"));
        Money money = new Money(20, "USD");
        ProductData productData = new ProductData(Id.generate(), money, "sukienka", ProductType.STANDARD, new Date());
        when(taxPolicy.calculateTax(productData.getType(), money)).thenReturn(new Tax(new Money(34.1, "USD"), "Tajna operacja"));

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is(0));
    }
}