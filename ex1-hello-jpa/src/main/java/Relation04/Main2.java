//package Relation04;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//
//public class Main2 {
//
//    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//
//        tx.begin();
//
//        try{
//            Parent_ parent_ = new Parent_();
//            ParentID_ parentID_ = new ParentID_("myId1", "myId2");
//            parent_.setId(parentID_);
//            parent_.setName("parentName");
//            em.persist(parent_);
//
//            tx.commit();
//        }catch (Exception e){
//            tx.rollback();
//        }
//
//
//
//    }
//}
