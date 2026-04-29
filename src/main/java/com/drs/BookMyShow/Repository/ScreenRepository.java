package com.drs.BookMyShow.Repository;


import com.drs.BookMyShow.Model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen,Long> {

    Optional<Screen> findByTheaterId(Long theaterId);
}
