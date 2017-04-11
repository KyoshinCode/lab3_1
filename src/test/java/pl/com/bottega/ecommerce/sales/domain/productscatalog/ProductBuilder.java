package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

/**
 * Created by Wojciech Szczepaniak on 11.04.2017.
 */
public class ProductBuilder {

    private Id id = Id.generate();
    private Money price = Money.ZERO;
    private String name = "Test";
    private ProductType productType = ProductType.STANDARD;


}
