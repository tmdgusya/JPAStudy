# 추가 요구 사항 추가

## Member.class

1. 회원은 일반회원과 관리자로 구분해야 한다.
2. 회원 가입일과 수정일이 있어야 한다.
3. 회원을 설명할 수 있는 필드가 있어야 한다. 이 필드는 길이 제한이 없다.

## 구현 설계

* 회원이 가질수 있는 역할은 RoleType 의 ENUM class 로 설계한다.

* 회원가입일은 생성할 당시의 Date 값을 입력하도록 하고, 수정일은 upate 칠때 Date 값을 기록해준다.
=> 수정일 초기값은 생성할 당시 Date 와 같은 값 기록.

* 회원을 설명할 수 있는 필드가 있어야 한다. 이 필드는 길이 제한이 없다.
=> clob Type 을 사용하여, Data 의 제한이 없도록 한다.