# Spring-Cloud-Gateway-With-OAuth2

Spring Cloud Gateway 와 OAuth2 를 연동하여 하위 MSA 서비스들에게 인증 된 사용자 값을 전달하고, 각 MSA 서비스들이 전달 된 사용자 값을 어떻게 처리할 수 있는지에 대한 예제

## 목적

사용자 인증은 Gateway 에서 수행하고, 각 MSA 로 인증값이 전달 될 때는 Custom Header 로 변경하여 전달하는 서비스를 구현하고자 함

## 요구사항

- [Spring Authorization Server](https://github.com/lutics/Spring-Authorization-Server-On-GCP-AppEngine)

에 대한 사전 지식이 있거나, OAuth2 Authorization Server 가 이미 구축되어 있으면 좋다

## 구성

- Spring Cloud Gateway
- 예제용 MSA Service 1 (WebFlux)

## 설정

- Spring Authorization Server 가 없다면, 별도로 준비해야 한다
- Spring Cloud Gateway 프로젝트 내부에 있는 `application.yaml` 에서 `spring.security.oauth2.resourceserver.jwt.jwk-set-uri` 주소를 연동하고자 하는 OAuth2 서버의 경로로 수정한다

## 실행

- Gateway, Service 폴더를 각각 IDE로 열어 실행시킨다 (8080, 8081)
- [설정](#설정)에 세팅 된 OAuth2 서버에서 발급 한 임의의 Access Token 을 가지고 아래의 경로를 호출한다

#### 예제 1. @PreAuthorized 적용되지 않은 예제

```curl
curl "http://localhost:8080/service1/test" \
     -H 'Authorization: Bearer [Access Token]'
```

#### 예제 2. @PreAuthorized 가 적용 된 예제

```curl
curl "http://localhost:8080/service1/testSecured" \
     -H 'Authorization: Bearer [Access Token]'
```