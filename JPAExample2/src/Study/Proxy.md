# 막 적기

아 처음 프록시를 봤을때 정의가 객체는 객체 그래프로 연관된 그래프를 탐색한다. 그런데 객체가 데이터 베이스에 저장되어 있으므로 연관된 객체를 마음껏 탐색하기는 어렵다.
라는 말이 적혀있었다.. 이게 무슨소리지? 객체는 객체 그래프라는 뜻을 이해했는데, 객체가 데이터 베이스에 저장되어 있다니??.. 뭐 Entity 객체가 Database 에 저장되어 있긴
하니까 맞다. 근데 이말이 나에게는 뭔가 확 와닿지 않았.. <br>
그래서 다른 방식으로 최대한 이해해 보려고 했고, 다책을 보다보니 딱이해하기 좋은 예제가 나왔다. 아래를 한번보자

## 예제

### 회원과 팀 정보를 출력하는 비즈니스 로직

```java
pubilc void printUserAndTeam(String memberId){
    Member member = em.find(Member.class, memberId);
    Team team - member.getTeam();
    System.out.println("회원 이름 : " + member.getUsername());
    System.out.println("소속 팀 : " + team.getName());
}
```

### 회원 정보만 출력하는 비즈니스 로직

```java
public String printUser(String memberId){
    Member member = em.find(Member.class, memberId);
    System.out.println("회원 이름 : " + member.getUsername());
}
```

### 필요한 이유 분석

위의 함수를 보면 printUserAndTeam 은 Team 의 정보가 필요하므로, Team Entity 까지 조회하는 것이 맞다. 하지만 printUser 경우는
TeamEntity 는 조회될 필요가 없다. 따라서 printUser 에서 em.find 를 날리게 되면, team 정보 까지 조회해서 오므로 비효율적이므로, 지연 로딩은 해당
team.getName() 이 실행되는 시점에서 조회 쿼리를 날리게 된다. printUser 처럼 team 에 대한 정보를 얻는 문이 없다면, 팀 엔티티에 대한 조회를 하지 않는다.
자 ! 그렇담 지연로딩을 하기 위해 필요한 것은 무엇일까? 앞에서 설명한 프록시이다.

### 프록시 서론..

JPA 에서 Entity 하나를 조회하는데 em.find() Method 를 이용한다. 이 메소드는 영속성 컨텍스트에 데이터가 없으면 데이터 베이스를 조회한다. <br>
이렇게 엔티티를 직접 조회하면 조회한 엔티티를 실제 사용하든 사용하지 않든 데이터 베이스를 조회하게 된다. <br>
위에서 설명한 방식대로 데이터가 실제 사용되는 시점까지 조회쿼리를 미루고 싶다면 em.getReference() 를 이용하면 된다. <br>
Member member = em.getReference(Member.class, "member1");

이 메소드는 호출될 때 DB 를 조회하지도 않고, 실제 엔티티 객체도 생성하지도 않는다. 대신에 데이터 베이스 접근을 위임한 프록시 객체를 반환한다.

### 프록시의 특징

- 프록시 클래스는 실제 클래스를 상속받아서 만들어지므로 실제 클래스와 겉 모양이 같다.
- 따라서 사용하는 입장에서는 이것이 진짜 객체인지 프록시 객체인지 구분하지 않고 사용해도 된다.
- 프록시 객체는 실제 객체에 대한 참조를 보관한다(adress 를 보관 한다는 뜻 인거 같음 => 한번 확인해볼 필요가 있음)
- 그리고 프록시 객체의 메소드를 호출하면 프록시 객체는 실제 객체의 메소드를 호출한다.

### 프록시 객체의 초기화

- 프록시 객체는 member.getName() 처럼 실제 사용될 때 데이터 베이스를 조회해서 실제 엔티티 객체를 생성하는데 이것이 **프록시 객체의 초기화** 라고 한다.

### 프록시 동작 방식

1. 프록시 객체에 member.getName() 을 호출해서 실제 데이터를 조회한다.
2. 프록시 객체는 실제 엔티티가 생성되어 있지 않으면 영속성 컨텍스트에 실제 엔티티 생성을 요청하는데 이것을 초기화라 한다.
3. 영속성 컨텍스트는 데이터베이스를 조회해서 실제 엔티티 객체를 형성한다.
4. 프록시 객체는 생성된 실제 엔티티 객체의 참조를 Member taget 이라는 멤버변수에 저장한다.
5. 프록시 객체는 실제 엔티티 객체의 getName() 을 호출해서 결과를 반환한다.

### 주의할 점 

준영속 상태의 Entity 를 Proxy 를 이용하여 초기화를 시도하게 되면, LazyInitail ~ 머시기 오류가난다. <br>

### 프록시와 식별자

엔티티를 프록시로 조회할 때 식별자 값을 파라미터로 전달하는데 프록시 객체는 이 식별자 값을 보존한다.

### 프록시 초기화 확인 및 객체 확인

boolean isLoad = emf.getPersistenceUnitUtil().isLoaded(entity); <br>

위의 코드에서 false 가 리턴된다면, 프록시 객체는 아직 초기화 되지 않았다, 혹은 이미 초기화 되었거나 프록시 인스턴스가 아니면 true를 반환한다.

- 조회한 엔티티가 진짜 엔티티인지 프록시로 조회한 것인지 확인하려면 클래스명을 직접 출력해보면 된다.
member.getClass().getName() => jpabook.~.Member_$$_javassist_0 이면 프록시 객체이다.
