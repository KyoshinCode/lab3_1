package lab3_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs; 

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTest {

	private ReservationRepository reservationRepository;
	private ProductRepository productRepository;
	private SuggestionService suggestionService;
	private ClientRepository clientRepository;
	private SystemContext systemContext;
	private AddProductCommandHandler addProductCommandHandler;
	private AddProductCommand addProductCommand;
	private Reservation reservation;
	private Product product;
	
	@Before
	void initialize() {
		reservationRepository = mock(ReservationRepository.class);
		productRepository = mock(ProductRepository.class);
		suggestionService = mock(SuggestionService.class);
		clientRepository = mock(ClientRepository.class);
		systemContext = mock(SystemContext.class);
		addProductCommandHandler = new AddProductCommandHandler();
		addProductCommand = new AddProductCommand(Id.generate(),Id.generate(),3);
		reservation = new Reservation(Id.generate(),ReservationStatus.OPENED,new ClientData(Id.generate(),"Name"),new Date());
		product = new Product(Id.generate(), new Money(10), "product", ProductType.STANDARD);
		
		when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);
		when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);
		
		Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
 		Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
 		Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
 		Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
 		Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
		
	}
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
