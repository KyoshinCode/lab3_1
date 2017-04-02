package pl.com.bottega.ecommerce.sales.application.api.command;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class AddProductCommand {

	private Id orderId;
	private Id productId;
	private int quantity;

	public AddProductCommand(Id orderId, Id productId, int quantity) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Id getOrderId() {
		return orderId;
	}

	public Id getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public static class Builder {
		private Id orderId = Id.generate();
		private Id productId = Id.generate();
		private int quantity;

		public Builder orderId(Id orderId) {
			this.orderId = orderId;
			return this;
		}

		public Builder productId(Id productId) {
			this.productId = productId;
			return this;
		}

		public Builder quantity(int quantity) {
			this.quantity = quantity;
			return this;
		}

		public AddProductCommand build() {
			return new AddProductCommand(this);
		}
	}

	private AddProductCommand(Builder builder) {
		this.orderId = builder.orderId;
		this.productId = builder.productId;
		this.quantity = builder.quantity;
	}
}