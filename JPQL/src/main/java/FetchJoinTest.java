import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinTest {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        String fetchJoinTest = "select m from Member m join fetch m.team";
        String fetchJoinCollection = "select t from Team t join fetch t.members where t.name = 'CodeSquad'";

        List<Team> teams = em.createQuery(fetchJoinCollection, Team.class).getResultList();
        
        for(Team t : teams){
            System.out.println("t.getUsername() = " + t.getName());
            List<Member> members = t.getMembers();
            if(members!=null){
                for(Member m : members){
                    System.out.println("->>> m.getUsername() = " + m.getUsername());
                }
            }
        }
    }
}
