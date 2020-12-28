# 기본키 직접 할당 전략

```java
@Id()
@Column(name = "id")
private Long id;
```
- 자바 기본형
- 자바 Wrapper 형
    - String
    - java.util.Date
    - java.sql.Date
    - java.math.BigDecimal
    - java.math.BigInteger
* 기본 키 직접 할당 전략은 em.persist() 로 엔티티를 저장하기 전에 애플리케이션에서 기본키를 직접 할당하는 전략이다.

```java
Member member = new Member();
member.setId(1L);
```

# IDENTITY 전략

- 기본 키 생성을 데이터 베이스에 위임하는 전략이다. 주로 MySQL, PostgreSQL 등등에서 사용한다.
- 예를 들어 MySQL 의 AUTO_INCREMENT 등의 기능은 자동으로 기본키를 생성해준다.
```roomsql
CREATE TABLE MEMBER (
    id LONG NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCAHR(10)
);
```
- 해당 방식은 데이터 베이스가 테이블을 생성할때, 데이터 베이스에서 값이 들어올때마다 자동으로 ID 값을 추가해주는 전략이다.
- 스프링에서도 해당 방식을 이용가능하다
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
- 근데 어찌보면 해당 방식의 문제점이 보일 수도 있다. 우리가 앞에서 공부한바에 따르면 엔티티를 식별하기 위해서는
엔티티 구별자가 필요한데 그 역할을 Entity 의 Primary key 가 해왔다. 근데 해당 방식은 Entity Manager 에서
증가 시키는 방식이 아닌 DB 에서 증가시키는 방식이다. 따라서 트랜잭션을 커밋해야만 식별자를 알 수 있는 상황으로 JPA 가 지원하는
쓰기 지연 전략이 사용불가능하다.

# SEQUENCE 전략
- 데이터 베이스 시퀀스는 유일한 값을 순서대로 생성하는 특별한 **데이터 베이스 오브젝트** 이다.
- SEQUENCE 전략은 생성한 SEQUENCE 를 이용하여 기본 키를 생성한다.
- 이 전략은 SEQUENCE 를 지원해주는 DB 에서만 사용가능하다.

```roomsql
CREATE TABLE MENBER (
    id LONG NOT NULL PRIMARY KEY,
    name VARCAHR(10)
);

CREATE SEQUENCE MEMBER_SEQ START WITH 1 INCREMENT BY 1;
```

```java
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator{
    name = "MEMBER_SEQ_GENERATOR",
    sequenceName = "MEMBER_SEQ", // DB SEQUENCE Table
    initialValue = 1,
    allocationSize = 1 // 시퀀스 한번 호출에 증가하는 
}

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private Long id;
}
```
- 필수조건으로 사용할 데이터 베이스 시퀀스를 Mapping 해줘야 한다.
- 이 해당 조건을 사용하는 이유는 아까 IDENTITY 의 경우, transaction을 commit 하기 전까지는 Entity 식별자를 파악할 수 없으나
해당 전략은 em.persist() 호출할때, 데이터 베이스 시퀀스를 이용해서 Entity 의 식별자를 조회한다. 그리고 조회한 뒤에 Persistence Context 에 이를 저장한다!.
이후 Transaction 이 일어나면 DB에 Entity 의 내용을 저장시키는 Flow 다.수

# ~~Table 전략~~
* 음 해당방법은 아직 고려안해도 될듯하다. 사실 DB 와 통신을 두번하는데 큰 장점이 있나 싶다. 조금더 알아봐야 될듯함

# AUTO 전략
- 위에서 본 전략들은 특정 DB 에서는 안될 수도 있다. 즉 방언이 존재한다. 따라서 JPA 에서는 AUTO 라는 옵션을 제시해주는데,
AUTO 는 선택한 데이터베이스에 따라 전략을 자동 구성해준다! 정말 멋진 기능이다!

```java
import javax.persistence.Entity;
@Entity
public class Member{
    @Id() @GeneratedValue(strategy = GenrationType.AUTO)
    private Long id;
}
```

- 근데 방언에 따라 SEQENCE 나 TABLE 등이 선택될 경우가 있을것이다. 이경우 사용자가 시퀀스나 키테이블을 생성해 놓아야 한다.

# 기본키 Mapping 핵심 내용 정리
- 영속성 컨텍스트는 Entity 를 식별자로서 구분함!
- 따라서 해당 전략마다 식별자를 관리하거나 확인할 수 있는 시점을 유의하여 공부!
- 직접할당 : 직접 식별자를 할당하므로 이미 알고 있음
- SEQUENCE : em.persist() 가 일어날때 SEQENCE_TABLE 을 조회해서 식별자를 가져온 뒤 영속성 컨텍스트에 저장!
- TABLE : 데이터 베이스 시퀀스 생성용 테이블에서 식별자 값을 획득한 후 영속성 컨텍스트에 저장한다.
- IDENTITY : DB 에 위임하므로, DB 조회 전까지는 알수 없음 보통 Transaction commit 이 일어나면 자동 조회해서 영속성 컨텍스트에 저장함
데이터 베이스 엔티티를 저장해서 식별자 값을 획득한 후 영속성 컨텍스트에 저장한다.