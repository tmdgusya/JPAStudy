# Entity Manager

- **Entity Manager 는 Entity 를 수정하고, 삭제하고, 조회하는 등 엔티티와 관련된 모든 일을 처리합니다.** 말그대로 Manager
- Entity Manger 는 하나가 제공되야 하므로, Factory를 통한 Method 를 통해 공급한다.

```java
EntityManagerFactory emf = Persistence.createEntityMangerFactory("persistence_xml_name");
EntityManger em = emf.createEntityManger();
```

- EntityManagerFactory 엔티티 매니저는 여러 스레드가 동시에 접근해도 안전하므로 서로 다른 스레드 간에 공유해도 되지만,
  엔티티 매니저는 여러 스레드가 동시에 접근하면 동시성 문제가 발생하므로 스레드 간에 절대 공유하면 안된다.
- EntityManager 는 트랜잭션을 이용할때만 커넥션을 획득한다 => 이건 상당히 중요함. 내가 다니던 회사에서는 TypeORM 이라는 라이브러리를 이용했는데,
  해당 ORM 이 유휴 Connection 을 계속 유지해서 관리하기 힘들었었다. 그래서 Connection 을 자동적으로 적용해주는건 좋은 기능인 것 같다.

# Persistence Context

- **엔티티를 영구 저장하는 환경**
- 우리가 이용했던 em.persiste(Entity); 를 담았던 코드는 영속성 컨텍스트에 이제 이 Entity의 관리를 맞기겠다는 것이다.
- **IoC (Inversion of Context)** 중 하나라고 생각하면 편하다. => Entity 의 관리를 사용자에게 맞기는 것이 아닌 PersistContext 에 맞기는 것이다.

# Entity 의 생명 주기

- 비영속 : 영속성 컨텍스트와 전혀 관계가 없는 상태
- 영속 : 영속성 컨텍스트에 저장된 상태
- 준영속 : 영속성 컨텍스트에 저장되었다가 분리된 상태
- 삭제 : 삭제된 상태

# 영속성 컨텍스트의 특징

- **영속성 컨텍스트와 식별자 값**

  - 영속성 컨텍스트는 엔티티를 식별자 값으로 구분한다. 따라서 영속상태는 식별자 값이 반드시 있어야 한다. 식별자 값이 없으면 예외가 발생한다.
  - 위의 내용은 매우 중요하다. Entity 를 구별하는 식별자는 보통 PK 를 이용하는데, 기본키 Mapping 전략시 IDENTITY 전략의 경우 DB 에 완전히 기본키 권한을 위임한다. 따라서 DB 에 저장되고 FK를 얻어온뒤, Entity를 저장하게된다. => 이런 이유로 IDENTITY 전략은 **쓰기 지연** 을 지원해주지 못한다.

- **영속성 컨텍스트와 데이터 베이스 저장**
  - 영속성 컨텍스트에 엔티티를 저장한다고, 바로 데이터 베이스에 저장되지 않는다. Transaction 단계의 Flush 혹은 Commit 이 일어나야 DataBase 에 저장된다. 정확히말하면 Flush 가 될때 저장된다.

## 1차 캐시

- Entity Manager 를 써서 얻을 수 있는 장점으로는, 1차 캐시를 이용할 수 있다는 점이다. 우리가 보통 백엔드 측에서 블락이 걸릴 수 있는 작업들은 I\O 작업들이 대부분이다. 그런데 이 부분을 빠르게 해결 할 수 있는 부분으로 Cache 상에 있는 Entity 의 정보를 획득하여 있으면 전달해주고, 없을시 DB 에 가서 조회한뒤, 다시 Cache 로 저장한다. 정말 멋진 기능이다. 우리가 DB 로 가지 않고도, 값을 알아낼 수 있는 것이다. 다만 Cache 이므로 RunTime 시 이다.

## 동일성 보장

- Member a = em.find(Member.class, "member1");
- Member b = em.find(Member.class, "member1");
- a == b 는 참인가? 해당 값을 호출하면 당연히 참이 나온다. 왜냐 우리는 영속성 관리자에 저장되어 있는 Entity 의 정보, 즉 1차 캐시에 있는 정보를 가져오는 것이므로, 당연히 같은 인스턴스를 제공받게 된다.

## 쓰기 지연

- 엔티티 매니저는 트랜잭션을 Commit 하기 전까지, 데이터 베이스에 저장하지 않고, 내부 Query Store 에 저장한뒤, 플러시가 일어날때, 데이터 베이스로 보내는데 이를 **쓰기 지연**이라고 한다.

## 변경 감지

- JPA 는 1차 Cache 의 저장소에 SnapShot 으로 Entity 의 형태를 남겨둔다. 그리고 스냅샷 시점과 엔티티를 비교해서 변경된 데이터를 찾는다.
- flush() => Entity 의 변경 내용을 데이터베이스로 전달한다.

### 단계

- 트랜잭션을 커밋하면 엔티티 내부에서 먼저 플러시가 호출 된다.
- 엔티티와 스냅샷을 비교해서 변경된 엔티티를 찾는다.
- 변경된 엔티티가 있으면, 수정 쿼리를 생성해서 쓰기 지연 SQL 저장소에 보낸다.
- 쓰기 지연 저장소의 SQL 을 데이터베이스로 보낸다.
- 데이터 베이스 트랜잭션을 커밋한다.

- **제일 중요한 점은 영속성 관리 상태에 들어가 있어야만, 변경 감지가 지원된다. Observable 이랑 비슷하게 생각해라!**

## merge()

- 준영속 상태의 엔티티 => 영속상태의 Entity
- 가끔 보면 merge() 를 이용해 변경된 부분을 반영하려고 하는데 이는 상당히 안좋은 행위로 보인다.
- 아래코드를 보고 따라해보자 .. 코드는 간략히 Pseudo Code 처럼적겠다.

```java
public class Merge{

    member("roach",24);
    persist(member)

    em.close();

    ====after====
    # 현재 멤버는 준영속 상태
    member1.setAge(25)
    em1.merge(member1) => 영속상태

    em1.contains(member) => false
    em1.contains(member1) => true
}
```

- 이 과정에서 contains 가 같은 인스턴스로 나와야 하는데 왜 false true 가 다르지? 라고 생각하시는 분이 있으 실 것같다.
- 해당 과정에서 처음에 em 을 close 하게 되고, member 는 준영속 상태가 되게 된다.
- 그리고 그 뒤 25살로 나이를 변경해도 준영속 상태이므로, 변경 감지를 눈치 챌 수 없다. 따라서 em1.merge(member1)은 현재 값이 없는 Entity 로
  DB 조회 후 DB 에서 값을 찾은 뒤, 새롭게 반환하는 Instance 이므로 다른 Instance 를 반환하는 것이다. 즉 변경감지를 이용하는 것이 아니므로, Instance 도 낭비되고, 1차 Cache 또한 이용할 수 없다. 이점이 정말 merge() 의 큰 단점 중 하나 이므로 기억해두길 바란다.
