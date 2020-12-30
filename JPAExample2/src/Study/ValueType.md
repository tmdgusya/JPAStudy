# 값 타입

- 문제점 : 객체가 공유되면, 객체의 참조가 일어나 다른 Entity 에 영향을 미칠수 있음 => 이를 Side Effect 라고하는데, 
해당 문제는 발생하면 결함을 찾기가 힘듬.. 그래서 값타입은 Immutable 하게 생성해야됨.

생성될때만 수정이 가능하고, 그 이후로는 수정이 불가능해야함

# 값 타입 컬렉션을 쓰게 되면 조회시 지연로딩임.

```java
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
        m1_0.member_id = ?
Hibernate: 
    select
        a1_0.MEMBER_ID,
        a1_0.city,
        a1_0.street,
        a1_0.zipcode 
    from
        ADDRESS_LIST as a1_0 
    where
        a1_0.MEMBER_ID = ?
city
Seoul
```

값타입은 절대로 em.find 로 찾아온것으로 재젖의 하면 안된다. 예를 들면

```java
Member findMember = em.find(Member.class, 1L);
findMember.getAddress().setCity("Seoul"); => 이렇게 하면 절대로 안되고 아래와 같은 방식으로 해야된다.
findMember.setAddress(new Address("Seoul", "Center-Lo", "1000"))
```

객체 자체를 갈아끼워야 한다.