package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.ArrayList;
import java.util.List;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

public class InvoiceRequestBuilder {

	private ClientData client;
	private List<RequestItem> reqestItems;

	public InvoiceRequestBuilder() {
		this.client = new ClientDataBuilder().build();
		this.reqestItems = new ArrayList<>();
	}

	public InvoiceRequestBuilder withClient(ClientData client) {
		this.client = client;
		return this;
	}

	public InvoiceRequestBuilder addItem(RequestItem item) {
		reqestItems.add(item);
		return this;
	}

	public InvoiceRequest build() {
		InvoiceRequest invoiceRequest = new InvoiceRequest(client);
		for (RequestItem reqestItem : reqestItems) {
			invoiceRequest.add(reqestItem);
		}
		return invoiceRequest;
	}

}