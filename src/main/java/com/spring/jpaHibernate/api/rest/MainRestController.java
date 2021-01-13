package com.spring.jpaHibernate.api.rest;

import com.spring.jpaHibernate.entities.Hotel;
import com.spring.jpaHibernate.repositories.HotelRepository;
import com.spring.jpaHibernate.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MainRestController {

    @Autowired
    private HotelService hotelService;

    @ResponseBody
    @GetMapping("/hotels")
    public ResponseEntity<List<Hotel>> getAllHotels(){
        List<Hotel> hotels = hotelService.getAllHotels();
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    @ResponseBody
    //@CrossOrigin(value = "http://localhost") //access from any other host
    @PostMapping("/add_hotel")
    public ResponseEntity<String> addHotel(
            @RequestParam("hotel_name") String hotelName,
            @RequestParam("hotel_country") String hotelCountry,
            @RequestParam("hotel_stars") Byte hotelStars
    ){
        Hotel hotelToAdd = new Hotel();
        hotelToAdd.setName(hotelName);
        hotelToAdd.setCountry(hotelCountry);
        hotelToAdd.setStars(hotelStars);
        hotelService.addHotel(hotelToAdd);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
