import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin(); // Transaction 시

        try{

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            child1.setParent(parent);
            child2.setParent(parent);
            parent.getChildren().add(child1);
            parent.getChildren().add(child2);
            em.persist(parent);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
    }
}
