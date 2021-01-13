package com.spring.jpaHibernate.services;

import com.spring.jpaHibernate.entities.Hotel;

import java.util.List;

public interface HotelService {
    List<Hotel> getAllHotels();
    Hotel addHotel(Hotel hotel);
    Hotel saveHotel(Hotel hotel);
    Hotel getHotel(Long id);
    void deleteHotel(Hotel hotel);
    List<Hotel> getAllHotelsPaged(int page, int length);
    int countAllHotels();

}
