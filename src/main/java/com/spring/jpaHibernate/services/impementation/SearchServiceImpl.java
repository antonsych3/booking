package com.spring.jpaHibernate.services.impementation;

import com.spring.jpaHibernate.entities.BookingEntity;
import com.spring.jpaHibernate.entities.Hotel;
import com.spring.jpaHibernate.repositories.HotelRepository;
import com.spring.jpaHibernate.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    HotelRepository hotelRepository;

    @Override
    public List<Hotel> searchBookingEntities(int page, int length, String city, double priceFrom, double priceTo, byte roomsAmount, byte peopleAmount) {

        Pageable pageable = PageRequest.of(page, length);
        Specification<Hotel> specification = (Specification<Hotel>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("city")), "%"+city.toUpperCase()+"%");

        if (priceFrom!=0){
            specification = specification.and((Specification<Hotel>)(root, criteriQuery, criteriaBuilder)->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceFrom));
        }

        if (priceTo!=0){
            specification = specification.and((Specification<Hotel>)(root, criteriQuery, criteriaBuilder)->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo));
        }

        if (roomsAmount!=0){
            specification = specification.and((Specification<Hotel>)(root, criteriQuery, criteriaBuilder)->
                    criteriaBuilder.equal(root.get("roomsAmount"), roomsAmount));
        }

        if (peopleAmount!=0){
            specification = specification.and((Specification<Hotel>)(root, criteriQuery, criteriaBuilder)->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("peopleAmount"), peopleAmount));
        }

        Page<Hotel> hotels = hotelRepository.findAll(specification, pageable);
        return hotels.getContent();
    }

    @Override
    public int countHotels(String city, double priceFrom, double priceTo, byte roomsAmount, byte peopleAmount) {

        Specification<Hotel> specification = (Specification<Hotel>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("city")), "%"+city.toUpperCase()+"%");

        if (priceFrom!=0){
            specification = specification.and((Specification<Hotel>)(root, criteriQuery, criteriaBuilder)->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceFrom));
        }

        if (priceTo!=0){
            specification = specification.and((Specification<Hotel>)(root, criteriQuery, criteriaBuilder)->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo));
        }

        if (roomsAmount!=0){
            specification = specification.and((Specification<Hotel>)(root, criteriQuery, criteriaBuilder)->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("roomsAmount"), roomsAmount));
        }

        if (peopleAmount!=0){
            specification = specification.and((Specification<Hotel>)(root, criteriQuery, criteriaBuilder)->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("peopleAmount"), peopleAmount));
        }

        return (int)hotelRepository.count(specification);
    }

}
