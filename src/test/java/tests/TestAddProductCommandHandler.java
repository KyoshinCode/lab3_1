/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import static org.hamcrest.Matchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

/**
 *
 * @author jankowskirobert
 */
public class TestAddProductCommandHandler {
    
    private ReservationRepository reservationRepository;
	

	private ProductRepository productRepository;
	

	private SuggestionService suggestionService;
	

	private ClientRepository clientRepository;
	

	private SystemContext systemContext;
        private AddProductCommandHandler addProductCommandHandler;
        private static final Id productId = Id.generate();
        private static final Id orderId = Id.generate();
    @Before
    public void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        suggestionService = mock(SuggestionService.class);
        clientRepository = mock(ClientRepository.class);
        systemContext = mock(SystemContext.class);
        addProductCommandHandler = spy(AddProductCommandHandler.class);
        
        addProductCommandHandler.setClientRepository(clientRepository);
        addProductCommandHandler.setProductRepository(productRepository);
        addProductCommandHandler.setReservationRepository(reservationRepository);
        addProductCommandHandler.setSuggestionService(suggestionService);
        addProductCommandHandler.setSystemContext(systemContext);
    }
    @Test
    public void testProductHandle() {
        AddProductCommand command = new AddProductCommand(orderId, productId, 1);
        addProductCommandHandler.handle(command);
        verify(reservationRepository, times(1)).load(command.getOrderId());
//        verify(productRepository, times(1)).load(command.getProductId());
    }
    
}
