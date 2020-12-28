package Chapter02;

import Chapter01.member.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class RefactoringMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Team team = new Team();
        team.setName("Red");
        em.persist(team);

        Person person = new Person();
        person.setName("jsh");
        person.setTeam(team);
        em.persist(person);
        // team.getMembers().get(0).setTeam(team2) 이런 식으로 해도 적용안됨! 왜냐면 주인관계가 아닌 곳에서는 불가능함

        team.getMembers();
        try {
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }


    }


}
