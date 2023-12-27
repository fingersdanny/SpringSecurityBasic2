### AuthenticationProvider 
* 여러 개의 Authentication Provider가 존재할 수 있음 예를 들면
  * userName, password Authentication
  * OAuth 2.0 Authentication
  * OTP Authentication
* 각각의 Authentication Provider가 필요하다.

### CORS (CROSS-ORIGIN RESOURCE SHARING)
* 스프링 서버에서의 해결 방법
1. ``@CrossOrigin(origins = "http://localhost:4200")`` ``@CrossOrigin(origins = "*")``
* 이 어노테이션을 사용하면 각각의 컨트롤러에 달아줘야 하므로 귀찮고 무의미한 반복 작업을 계속 해야함 
* 실제 서비스에 얼마나 많은 컨트롤러가 있을지 모름 -> 비추천
2. ``CorsConfiguration``을 bean으로 새로 설정해서 전역적으로 CORS 설정을 적용할 수 있음.

### CSRF (CROSS-SITE REQUEST FORGERY)
* 유저의 동의없이 해당 유저의 신분을 사용해서 해당 유저의 권한을 남용하는 것...
* 백엔드에서는 해당 유저가 적법한 경로를 통해서 Http Request를 보내고 있는지 확인해야함.
* GET과 같은 경우는 상관 없는데 데이터를 수정해야하는 POST, PUT과 같은 경우 
* Spring Security를 따로 설정하지 않는다면 기본적으로 막혀있다.
* 이는 개발자의 편의를 위해 일단 기본적으로 막아놓은 상태에서 시작하여 웹 어플리케이션을 보호하기 위함

### Filter chain
* Spring Security에는 기본적으로 여러가지 체인이 있고 하나의 체인이 다른 체인을 invoke하는 형식으로 되어 있음
* response나 request를 가로채서 처리하고 없애는 데 있음

### 메서드 레벨 보안
* ``@PreAuthorize`` 와 ``@PostAuthorize`` SpeL (Spring Expression Language)를 통해서 메서드 별로 지정해줄 수 있음
* ``@PreAuthorize``는 메서드 실행 전에 권한 확인
* ``@PostAuthorize``는 메서드 실행 후에 권한 확인
* 내부적으로 AOP를 통해서 메서드 레벨 보안을 구현한다.
* ``@PreFilter`` 와 ``@PostFilter``를 통해서 필터를 메서드 레벨에 적용할 수 있다.

### OAuth2