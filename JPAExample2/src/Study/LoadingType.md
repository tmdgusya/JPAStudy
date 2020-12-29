# 즉시 로딩과 지연 로딩

아까 방식으로 봤을때 회원 엔티티를 조회할때 실제 데이터 기준으로 조회 쿼리를 날리는것이 효율적인 것을 알게 되었다. <br>
그렇담 우리는 JPA 가 어떤 방식으로 지원해주는지 알아야 한다.

## 즉시 로딩

- 엔티티를 조회할 때 연관된 엔티티도 함께 조회한다.
- 예시) em.find 를 조회할 때 연관된 엔티티도 함께 조회한다.
- 설정 방법 : @ManyToOne(fetch = FetchType.EAGER)

## 지연 로딩

- 연관된 엔티티를 실제 사용할 때 조회한다.
- 예시) member.getTeam().getName() 처럼 실제 Team 객체를 사용할때 JPA 가 SQL 을 생성해서 팀 엔티티를 조회한다.
- 설정 방법 : @ManyToOne(fetch = FetchType.LAZY)

## 즉시 로딩 예시 코드

Member.class 의 ManyToOne에 fetch = fetchType.EAGER 를 붙여준다. <br>
위와 같은 Annotation 에 config 를 해주면, em.find(Member.class, "member"); 를 하게되면, <br>
Team team - member.getTeam();

쿼리를 확인하면 회원을 조회하는 순간 team 에 대한 값들도 함께 조회하게 된다. <br>
여기서 JPA 가 쿼리를 두번 조회할 것 같지만, **즉시 로딩을 최적화 하기 위해서 가능하면 조인쿼리를 사용한다.** <br>

아래 쿼리가 위의 코드를 수행했을때 일어나는 일들이다. 

```roomsql
Hibernate: 
    select
        m1_0.member_id,
        m1_0.createdBy,
        m1_0.createdDate,
        m1_0.lastModifiedBy,
        m1_0.lastModifiedDate,
        m1_0.city,
        m1_0.name,
        t1_0.team_id,
        t1_0.name,
        m1_0.zipcode 
    from
        Member as m1_0 
    left outer join
        Team as t1_0 
            on m1_0.team_id = t1_0.team_id 
    where
        m1_0.member_id = ?
```

여기서 유심히 보고 갈 점이 하나 있다. outer Join 을 하는 이유이다. 지금 team Entity 를 보면 member 에 대해 nullabe 이다.<br>
그리고 member 도 team 에 대해 nullabe 이므로, 만약 팀에 들지 않은 멤버라면 inner join 의 경우 조건문에 해당하지 않아 조회되지 않는다. <br>
따라서 outerjoin 을 사용하는 것이다. 만약에 team 과 멤버에 대해 nullabe=false 를 주게되면 JPA 는 inner join 을 한다.

### 지연 로딩 예시 코드

이 방법의 경우에는 **fetch = fetchType.LAZY** 를 사용한다.

지연 코딩도 위의 코드와 같이 실행시켜 보겠다. 어라 이게 똑같은 코드인데 지연코드는 나오는것이 다르다. 아래를 보자.

```roomsql
Hibernate: 
    select
        m1_0.member_id,
        m1_0.createdBy,
        m1_0.createdDate,
        m1_0.lastModifiedBy,
        m1_0.lastModifiedDate,
        m1_0.city,
        m1_0.name,
        m1_0.team_id,
        m1_0.zipcode 
    from
        Member as m1_0 
    where
        m1_0.member_id = ?
```

- 해당 Member 객체만 조회하고 있다. 왜 이런것일까? 공통적으로 사용한 코드를 보자

```java
            Member member1 = em.find(Member.class, 1L);
            Team findTeam = member1.getTeam();
            tx.commit();
```

아직 얻어온 findTeam 에 대해서는 사실상 수행하는 얻어올 데이터가 없다. 그렇담 우리가 EAGER 즉 즉시 로딩이랑 같은 기능을 수행하려면,
아래와 같은 코드를 실행하면 된다.

```java
            Member member1 = em.find(Member.class, 1L);
            Team findTeam = member1.getTeam();
            findTeam.getName();
            tx.commit();
```

위의 코드를 실행시키면 아래와 같은 쿼리가 나온다. SELECT 를 두번이나 날린다... 조인을 이용하지 않고, 하지만 데이터가 실제로 사용할때만
쿼리를 날린다는 점에선 정말 효율적이라고 볼 수 있다. 따라서 바로 불러와야 되는 상황이 아니라면, 지연로딩이 조금 더 효율적인 코드이지 않을까 라는 생각이 든다.
조금 더 공부해봐야 알것 같다!

```roomsql
Hibernate: 
    select
        m1_0.member_id,
        m1_0.createdBy,
        m1_0.createdDate,
        m1_0.lastModifiedBy,
        m1_0.lastModifiedDate,
        m1_0.city,
        m1_0.name,
        m1_0.team_id,
        m1_0.zipcode 
    from
        Member as m1_0 
    where
        m1_0.member_id = ?
JSH
Hibernate: 
    select
        t1_0.team_id,
        t1_0.name 
    from
        Team as t1_0 
    where
        t1_0.team_id = ?
RED
```

### 지연 로딩 활용

- 회원은 Team 하나에만 속할 수 있다.
- 회원은 여러 주문 내역을 가진다.
- 주문 내역은 상품정보를 가진다.

### 어플리케이션 분석

- 회원과 팀은 항상 같이 쓰여, 즉시 로딩으로 설정했따.
- 회원과 주문은 가끔 사용되었다. 예를 들면 회원정보를 볼때는 딱히 주문정보는 필요하지않다. 그래서 지연 로딩으로 설정했다.
- 주문과 상품은 즉시 로딩으로 설정했다. 주문에는 항상 상품이 보여야 하므로 즉시 로딩으로 하는것이 효율적이다.

## JPA 의 기본 Fetch 전략

- @ManyToOne, @OneToOne : 즉시 로딩
- @OneToMany, @ManyToMany : 지연 로딩

- JPA 의 기본 전략은 연관된 Entity 가 하나면 즉시 로딩을 , 컬렉션이면 지연로딩을 이용한다. 컬렉션을 로딩하는 것은 너무 많은 비용이 들 수 있기 때문이다.
김영한님의 추천방법을 일단 지연로딩으로 전부 사용하되, 어플리케이션이 개발 단계가 마쳐갈때, 사용 상황을 보고 즉시 로딩으로 결정할지 여부를 정하는 것이 좋다고 말하고 있다.
짬에서 나오는 바이브기 때문에 일단 믿고 써본다.. 근데 이 말이 맞는 것 같는게, 비용 계산을 실제로 보고 정하는 것이 맞다고 판단된다. 컬렉션일 경우 비용이 
천문학적으로 커질 수 있기 때문이다..

# 영속성 전이 : CASCADE

- 특정 엔티티를 속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶으면 영속성 전이 기능을 사용하면 된다.
- 그니까 쉽게말하면 아까 멤버 엔티티를 영속성으로 만들때 연관된 팀테이블도 영속성으로 만들고 싶다면 해당 기능을 이용하면 된다는 것이다.

코드로 설명하는게 더 빠르다. 코드를 보자!

```java
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            child1.setParent(parent);
            child2.setParent(parent);
            parent.getChildren().add(child1);
            parent.getChildren().add(child2);
            em.persist(parent);
            tx.commit();
```

위의 코드가 영속성 전이를 이용한 뒤 Main 에서 작동하는 코드이다. 원래같으면 child 멤버들도, persist 에 넣어야 하지만,
parent 를 영속 시키면서 영속이 전이되어 따로 코드를 쓸 필요가 없어졌다.
자여기서 코드를 많이 효율적으로 쓰려면 어떻게 해야할까? 즉, One 에 cascade 옵션을 쓰는것이 맞다. 그것이 코드를 줄이는 일이기 때문이다.

## 고아 객체 (이거 꿀 기능인듯 하다..)

- JPA는 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 기능을 제공하는데 이를 고아객체라고 한다.
옵션은 orphanRemoval = true 로 주면 된다.