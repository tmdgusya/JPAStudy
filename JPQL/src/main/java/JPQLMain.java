import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class JPQLMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        List<UserDTO> userDTOS = new ArrayList<>();

        String jpql = "SELECT m.username, m.age FROM Member m";
        List<Object[]> resultList = em.createQuery(jpql).getResultList();
        
        for(Object[] row : resultList){
            UserDTO userDTO = new UserDTO((String) row[0], (Integer) row[1]);
            userDTOS.add(userDTO);
        }
        
        for(UserDTO userDTO : userDTOS){
            System.out.println("userDTO.getUsername() = " + userDTO.getUsername());
            System.out.println("userDTO.getAge() = " + userDTO.getAge());
        }
    }
}
