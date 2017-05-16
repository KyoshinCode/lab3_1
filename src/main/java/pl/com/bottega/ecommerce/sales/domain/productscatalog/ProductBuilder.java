package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
	
	private Id id = Id.generate();
	private Money price;
	private String name;
	private ProductType productType;
	
	public ProductBuilder withPrice(Money price){
		this.price = price;
		return this;
	}
	
	public ProductBuilder withName(String name){
		this.name = name;
		return this;
	}
	
	public ProductBuilder withProductType(ProductType type){
		this.productType = type;
		return this;
	}
	
	public Product build(){
		return new Product(id, price, name, productType);
	}

}
