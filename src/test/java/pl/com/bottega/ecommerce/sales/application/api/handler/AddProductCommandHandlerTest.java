package pl.com.bottega.ecommerce.sales.application.api.handler;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;


public class AddProductCommandHandlerTest {

    private AddProductCommandHandler handler;
    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;
    private SystemContext systemContext;
    AddProductCommand command;
    Reservation reservation;
    Product product;
    @Before
    public void setUp(){
        handler = new AddProductCommandHandler();

        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        suggestionService = mock(SuggestionService.class);
        clientRepository = mock(ClientRepository.class);
        systemContext = mock(SystemContext.class);

        command = new AddProductCommand(Id.generate(),Id.generate(),5);
        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "JAN"), new Date());
        product = new Product(Id.generate(),new Money(100),"product", ProductType.FOOD);

        when(reservationRepository.load(any(Id.class))).thenReturn(reservation);
        when(productRepository.load(any(Id.class))).thenReturn(product);


        Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(handler, "productRepository", productRepository);
        Whitebox.setInternalState(handler, "suggestionService", suggestionService);
        Whitebox.setInternalState(handler, "clientRepository", clientRepository);
        Whitebox.setInternalState(handler, "systemContext", systemContext);
    }



    @Test
    public void isReservationSaved(){
        handler.handle(command);
        verify(reservationRepository).save(reservation);
    }

    @Test
    public void isProductAdded(){
        handler.handle(command);
        assertFalse(reservation.getReservedProducts().isEmpty());
    }

    @Test (expected = DomainOperationException.class)
    public void exceptionWhenReservationClosed(){
        reservation.close();
        handler.handle(command);
    }

}
