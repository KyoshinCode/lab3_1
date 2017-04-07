package pl.com.bottega.ecommerce.sales.application.api.handler;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
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
    private AddProductCommand command;
    private Reservation reservation;
    private Product product;
    private SuggestionService suggestionService;


    @Before
    public void setUp(){
        ProductRepository productRepository;
        ClientRepository clientRepository;
        SystemContext systemContext;

        handler = new AddProductCommandHandler();

        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        suggestionService = mock(SuggestionService.class);
        clientRepository = mock(ClientRepository.class);
        systemContext = new SystemContext();

        command = new AddProductCommand(Id.generate(),Id.generate(),5);
        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "JAN"), new Date());
        product = new ProductBuilder().build();

        Product equivalentProduct = new ProductBuilder().id(Id.generate()).name("Zamiennik").price(new Money(10)).build();
        Client client = new Client();

        when(clientRepository.load(any(Id.class))).thenReturn(client);
        when(reservationRepository.load(any(Id.class))).thenReturn(reservation);
        when(productRepository.load(any(Id.class))).thenReturn(product);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(equivalentProduct);

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

    @Test
    public void isAvailableRemovedProduct(){
        product.markAsRemoved();
        assertFalse(product.isAvailable());
    }
    @Test
    public void isSuggestedEquivalent(){
        product.markAsRemoved();
        handler.handle(command);
        verify(suggestionService).suggestEquivalent(any(Product.class), any(Client.class));
    }

}
