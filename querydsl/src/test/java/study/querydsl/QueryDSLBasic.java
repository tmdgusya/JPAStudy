package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class QueryDSLBasic {

    @Autowired
    EntityManager em;

    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    public void before(){

        jpaQueryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("A");
        Team teamB = new Team("B");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL(){
        Member findByJPQL = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        Assertions.assertThat(findByJPQL.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQueryDSL(){
        QMember m = QMember.member;

        Member member = jpaQueryFactory.select(m).from(m).where(m.username.eq("member1")).fetchOne();
        Assertions.assertThat(member.getUsername()).isEqualTo("member1");
    }
}
