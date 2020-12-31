import javax.persistence.*;
import java.util.List;

public class Insert {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        String getOrderListJSH = "SELECT o FROM Order o WHERE o.orderAmount = 10000";
        String getOrderListYJS = "SELECT o FROM Order o WHERE o.orderAmount = 15000";

        tx.begin();

        try{
            TypedQuery<Order> getOrderJSHQuery = em.createQuery(getOrderListJSH, Order.class);
            TypedQuery<Order> getOrderYJSQuery = em.createQuery(getOrderListYJS, Order.class);

            List<Order> JSHOrder = getOrderJSHQuery.getResultList();
            List<Order> YJSOrder = getOrderYJSQuery.getResultList();

            Team codeSquad = new Team();
            codeSquad.setName("CodeSquad");
            em.persist(codeSquad);

            Team fLab = new Team();
            fLab.setName("F-Lab");
            em.persist(fLab);

            Member member1 = new Member();
            member1.setUsername("jsh");
            member1.setAge(25);
            member1.setTeam(codeSquad);

            for(Order o : JSHOrder){
                member1.getOrders().add(o);
                o.setMember(member1);
            }

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("yjs");
            member2.setAge(25);
            member2.setTeam(fLab);

            for(Order o : YJSOrder){
                member2.getOrders().add(o);
                o.setMember(member2);
            }

            em.persist(member2);

            tx.commit();

        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }
}
