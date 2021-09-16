/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.MovieDTO;
import dtos.RenameMeDTO;
import entities.Movie;
import entities.RenameMe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        FacadeExample fe = FacadeExample.getFacadeExample(emf);
        fe.create(new RenameMeDTO(new RenameMe("First 1", "Last 1")));
        fe.create(new RenameMeDTO(new RenameMe("First 2", "Last 2")));
        fe.create(new RenameMeDTO(new RenameMe("First 3", "Last 3")));
        
        
        MovieFacade mf = MovieFacade.getMovieFacade(emf);
        mf.create(new MovieDTO(new Movie(1990,"Det virkede 1",new String[] {"test1","test1"})));
        mf.create(new MovieDTO(new Movie(2000,"Det virkede 2",new String[] {"test2","test2"})));
        mf.create(new MovieDTO(new Movie(2010,"Det virkede 3",new String[] {"test3","test3"})));
    }
    
    public static void main(String[] args) {
        populate();
    }
}
