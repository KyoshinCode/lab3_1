package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerUnitTests {
	 
 	private ReservationRepository reservationRepository;
 	private ProductRepository productRepository;
 	private SuggestionService suggestionService;
 	private ClientRepository clientRepository;
 	private SystemContext systemContext;
 	private AddProductCommandHandler addProductCommandHandler;
 	private AddProductCommand command;
 	private Reservation reservation;
 	private Product product;
 	

  }
