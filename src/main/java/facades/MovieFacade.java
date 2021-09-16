/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.MovieDTO;
import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author mikke
 */
public class MovieFacade {
    private static MovieFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private MovieFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    
    public MovieDTO create(MovieDTO mvDto){
        Movie mv = new Movie(mvDto.getYear(),mvDto.getTitle(),mvDto.getActors());
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(mv);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new MovieDTO(mv);
    }
    public MovieDTO getById(long id){
        EntityManager em = emf.createEntityManager();
        return new MovieDTO(em.find(Movie.class, id));
    }
    
     public List<MovieDTO> getAll(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
        List<Movie> movies = query.getResultList();
        return MovieDTO.getDtos(movies);
    }
     
     public Integer countMovies(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(m.id) from Movie m", Long.class);
        return query.getSingleResult().intValue();
     }
     
     public MovieDTO getMovieByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        MovieDTO dto = null;
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m from Movie m where m.title = :title", Movie.class);
            query.setParameter("title", title);
            dto = new MovieDTO(query.getSingleResult());
            return dto;
        } catch(NoResultException e){
            return dto;
        }
            finally {
            em.close();
        }
    }
     
}
