import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JoinMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        String Joinquery = "SELECT m FROM Member m INNER JOIN m.team t " +
                "WHERE t.name = :teamName";
        List<Member> members = em.createQuery(Joinquery, Member.class)
                .setParameter("teamName","CodeSquad")
                .getResultList();

        for(Member member : members){
            System.out.println("member.getUsername() = " + member.getUsername());
        }
    }

}
