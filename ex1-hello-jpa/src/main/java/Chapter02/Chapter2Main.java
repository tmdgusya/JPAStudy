package Chapter02;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Chapter2Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Person person = new Person();
        Team team = new Team();
        team.getMembers().add(person);
        person.setName("jsh");
        person.setTeam(team);
        em.persist(person);


        team.setName("Red");
        em.persist(team);
        team.getMembers();
        try{
            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }
    }
}
