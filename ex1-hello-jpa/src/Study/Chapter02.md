# 연관관계 Mapping

* 객체 그리고 DB 는 각 다른 Entity 또는 Table 에 연관관계를 가진다.
* 우리는 이 연관관계를 표현해야 한다. DB 에서는 보통 외래키로 표현하고, 객체 지향적인 관점에서는 양방향이 아닌 단방향으로
서로의 참조값을 가져오는 것(getInstance()) 등등 으로 참조할 수 있다.
* 이렇듯 DB 는 외래키를 통해 참조할 경우 서로 Join 이 가능하다.
```roomsql

SELECT * FROM MEMBER M JOIN TEAM T ON M.MEBER_ID = T.TEAM_ID;

SELECT * FROM TEAM T JOIN MEMBER M ON T.TEAM_ID = M.MEMBER_ID;

```

이렇듯 양방향 Join 이 가능하다. 근데 객체의 경우 양방향이 없다. Method 를 통한 호출에 의한 값 참조이므로 단방향이 두개 있는 것이다.

** 혹시라도 헷갈릴까봐 객체 참조 테스트!!**

* 근데 아래 테스트는 주소값을 참조하므로 양방향적임, 근데 위에서 설명한 값 참조는 그니까 우리는 TeamId 를 Foreign Key 로 가져야 하므로
Id 값을 참조해 와야 하는데, 그때마다 값을 참조해 오는 방식으로 단뱡향 적임.

```
[객체 A | Address : #11727]

[객체 B | Address : #17827]

class B
    A info_a = A.getInstance();

showMemoryAdress(B.info_a) => #11727 을 가진다. 

```

이렇듯 위의 테스트와 같이 서로 주소값을 참조하게 된다.

위의 테스트가 궁금하다면 아래 코드를 참조하자!
![HASH_구현코드](../main/java/Chapter02/HashTest.java)

### DB 설계도 구현

* 이건 관계 Mapping Test 로 DB 자체 구현이 어렵지 않으므로 간단히 그리겠음

```
[Person]                        [Team]
------------ N                1 --------------
[person_id]  ------------------ [team_id]
[name]                          [name]
[team]

```

### ManyToOne OneToMany etc..

* 이건 어떤 방향으로 적어야 할지 고민하지 말자!
* 그냥 DB Architecture 그대로 그 방향대로 적으면 된다. => 어려운게 없음 그냥 DB 설계도 적는 연습 많이하면 쉬움!

### 연관관계가 있는 엔티티를 조회하는 방법!

* 객체 그래프 탐색
* 객체지향 쿼리 사용

* 객체 그래프 탐색 ?
    * member 가 포함된 팀을 찾으려면 member.getTeam() 으로 하면 된다.

* 객체 지향 쿼리 사용
```java

String jpql = "select m from Member m join m.team t where "+ "t.name =:teamName"

List<Member> resultList = em.createQuery(jpql, Member.class )
                          .setParameter("teamName", "팀1")
                          .getResultList();
```

* **':'** 와 같은 문법은 Parameter 를 바인딩 받는 문법이다.

### mappedBy

* mappedBy 는 양방향 매핑일 때 사용하는데 반대쪽 매핑의 필드 이름을 값으로 주면 된다.

* 객체에서는 단방향이 두방향으로 나뉘어져 있을뿐, 애플리케이션 로직으로 잘 묶어서 양방향 처럼 보이는 것이다.

* 데이터 베이스 테이블은 앞서 설명했듯이 외래키 하나로 양쪽이 서로 조인할 수 있다.

* 데이터 베이스 에서는 외래키를 이용해 서로 양방향 적인 관계인데, 객체에서는 단방향이 두갈래로 있는 것으로
서로 차이가 존재할 수 밖에 없다. 그니까 member 에 team 을 세팅해도 getMember() 를 하기전까지 team 은
member 가 추가된지 모르는 것이다. 따라서 한쪽이 업데이트가 되면 다른쪽도 자동으로 업데이트가 되야 한다.

즉 **주인관계** 를 설정해야 하는 이유다.

* 주인은 mappedBy를 설정하지 않는다.

* 주인이 아니면 mappedBy 속성을 사용해서 속성의 값으로 연관관계의 주인을 지정해야 한다.

* 연관관계의 주인은 외래키가 있는 고으로 해야 한다. 음 이건 이렇게 해야 성능 이슈랑 Entity 랑 Table 이 Mapping 되어 있는 테이블에서 외래키가 관리된다.
라고 하는데, 아직 까지는 와닿을 정도로 이해가 되지 않는다. 책을 몇회독 할때쯤에는 이해가려나..?

* ㅇㅏ 이제 알았다! **연관관계의 주인을 정하는건 사실 외래키 관리자를 선택하는 것이다.**
=> 왜냐 연관관계의 주인이 아닌 대상은 읽기 밖에 못하고, 외래키를 변경할 수 없는데, 외래키에 반대편을 주인으로 정할 수 없기 때문이다.
