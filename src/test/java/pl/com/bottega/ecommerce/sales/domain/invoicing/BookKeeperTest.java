package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;

import static org.mockito.Mockito.*;

/**
 * Created by grusz on 24.03.2017.
 */
public class BookKeeperTest {

    private InvoiceRequest invoiceRequest;
    private RequestItem requestItem;
    private InvoiceFactory invoiceFactory;
    private ProductData productData;

    @Before
    public void setUp() throws Exception {

        invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Andrew"));
        Product product = ProductBuilder
                .aProduct()
                .withProductType(ProductType.FOOD)
                .build();
        productData = product.generateSnapshot();
        requestItem = new RequestItem(productData,1,new Money(100));
        ClientData clientData = invoiceRequest.getClientData();
        invoiceFactory = mock(InvoiceFactory.class);
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
    }

    @Test
    public void issueInvoice_onePosition_checkPositionsAmountOnInvoice() {

        invoiceRequest.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0), "no tax");
            }
        };

        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertThat(result.getItems().size(), is(equalTo(1)));


    }

    @Test
    public void issueInvoice_TwoPositions_callCountForTaxPolicy(){
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0), "no tax");
            }
        };
        TaxPolicy spy = spy(taxPolicy);
        Invoice result = bookKeeper.issuance(invoiceRequest, spy);
        verify(spy, times(2)).calculateTax(productData.getType(),requestItem.getTotalCost());
    }

    @Test
    public void issueInvoice_NoPositions_callCountForTaxPolicy(){

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0), "no tax");
            }
        };
        TaxPolicy spy = spy(taxPolicy);
        Invoice result = bookKeeper.issuance(invoiceRequest, spy);
        verify(spy, times(0)).calculateTax(productData.getType(),requestItem.getTotalCost());
    }

    @Test public void issueInvoice_NoPositions_checkPositionsAmountOnInvoice() {

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0), "no tax");
            }
        };

        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertThat(result.getItems().size(), is(equalTo(0)));


    }


}
