package lab3_1;
 
 import static org.junit.Assert.*;
 
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.Mockito;
 import org.mockito.internal.util.reflection.Whitebox;
 import static org.hamcrest.Matchers.*;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
 import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
 import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationBuilder;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;
import pl.com.bottega.ecommerce.system.application.SystemContext;
 
 public class Tests_2 {
 	
	 AddProductCommandHandler testHandler = null;
	 AddProductCommand testCommand = null;
	 ProductRepository prRep = null;
	 ReservationRepository resRep = null;
 	
 	@Before
 	public void setupTests() {
 		this.resRep = Mockito.mock(ReservationRepository.class);
 		this.prRep = Mockito.mock(ProductRepository.class);
 		ClientRepository clRep = Mockito.mock(ClientRepository.class);
 		SuggestionService suSe = Mockito.mock(SuggestionService.class);
 		SystemContext sysCon = Mockito.mock(SystemContext.class);
 		this.testCommand = new AddProductCommand(Id.generate(), Id.generate(), 123);
 		this.testHandler = new AddProductCommandHandler();
 		Whitebox.setInternalState(testHandler, "reservationRepository", resRep);
 		Whitebox.setInternalState(testHandler, "clientRepository", clRep);
 		Whitebox.setInternalState(testHandler, "productRepository", prRep);
 		Whitebox.setInternalState(testHandler, "suggestionService", suSe);
 		Whitebox.setInternalState(testHandler, "systemContext", sysCon);
 		
 	}
 	
 	@Test
 	public void testAvailableProductCorrectlyAddedToReservation() {
 	Product tProd = new ProductBuilder()
		.available()
		.withName("test product")
		.withPrice(new Money(123))
		.withType(ProductType.STANDARD)
		.withId(Id.generate())
		.build();
	Reservation tRes = new ReservationBuilder()
		.open()
		.withClientData(new ClientData(Id.generate(), "wujeksado"))
		.withId(Id.generate())
		.build();
	Mockito.when(this.prRep.load(testCommand.getProductId())).thenReturn(tProd);
	Mockito.when(this.resRep.load(testCommand.getOrderId())).thenReturn(tRes);
	testHandler.handle(testCommand);
	assertThat(tRes.contains(tProd), is(equalTo(true)));
  	}
 	
 	@Test (expected = DomainOperationException.class)
 	public void testAvailableProductNotAddedToClosedReservation() {
 		Product tProd = new ProductBuilder()
 				.available()
 				.withName("test product")
 				.withPrice(new Money(123))
 				.withType(ProductType.STANDARD)
 				.withId(Id.generate())
 				.build();
 		Reservation tRes = new ReservationBuilder()
 				.close()
 				.withClientData(new ClientData(Id.generate(), "wujeksado"))
 				.withId(Id.generate())
 				.build();
 		Mockito.when(this.prRep.load(testCommand.getProductId())).thenReturn(tProd);
 		Mockito.when(this.resRep.load(testCommand.getOrderId())).thenReturn(tRes);
 		testHandler.handle(testCommand);
 	}
 }