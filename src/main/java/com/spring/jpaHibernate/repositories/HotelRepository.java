package com.spring.jpaHibernate.repositories;

import com.spring.jpaHibernate.entities.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    List<Hotel> findAllByDeletedAtNull(Pageable pageable);
    List<Hotel> findAllByDeletedAtNull();
    int countAllByDeletedAtNull();
}
