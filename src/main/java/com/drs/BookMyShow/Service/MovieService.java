package com.drs.BookMyShow.Service;

import com.drs.BookMyShow.Dto.MovieDto;
import com.drs.BookMyShow.Exception.ResourcesNotFoundException;
import com.drs.BookMyShow.Model.Movie;
import com.drs.BookMyShow.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieDto createMovie(MovieDto movieDto){
        Movie movie =  mapToEntity(movieDto);
        Movie saveMovie = movieRepository.save(movie);
        return mapToDto(saveMovie);
    }

    public MovieDto getMovieById(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new ResourcesNotFoundException("Movie not found with this " + id));
        return mapToDto(movie);
    }

    public List<MovieDto> getAllMovie(){
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMovieByLanguage(String language){
        List<Movie> movies = movieRepository.findByLanguage(language);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public List<MovieDto> getMovieByGenre(String genre){
        List<Movie> movies = movieRepository.findByGenre(genre);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> searchMovie(String title){
        List<Movie> movies = movieRepository.findByTitleContaining(title);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public MovieDto updateMovie(Long id , MovieDto movieDto){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new ResourcesNotFoundException("Movie not found with this " + id));
        movie.setTitle(movieDto.getTitle());
        movie.setDiscription(movieDto.getDiscription());
        movie.setGenre(movieDto.getGenre());
        movie.setLanguage(movieDto.getLangague());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        Movie updatedMovie = movieRepository.save(movie);
        return mapToDto(updatedMovie);
    }

    public void deleteMovie(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new ResourcesNotFoundException("Movie not found with this " + id));
        movieRepository.delete(movie);
    }

    public MovieDto mapToDto(Movie movie){
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDiscription(movie.getDiscription());
        movieDto.setLangague(movie.getLanguage());
        movieDto.setGenre(movie.getGenre());
        movieDto.setDurationMins(movie.getDurationMins());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setPosterUrl(movie.getPosterUrl());
        return movieDto;
    }

    public Movie mapToEntity(MovieDto movieDto){
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDiscription(movieDto.getDiscription());
        movie.setGenre(movieDto.getGenre());
        movie.setLanguage(movieDto.getLangague());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        return movie;
    }
}
