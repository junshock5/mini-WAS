# mini-WAS
목적
---
### Java 로 간단한 WAS(Web Application Server)를 만드는 것
### **java-network-programming 주소** <br/> https://in.finance.yahoo.com/quote/GOOG/history?p=GOOG&.tsrc=fin-srch

유의 사항
---
- 제출한 소스는 압축을 푼 다음에 mvn clean package 했을 때, JUnit 을 수행하고, JAR 파일을 생성해야 합니다. 
- 그리고 java –jar was.jar 해서 실행할 수 있어야 합니다.
- Java 표준 라이브러리 외 다른 네트워크 프레임워크(예, Netty)를 사용하지 말아주세요. 
### **Java8 표준 라이브러리 주소** <br/> https://docs.oracle.com/javase/8/docs/api/

사용 기술
---
- Maven Project
- Java8
- JUnit4
- Logback
- HTTP/1.1

프로젝트 주요 기능
---
- HTTP/1.1 Host 헤더를 해석합니다.
  - a.com, b.com의 ip가 같을지라도 설정에 따라 서버에서 다른 데이터를 제공할수 있습니다.
  - 아파치 웹 서버의 VirtualHost 기능 참고
- 설정 파일 관리 (json)
  - 포트 번호
  - HTTP/1.1 Host별 ROOT 디렉터리 다르게
  - 403, 404, 500 오류시 HTML 파일 이름
- 403, 404, 500 오류를 처리합니다. (설정파일, HTML 반환)
- 보안 규칙을 둡니다.
  - HTTP_ROOT 디렉터리 상위 디렉터리 접근시 403 에러를 보냅니다. ex) http://localhost:8000/../../../ect/passwd 
  - 확장자가 .exe 인 파일 요청시
  - 추후 규칙 추가 확장성 고려
- Loggback 프레임워크 http://logback.qos.ch/를 이용하여 다음의 로깅 작업을 합니다.
  - 로그 파일을 하루 단위로 분리합니다.
  - 로그 내용에 따라 적절한 로그 레벨을 적용합니다.
  - 오류 발생 시, StackTrace 전체를 로그 파일에 남깁니다.
- 간단한 WAS 를 구현합니다.
  - SimpleServlet, HttpRequet, HttpResponse 인터페이스나 객체는 여러분이 보다 구체적인 인터페이스나 구현체를 제공,동작 해야합니다.
  - url을 SimpleServlet 구현체로 맵핑합니다.
  - http://localhost:8000/Hello --> Hello.java 로 매핑
  - http://localhost:8000/service.Hello --> service 패키지의 Hello.java 로 매핑
  - 과제는 URL 을 바로 클래스 파일로 매핑하지만, 추후 설정 파일을 이용해서 매핑하는 것도 고려해서 개발하십시오.
  ▪ 추후 확장을 고려하면 됩니다. 설정 파일을 이용한 매핑을 구현할 필요는 없습니다.
  ▪ 설정 파일을 이용한 매핑에서 사용할 수 있는 설정의 예, {“/Greeting”: “Hello”, “/super.Greeting”: “service.Hello”}
- 현재 시각을 출력하는 SimpleServlet 구현체를 작성하세요.
  - 앞서 구현한 WAS 를 이용합니다.
  - WAS 와 SimpleServlet 인터페이스를 포함한 SimpleServlet 구현 객체가 하나의 JAR 에 있어도 괜찮습니다. 분리하면 더 좋습니다.
- 앞에서 구현한 여러 스펙을 검증하는 테스트 케이스를 JUnit4 를 이용해서 작성하세요.

기술적인 집중 요소
---
- 객체지향의 기본 원리와 의미 있는 코드 작성
- 라이브러리 및 기능 추가 시 이유있는 선택과 사용 목적 고려
- 테스트 코드 작성
