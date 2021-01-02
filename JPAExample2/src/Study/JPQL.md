# JPQL

우리가 지금 까지한 JPA 는 Entity 중심으로 쿼리가 진행됬다. 근데 우리는 객체 중심으로 쿼리가 진행되야할 이유가 있다. <br>
만약에 테이블 내의 30살 이상의 유저들을 모두 검색하려면 우리가 현재까지 해오던 방식으로는 찾기가 힘들다..<br>
그래서 JPA 를 제대로 사용하기 위해서는 JPQL 을 학습해야 한다.<br>

## 특징

- **테이블이 아닌 객체를 대상으로 검색하는 객체지향 쿼리다.**

- SQL을 추상화해서 특정 데이터베이스 SQL 에 의존하지 않는다.

## 간단한 구현

```java
String jpql = "select m from Member as m where m.username = 'kim'";
List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();
```

- 회원이름이 Kim 인 엔티티를 조회한다.

## Criteria 쿼리 소개

- Builder 패턴으로 설계된 클래스로, Builder 형태로 Query 를 이어 붙여서 사용할 수 있다.

## 예시 코드

```java
        CriteriaBuilder cb =  em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);

        Root<Member> m = query.from(Member.class);

        CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("name"), "roach"));

        List<Member> resultList = em.createQuery(cq).getResultList();
        
        for(Member mem : resultList){
            System.out.println("mem.getId() = " + mem.getId());
        }
```

- 아래와 같은 쿼리가 나간다.

```roomsql
Hibernate: 
    select
        m1_0.member_id,
        m1_0.createdBy,
        m1_0.createdDate,
        m1_0.lastModifiedBy,
        m1_0.lastModifiedDate,
        m1_0.city,
        m1_0.street,
        m1_0.zipcode,
        m1_0.name,
        m1_0.team_id,
        m1_0.endDate,
        m1_0.startDate,
        m1_0.work_city,
        m1_0.work_street,
        m1_0.work_zipcode 
    from
        Member as m1_0 
    where
        m1_0.name = ?
```


## 특징

- 컴파일 시점에 오류를 발견할 수 있다. => 이게 가장 큰 장점 같다. 런타임상에서 오류를 발견하지 않는 것이.

# QueryDSL

- QueryDSL 도 JPQL 의 빌더 역할을 한다. QueryDSL의 장점은 코드 기반이면서 단순하고 사용하기 쉽다.
- QueryDSL의 장점은 코드 기반이면서 단순하고 사용하기 쉽다.
