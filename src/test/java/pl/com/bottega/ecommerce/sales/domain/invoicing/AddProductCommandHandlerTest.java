package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTest {

	AddProductCommandHandler testHandler = null;
	AddProductCommand testCommand = null;
	ProductRepository prodRep = null;
	ReservationRepository reserRep = null;
	ClientRepository clientRep = null;
	SuggestionService suggServ = null;
	SystemContext sysContext = null;
	Product product = null;
	Product unavaiableProduct = null;
	Reservation reservation = null;
	
	@Before
	public void setUp() {
		reserRep = mock(ReservationRepository.class);
		prodRep = mock(ProductRepository.class);
		clientRep = mock(ClientRepository.class);
		suggServ = mock(SuggestionService.class);
		reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED,
				new ClientData(Id.generate(), "Ala Kota"), new Date());
		product = new Product(Id.generate(), new Money(100), "ananas", ProductType.FOOD);
		unavaiableProduct = new Product(Id.generate(), new Money(100), "lizak", ProductType.FOOD);
		sysContext = new SystemContext();
		testHandler = new AddProductCommandHandler();
		testCommand = new AddProductCommand(Id.generate(), Id.generate(), 1);
		when(reserRep.load(testCommand.getOrderId())).thenReturn(reservation);
		Whitebox.setInternalState(testHandler, "reservationRepository", reserRep);
		Whitebox.setInternalState(testHandler, "clientRepository", clientRep);
		Whitebox.setInternalState(testHandler, "productRepository", prodRep);
		Whitebox.setInternalState(testHandler, "suggestionService", suggServ);
		Whitebox.setInternalState(testHandler, "systemContext", sysContext);
	}
	
	@Test
    public void testProductIsAvailable() throws Exception {
        when(prodRep.load(any(Id.class))).thenReturn(product);
        testHandler.handle(testCommand);
        assertThat(product.isAvailable(), is(equalTo(true)));
    }

}
