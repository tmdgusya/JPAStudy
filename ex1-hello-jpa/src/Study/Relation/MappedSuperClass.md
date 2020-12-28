# MappedSuperClass

- Base Entity 를 이용하여 객체들이 주로 사용하는 공통 Mapping 정보를 정의했다.
- 자식 Entity 들은 상속을 통해 BaseEntity 의 정보를 상속 받을 수 있다.

- @MappedSuperClass 는 Table 로 정의 되지 않는다. 단순히 해당 BaseEntity 는 공통 속성을 모아둔 추상 클래스이다.

- BaseEntity 는 Entity 가 아니므로, find 와 같은 쿼리의 조회가 불가능하다.

- 음 이건 등록일자나, 가입일자, 수정일자 등등의 공통적인 속성을 뽑아만들면 되게좋을것 같다.