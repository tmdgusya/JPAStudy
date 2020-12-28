package Relation04;

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
            Parent parent = new Parent();
            parent.setId1("myId1");
            parent.setId2("myId2");
            parent.setName("parentName");
            em.persist(parent);

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
    }
}
