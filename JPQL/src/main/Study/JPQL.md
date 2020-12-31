# JPQL

JPQL 은 객체 지향 쿼리언어이다. 따라서 테이블을 대상으로 쿼리하는 것이 아니라, 엔티티 객체를 대상으로 쿼리한다. <br>
JPQL 은 SQL 을 추상화해서 특정 데이터베이스 SQL 에 의존하지 않는다. <br>
JPQL 은 결국 SQL로 변환된다. <br>

## SELECT

```java
String jpql = "SELECT m FROM Member AS m WHERE m.username = 'yjs'"
```
* 특징 *

## 대소문자 구분

- 엔티티와 속성은 대소문자를 구분 한다. 아까 오류가 진짜 많이났었는데. 지금 내 Order 에는 orderAmount 속성이 있는데 <br>
- 반면에 SELECT, FROM, AS 같은 JPQL 키워드는 대소문자를 구분 하지 않는다.

## 엔티티 이름

- JPQL 에서 사용한 Member 는 클래스 명이 아니라 명이다. 

## 결과 조회

```roomsql

 String getOrderListJSH = "SELECT o FROM Order o WHERE o.orderAmount = 10000";
 String getOrderListYJS = "SELECT o FROM Order o WHERE o.orderAmount = 15000";

 TypedQuery<Order> getOrderJSHQuery = em.createQuery(getOrderListJSH, Order.class);
 TypedQuery<Order> getOrderYJSQuery = em.createQuery(getOrderListYJS, Order.class);
 
```

## 파라미터 바인딩

```roomsql

 String getOrderListJSH = "SELECT o FROM Order o WHERE o.orderAmount = :amount";
 String getOrderListYJS = "SELECT o FROM Order o WHERE o.orderAmount = :amount";

 TypedQuery<Order> getOrderJSHQuery = em.createQuery(getOrderListJSH, Order.class).setParameter(10000);
 TypedQuery<Order> getOrderYJSQuery = em.createQuery(getOrderListYJS, Order.class).setParameter(15000);

```

## 프로젝션

- SELECT 절에 조회할 대상을 지정하는 것을 프로젝션이라 하고 [SELECT 프로젝션 대상 FROM ] 으로 대상을 선택한다.
- 프로젝션 대상은 **엔티티, 엠비디드 타입, 스칼라 타입** 이 있다.

### 엔티티 프로젝션 

- 처음은 회원을 조회하고 두 번째는 회원과 연관된 팀을 조회했는데 둘 다 엔티티를 프로젝션 대상으로 사용했다.
- 쉽게 생각하면 원하는 객체를 바로 조회한 것인데 컬럼을 하나하나 나열해서 조회해야 하는 SQL 과는 차이가 있다.
- 참고로 이렇게 조회한 엔티티는 영속성 컨텍스트에서 관리된다.

### 임베디드 타입 프로젝션

- JPQL에서 임베디드 타입은 엔티티와 거의 비슷하게 사용한다.
- 임베디드 타입을 조회의 시작점이 될 수 없다는 제약이 있다. Entity를 통해서 조회의 시작점이 되어야 한다.
- 그니까 만약에 Address 로 조회를 날려도, 결국 Order Entity 가 시작점으로 바뀌게 된다.

### 스칼라 타입 프로젝션

- 숫자, 문자, 날짜와 같은 기본 데이터 타입들을 스칼라 타입이라고 한다.
- 예를들어 전체 회원의 이름을 조회하려면 다음처럼 쿼리하면 된다.

### NEW 명령어

- DTO 를 이용한 방법으로 난 개인적으로 이 방식이 상당히 친숙한것 같다.
- 첫번째로 List 를 생성하고, DTO 객체를 받아온 값마다 생성해 주어 add 방식이다.

```java
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
```

### 페이징 API

- 페이징 처리를 DB 에서 제대로 공부하고 보자!
- 근데 사실 페이징 처리에 관련한 Example 이 적혀있으므로, 나중에 DB 공부할때 JPA 에 연동해서 실험해도 좋을듯

### 집합과 정렬

- 집합은 집함함수와 함께 통계 정보를 구할 때 사용한다.

- COUNT : 결과 수를 구한다. **반환타입 : LONG**
- MAX, MIN : 최대, 최소 값을 구한다. 문자, 숫자, 날짜 등에 사용한다.
- AVG : 평균값을 구한다. 숫자타입만 사용할 수 있다. **반환타입 : Double**
- SUM : 합을 수한다. 숫자 타입만 사용할 수 있다. 반환 타입 : **정수합 : Long, 소수합 : Double etc..**

#### 주의할점!

- NULL 값은 무시하므로 통계에 잡히지 않는다, 즉 null 과 0 혹은 값을 지니지 않은 컬럼을 통계로 계산하려 들면 안됨.
- 만약 값이 없는데, SUM, AVG, MAX, MIN 함수를 사용하면 NULL 값이 된다. 단 COUNT 는 0 이 된다.

### JPQL 조인

- SQL 조인 문과 상당히 흡사하다.

#### 내부 조인

- 내부 조인은 INNER JOIN 을 사용한다. 참고로 INNER 는 생략할 수 있다.

- 현재 DB 구조
```
TEAM_ID  	NAME  
28	CodeSquad
29	F-Lab
32	CodeSquad
33	F-Lab
36	CodeSquad
37	F-Lab
40	CodeSquad
41	F-Lab
```

```java
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        String Joinquery = "SELECT m FROM Member m INNER JOIN m.team t " +
                "WHERE t.name = :teamName";
        List<Member> members = em.createQuery(Joinquery, Member.class)
                .setParameter("teamName","codeSquad")
                .getResultList();

        for(Member member : members){
            System.out.println("member.getUsername() = " + member.getUsername());
        }
```

아래 조인문을 실행하면 결과는 아래와 같다.

```roomsql
Hibernate: 
    select
        m1_0.MEMBER_ID,
        m1_0.age,
        m1_0.TEAM_ID,
        m1_0.USERNAME 
    from
        Member as m1_0 
    inner join
        Team as t1_0 
            on m1_0.TEAM_ID = t1_0.TEAM_ID 
    where
        t1_0.name = ?
```

#### 결과

```
member.getUsername() = jsh
member.getUsername() = jsh
member.getUsername() = jsh
member.getUsername() = jsh
```

### Fetch Join

- 페치 조인은 SQL 에서 이야기하는 조인의 종류는 아니고, JPQL에서 성능 최적화를 위해 제공하는 기능이다.
- 연관된 엔티티나 Join 을 한꺼번에 조회하는 기능인데 join fetch 명령어로 사용할 수 있다.
- 자 백문이 불여일견이라고 한번 써보자!

```java
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        String fetchJoinTest = "select m from Member m join fetch m.team";

        List<Member> members = em.createQuery(fetchJoinTest, Member.class).getResultList();
        
        for(Member m : members){
            System.out.println("m.getUsername() = " + m.getUsername());
        }
```

해당 쿼리를 입력하면 아래와 같은 쿼리와 결과값이 나온다.

#### 쿼리
```roomsql
Hibernate: 
    select
        m1_0.MEMBER_ID,
        m1_0.age,
        t1_0.TEAM_ID,
        t1_0.name,
        m1_0.USERNAME 
    from
        Member as m1_0 
    inner join
        Team as t1_0 
            on m1_0.TEAM_ID = t1_0.TEAM_ID
```

#### 결과값

```
m.getUsername() = jsh
m.getUsername() = yjs
m.getUsername() = jsh
m.getUsername() = yjs
m.getUsername() = jsh
m.getUsername() = yjs
m.getUsername() = jsh
m.getUsername() = yjs
```

### 살펴보기

- 엔티티 페치 조인 JPQL 에서 select m 으로 회원 엔티티만 선택했는데 실행된 SQL 을 보면 SELECT M.*, T.* 으로 회원과 연관된
TEAM 도 모두 조회된것을 확인할 수 있습니다. 

```
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
                    System.out.println("m.getUsername() = " + m.getUsername());
                }
            }
        }
```

```
t.getUsername() = CodeSquad
->>> m.getUsername() = jsh
t.getUsername() = CodeSquad
->>> m.getUsername() = jsh
t.getUsername() = CodeSquad
->>> m.getUsername() = jsh
t.getUsername() = CodeSquad
->>> m.getUsername() = jsh
```

### 사용해야 하는 이유

- JPQL 은 결과를 반환할 떄 연관관계 까지 고려하지 않는다. 단지 SELECT 절에 지정한 엔티티만 조회할 뿐이다. 따라서 팀 엔티티만 조회하고, 연관된 회원 컬렉션은 조회하지 않는다.
만약 회원 컬렉션을 지연 로딩으로 설정하면 프록시나 아직 초기화 하지 않은 컬랙션 래퍼를 반환한다. 즉시 로딩으로 설정하면 회원 컬렉션을 즉시 로딩하게 되면 
쿼리를 한번 더 실행한다.