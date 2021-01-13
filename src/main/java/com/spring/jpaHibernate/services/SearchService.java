package com.spring.jpaHibernate.services;

import com.spring.jpaHibernate.entities.BookingEntity;
import com.spring.jpaHibernate.entities.Hotel;

import java.util.List;

public interface SearchService {

    List<Hotel> searchBookingEntities(int page, int length, String city,
                                      double priceFrom, double priceTo, byte roomsAmount, byte peopleAmount);
    int countHotels(String city, double priceFrom, double priceTo,
                    byte roomsAmount, byte peopleAmount);

}
