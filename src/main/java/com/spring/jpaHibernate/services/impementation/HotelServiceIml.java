package com.spring.jpaHibernate.services.impementation;

import com.spring.jpaHibernate.entities.Hotel;
import com.spring.jpaHibernate.repositories.HotelRepository;
import com.spring.jpaHibernate.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HotelServiceIml implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAllByDeletedAtNull();
    }

    @Override
    public Hotel addHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel getHotel(Long id) {
        return hotelRepository.getOne(id);
    }

    @Override
    public void deleteHotel(Hotel hotel) {
        hotel.setDeletedAt(new Date());
        hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotelsPaged(int page, int length) {
        Pageable pageable = PageRequest.of(page, length);
        return hotelRepository.findAllByDeletedAtNull(pageable);
    }

    @Override
    public int countAllHotels() {
        return hotelRepository.countAllByDeletedAtNull();
    }
}
