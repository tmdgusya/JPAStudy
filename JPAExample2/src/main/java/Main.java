import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin(); // Transaction 시

        try{
//            Member member = new Member();
//            member.setName("roach");
//            member.getAddressesList().add(new Address("city", "street", "zipcode"));
//            member.getAddressesList().add(new Address("Seoul", "YeongdeungPo", "100"));
//            em.persist(member);
            Member member = em.find(Member.class, 12L);
            List<Address> addressesList = member.getAddressesList();
            for(Address addr : addressesList){
                System.out.println(addr.getCity());
            }
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
    }
}
