package Relation3;

import Chapter01.member.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Album album = new Album();
            album.setName("album1");
            album.setPrice(10000);
            em.persist(album);

            Movie movie = new Movie();
            movie.setActor("jsh");
            movie.setDirector("jsh_dl");
            movie.setName("tenet");
            movie.setPrice(8000);
            em.persist(movie);

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
    }


}
