/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Movie;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mikke
 */
public class MovieDTO {
    private Long id;
    private int year;
    private String title;
    private String[] actors;

    public MovieDTO(int year, String title, String[] actors) {
        this.year = year;
        this.title = title;
        this.actors = actors;
    }

    public MovieDTO(Movie movie) {
        if (movie.getId() != null){ 
            this.id = movie.getId();
        }
        
        this.year = movie.getYear();
        this.title = movie.getTitle();
        this.actors = movie.getActors();
    }
    
    public static List<MovieDTO> getDtos(List<Movie> movies){
        List<MovieDTO> mvDtos = new ArrayList();
        movies.forEach(x->mvDtos.add(new MovieDTO(x)));
        return mvDtos;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    

    
    
    
    
    
}
