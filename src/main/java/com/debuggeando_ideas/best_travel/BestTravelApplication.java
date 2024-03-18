package com.debuggeando_ideas.best_travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Slf4j //crea un logger con la implementacion simple log for j
public class BestTravelApplication  {


	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

}


//		var fly = flyRepository.findById(15L).get();
//		var hotel = hotelRepository.findById(7L).get();
//		var ticket = ticketRepository.findById(UUID.fromString("32345678-1234-5678-4234-567812345678")).get();
//		var reservation = reservationRepository.findById(UUID.fromString("52345678-1234-5678-1234-567812345678")).get();
//		var customer = customerRepository.findById("BBMB771012HMCRR022").get();
//		log.info(String.valueOf(fly));
//		log.info(String.valueOf(hotel));
//		log.info(String.valueOf(ticket));
//		log.info(String.valueOf(reservation));
//		log.info(String.valueOf(customer));


//		this.flyRepository.selectLessPrice(BigDecimal.valueOf(20)).forEach(f->System.out.println(f));

//		this.flyRepository.selectBetweenPrice(BigDecimal.valueOf(10),BigDecimal.valueOf(15)).forEach(System.out::println);

//		this.flyRepository.selectOriginDestiny("Grecia","Mexico").forEach(System.out::println);

//		var fly = flyRepository.findById(1L).get();
//		// solo el vuelo
//		System.out.println(fly);
//
//		// vuelo y tickets
//		fly.getTickets().forEach(ticket -> System.out.println(ticket));


//		var fly = flyRepository.findByTicketId(UUID.fromString("12345678-1234-5678-2236-567812345678"));
//		System.out.println(fly);

		//hotelRepository.findByPriceBetween(BigDecimal.valueOf(100),BigDecimal.valueOf(200)).forEach(System.out::println);
		//hotelRepository.findByRatingGreaterThan(4).forEach(System.out::println);
//		var hotel = hotelRepository.findByReservationId(UUID.fromString("12345678-1234-5678-1234-567812345678"));
//		System.out.println(hotel);
