package com.drs.BookMyShow.Repository;

import com.drs.BookMyShow.Model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat,Long> {

    List<ShowSeat> findByMovieId(Long movieId);

    List<ShowSeat> findByShowIdAndStatus(Long showId , String status);

}
