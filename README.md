# mini-WAS
목적
---
- ### Java로 간단한 WAS(Web Application Server)를 만드는 것

사용 기술
---
- Maven Project
- Java 8
- Junit 4
- assertj
- Logback
- HTTP/1.1(모작)
- JSON
- guava

기술적인 집중 요소
---
- 객체지향의 기본 원리와 의미 있는 코드 작성
- 라이브러리 및 기능 추가 시 이유 있는 선택과 사용 목적 고려
- 테스트 코드 작성

프로젝트 주요 기능
---
- HTTP/1.1 Host 헤더를 해석
  - RequestProcessor 클래스에서 개별 인스턴스를 풀에 등록 후 처리
  - 쿼리 스트링, 쿠키값 파싱 유틸 생성
  - HTTP Request (Header, Body, Line) 클래스별 분리
  - Request 데이터 읽는 유틸 생성 (Strings, maps 컬렉션 Request 파싱 시 사용, key value 형식)
  - HTTP Response (Header, Body, StatusLine, statusCode) 클래스별 분리, 버전 명시, get, write, new 메서드 구현
  - Response notFound, error, processFile, addHeader 메서드 구현

- 설정 파일 관리 (JSON)
  - port, root, HtmlStatusName JSON 파일에서 읽어서 처리
  - 403, 404, 500 오류 시 HTML 파일 이름 JSON 파일에서 읽어서 처리
  - HttpResponse 에 HttpStatusCode 3글자로 JSON에 저장되어있는 value 값을 찾아 makeHtml 메서드에서 작성

- 보안 규칙
  - 상위 디렉터리 접근 시 403 에러 처리
  - 확장자가 .exe인 파일 요청 시 403 에러 처리
  - 추후 규칙 추가 확장성 고려
  - HttpStatusCode enum 클래스로 관리
  - JSON 파일의 Html_Code_State_List 값을 통해 HTML 타이틀 및 바디 생성

- Logback 로깅 작업 추가
  - 로그 파일을 하루 단위로 분리
  - 로그 내용에 따라 적절한 로그 레벨 적용 (error, info 분리)
  - 오류 발생 시, StackTrace 전체를 로그 파일 쓰기

- 간단한 WAS 구현
  - RequestHandler 에서 checkUpperDirectoryFileExte 함수 분리
  - HandlerMapping 에서 URL 에 따른 컨트롤러를 Dispatcher에서 생성하게끔 작성
  - Dispatcher에서 Hander에서 작성한 URL을 찾아서 Controller의 method에 따라 실행
  - Controller 인터페이스 작성 및 추상 구현체에 Content-Type 작성
  - Default, Home, Hello, Time Controller 작성

- JUnit4 테스트 코드 작성
  - makeHtmlByCode null 조건 추가
  - HttpRequestTest Get header, method, path, version 테스트 작성
  - HttpResponseTest Redirect, forbidden, notfound, error, process, body 메서드 테스트 작성
  - HttpRequestUtilsTest 쿼리스트링, 경로 구분자 개수 반환 메서드, 키벨류 가져오는 값, header 파싱 테스트 작성
  - IOUtilsTest 파일에 쓴 데이터 확인하는 테스트 작성
  - HttpServerTestsettingJson 시 포트, 루트 정보 테스트 작성
  - RequestHandlerTest 상위경로 요청인지, 파일 확장자가 exe인지 테스트 작성

참고
---
### **java-network-programming 주소** <br/> https://www.oreilly.com/library/view/java-network-programming/9781449365936/
### **Java8 표준 라이브러리 주소** <br/> https://docs.oracle.com/javase/8/docs/api/
