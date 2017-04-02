package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder.productBuilder;

/**
 * Created by Piotrek on 25.03.2017.
 */
public class BookKeeperTest {

    private InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    private RequestItem requestItem;
    private ProductData productData;

    @Before
    public void setUp() throws Exception{
        invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "TEST"));
        Product product = ProductBuilder
                .productBuilder()
                .withId(Id.generate())
                .withMoney(new Money(200))
                .withName("test")
                .withProductType(ProductType.STANDARD)
                .build();

        productData = product.generateSnapshot();
        requestItem = new RequestItem(productData, 1, new Money(200));

        ClientData clientData = invoiceRequest.getClientData();

        invoiceFactory = Mockito.mock(InvoiceFactory.class);
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
    }

    @Test
    public void issuanceTestWithOnlyOneItem() throws Exception{
        invoiceRequest.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(10), "TAX");
            }
        };

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertThat(invoice.getItems().size(), is(equalTo(1)));
    }

    @Test
    public void issuanceTestWithTwoPositions() throws Exception{
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(10), "TAX");
            }
        };

        TaxPolicy spyTax = spy(taxPolicy);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, spyTax);
        verify(spyTax, times(2)).calculateTax(productData.getType(), requestItem.getTotalCost());
    }

    @Test
    public void issuanceTestWithoutAnyPosition() throws Exception{
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(10), "TAX");
            }
        };

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertThat(invoice.getItems().size(), is(equalTo(0)));
    }

    @Test
    public void issuanceTestWithThreePosition() throws Exception{
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(10), "TAX");
            }
        };

        TaxPolicy spyTax = spy(taxPolicy);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, spyTax);
        Assert.assertThat(invoice.getItems().size(), is(equalTo(3)));
        verify(spyTax, times(3)).calculateTax(productData.getType(), requestItem.getTotalCost());
    }
}