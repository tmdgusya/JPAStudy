import com.sun.tools.corba.se.idl.constExpr.Or;

import javax.persistence.*;

public class oderInset {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        Address seoul = new Address("Seoul", "Center-Load", "42-1");
        Address ansan = new Address("Ansan", "Sanglok-su", "300-1");
        tx.begin();

        try{
            Order order1 = new Order();
            order1.setAddress(seoul);
            order1.setOrderAmount(10000);
            em.persist(order1);

            Order order2 = new Order();
            order2.setAddress(ansan);
            order2.setOrderAmount(15000);
            em.persist(order2);

            Product MemoJang = new Product();
            MemoJang.setName("MemoJang");
            MemoJang.setPrice(1000);
            MemoJang.setStockAmount(80);
            MemoJang.setOrder(order1);
            em.persist(MemoJang);

            Product GuJang = new Product();
            GuJang.setName("GuJang");
            GuJang.setPrice(1100);
            GuJang.setStockAmount(70);
            GuJang.setOrder(order2);
            em.persist(GuJang);

            tx.commit();

        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

}
