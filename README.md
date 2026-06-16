# Everycar - 렌터카 예약 웹 서비스
<img width="1616" height="621" alt="Image" src="https://github.com/user-attachments/assets/1e761298-5a4d-42bd-aa75-4be6d6468415" />
> K-디지털트레이닝 과정 팀 프로젝트 | 11인 팀 | 팀장

개발 과정 기록 노션 : https://app.notion.com/p/IT-1598a693d9d4826b8d998130928c3937
<br>

## 프로젝트 소개

차량 검색부터 예약·결제까지 한 번에 처리하는 렌터카 예약 웹 서비스입니다.  
사용자 페이지는 **React SPA**, 관리자 페이지는 **Thymeleaf 서버 렌더링**으로 구성했으며, 11명의 팀원이 역할을 나눠 개발했습니다.  
팀장으로서 프론트엔드·백엔드 설계 및 개발과 팀 일정 조율을 담당했습니다.

| 구분 | 내용           |
|---|--------------|
| 팀 구성 | 11명 (팀장 포함)  |
| 담당 역할 | 팀장, 백엔드, 프론트 |
| 개발 기간 | 24.11 ~ 25.04 |

### 주요 기능

<img width="627" height="362" alt="Image" src="https://github.com/user-attachments/assets/329c0da5-eefa-45f1-99f5-1b89beca737c" />

렌트카 대여 가능 주차장 검색
kakao map api 와 date-fns 캘린더 라이브러리를 커스텀해 시각적인 예약 검색 환경 구성

<img width="647" height="410" alt="Image" src="https://github.com/user-attachments/assets/a6bbaa0d-193f-4615-bbd8-24046a931dd6" />

대여 가능 차량 목록 조회

<img width="300" alt="Image" src="https://github.com/user-attachments/assets/60b46fde-1a5a-42da-9e98-a3dfa8604be0" />

모바일 전용 대응 화면
<br>

## 기술 스택

**Backend**
- Java 17 · Spring Boot 3.4.8
- Spring Security 6 · JJWT (JWT 인증)
- MyBatis 3.0.4 · MySQL
- Thymeleaf (관리자 페이지)

**Frontend**
- React · SCSS

**Infra**
- AWS EC2 (백엔드 배포)
- AWS S3 (프론트엔드 정적 배포)
- MySQL

<br>

## 기술적 도전과 해결 과정

### 1. React와 연결하는 REST API — CORS와 JSON 응답 설계

처음 Spring을 배울 때는 `@Controller`에서 뷰 이름을 반환하는 방식만 알고 있었습니다.  
React는 HTML이 아닌 JSON 데이터가 필요하고, 브라우저는 출처가 다른 서버에 요청할 때 CORS 정책으로 차단한다는 것을 직접 에러를 겪으면서 이해했습니다.

**해결 방법:**
- React와 통신하는 엔드포인트는 `@RestController`로 선언해 JSON을 직접 반환
- Spring Security의 `CorsConfigurationSource`를 Bean으로 등록해 `http://localhost:3000` 허용, `credentials: true` 설정

```java
// SecurityConfig.java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    configuration.setAllowCredentials(true);
    ...
}
```

`@CrossOrigin`을 컨트롤러마다 붙이는 방법도 있지만, Security 필터 체인을 통과하기 전에 CORS 처리가 되어야 한다는 걸 알게 된 후 SecurityConfig에서 중앙 관리하는 방식으로 통일했습니다.

---

### 2. JWT 로그인 + 관리자 세션 로그인 — 왜 둘을 분리했는가

가장 많이 고민한 부분입니다.  
처음에는 "인증 방식이 두 개면 관리가 복잡해지지 않나?"라고 생각했지만, 두 페이지의 성격이 근본적으로 다르다는 걸 이해한 후에 오히려 분리하는 것이 맞다는 결론을 냈습니다.

| | 사용자 페이지 (React) | 관리자 페이지 (Thymeleaf) |
|---|---|---|
| 렌더링 방식 | 클라이언트 사이드 (SPA) | 서버 사이드 |
| 인증 방식 | **JWT (무상태)** | **세션 (서버 상태 유지)** |
| 이유 | 서버가 상태를 가지지 않아야 API 확장이 용이 | 서버가 직접 응답을 생성하므로 세션이 자연스러움 |

**JWT 구현 방식:**

Refresh Token을 단순히 클라이언트에만 두면 로그아웃 후에도 토큰이 유효한 문제가 있습니다.  
이를 해결하기 위해 Refresh Token을 DB에 저장하고, 재발급 요청 시 DB 값과 대조해 일치할 때만 새 Access Token을 발급했습니다.

```
로그인 요청
  → AuthenticationManager 인증
  → Access Token (1시간) + Refresh Token (7일) 발급
  → Refresh Token DB 저장

이후 API 요청
  → JwtAuthenticationFilter (OncePerRequestFilter)
  → Bearer 토큰 추출 → 유효성 검증 → SecurityContext에 인증 정보 등록

토큰 만료 시
  → POST /api/refresh + Refresh Token 전송
  → DB 저장값과 대조 → 일치 시 새 Access Token 발급

로그아웃
  → POST /api/logout → DB의 Refresh Token 삭제 → 재사용 불가
```

`JwtAuthenticationFilter`는 `OncePerRequestFilter`를 상속해 요청당 정확히 한 번만 실행되도록 했고, `UsernamePasswordAuthenticationFilter` 앞에 배치해 Spring Security 기본 인증 흐름보다 먼저 토큰을 검사합니다.

---

### 3. 역할 기반 접근 제어 (RBAC) — SecurityConfig 설계

처음에는 `anyRequest().authenticated()` 정도만 알았고, 역할별 접근 제한을 어디에 어떻게 선언해야 하는지 몰랐습니다.  
Spring Security의 `authorizeHttpRequests`가 선언 순서대로 평가된다는 점을 이해한 후, 아래와 같이 구성했습니다.

```java
.authorizeHttpRequests(authorize -> authorize
    // 인증 없이 허용
    .requestMatchers("/api/reservation/**", "/api/parking/**",
                     "/api/login", "/api/signup", "/api/refresh").permitAll()

    // 로그인한 사용자만
    .requestMatchers("/api/mypage/**", "/api/reservation/complete").authenticated()

    // 관리자 공통 (ADMIN, MANAGER)
    .requestMatchers("/admin/**").hasAnyRole("ADMIN", "MANAGER")

    // 특정 기능은 ADMIN만
    .requestMatchers("/admin/user/{id}").hasRole("ADMIN")

    .anyRequest().authenticated()
)
```

역할은 USER / MANAGER / ADMIN 3단계로 구분했습니다.  
관리자 페이지에 접근하려면 최소 MANAGER 역할이 필요하고, 사용자 계정 관리처럼 민감한 기능은 ADMIN만 접근 가능하도록 분리했습니다.

---

### 4. 예약 가능 차량 조회 — 날짜 충돌 탐지 쿼리

렌터카 서비스의 핵심인 "이 날짜에 빌릴 수 있는 차"를 조회하는 쿼리를 짜면서 생각보다 까다롭다는 것을 알았습니다.  
단순히 `rental_datetime > returnDatetime OR return_datetime < rentalDatetime`으로 필터링하면 일부 겹치는 케이스를 잡아내지 못합니다.

두 날짜 범위 A(요청)와 B(기존 예약)가 겹치는 경우는 총 4가지입니다:

```
A:  |-------|
B:    |------|    ① A 시작이 B 범위 안
B:  |------|      ② A 종료가 B 범위 안
B:  |---------|   ③ B 시작이 A 범위 안
B:    |---|        ④ B 종료가 A 범위 안
```

이 4가지를 모두 OR로 연결해 충돌하는 car_id를 NOT IN으로 제외했습니다:

```sql
AND c.car_id NOT IN (
    SELECT fr.car_id FROM fast_reservation fr
    WHERE fr.rental_state IN (0, 1)  -- 예약 중, 대여 중만
    AND (
        (fr.rental_datetime  BETWEEN #{rentalDatetime} AND #{returnDatetime})
        OR (fr.return_datetime BETWEEN #{rentalDatetime} AND #{returnDatetime})
        OR (#{rentalDatetime}  BETWEEN fr.rental_datetime AND fr.return_datetime)
        OR (#{returnDatetime}  BETWEEN fr.rental_datetime AND fr.return_datetime)
    )
)
```

조회와 동시에 car·model·parking 테이블을 한 번의 JOIN으로 가져와 N+1 문제를 방지했고, MyBatis `resultMap`의 `association`으로 중첩 DTO(CarDTO → ModelDTO, ParkingDTO)를 한 번에 매핑했습니다.

---

### 5. 레이어 분리와 트랜잭션 관리

처음에는 컨트롤러 안에 DB 조회 코드를 쓰거나, 서비스 안에서 HTTP 응답 코드를 직접 다루는 코드를 작성했습니다.  
팀 작업을 하면서 "어디를 고치면 어디까지 영향이 가는지" 예측이 안 되는 문제를 겪었고, 이후 각 레이어의 책임을 명확하게 분리했습니다.

| 레이어 | 책임 |
|---|---|
| Controller | HTTP 요청/응답, 상태 코드, 파라미터 수신 |
| Service | 비즈니스 로직, 유효성 검사, 트랜잭션 관리 |
| Mapper | SQL 실행, DB I/O |

**트랜잭션 적용 기준:**
- 조회 전용 메서드: `@Transactional(readOnly = true)` — 불필요한 락 방지 및 성능 최적화
- 데이터 변경: `@Transactional` — 실패 시 롤백 보장
- 결제 검증 + 예약 저장: 두 작업을 하나의 트랜잭션으로 묶어 결제만 완료되고 예약이 저장 안 되는 상황을 방지

```java
// PaymentService.java
@Transactional
public boolean verifyAndSaveReservation(PaymentCheckRequest request) {
    // 1. PortOne API로 실제 결제 금액 조회
    // 2. 서버에서 계산한 금액과 비교
    // 3. 일치할 때만 예약 저장 — 하나라도 실패하면 전체 롤백
}
```

**현재 개선 중인 부분:**  
예외 처리가 컨트롤러마다 흩어져 있어 `@ControllerAdvice`로 전역 예외 핸들러를 분리하는 리팩토링을 진행 중입니다. 또한 일부 남아 있는 `System.out.println()`을 `@Slf4j` 로거로 교체하고 있습니다.

<br>

## API 목록

### 인증 (Auth)

| Method | URI | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/signup` | 회원가입 | - |
| POST | `/api/login` | 로그인 (Access + Refresh Token 발급) | - |
| POST | `/api/refresh` | Access Token 재발급 | Refresh Token |
| POST | `/api/logout` | 로그아웃 (DB Refresh Token 삭제) | - |

### 예약 (Reservation)

| Method | URI | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/parking?region=` | 지역으로 대여소 검색 | - |
| GET | `/api/reservation/cars` | 예약 가능 차량 목록 조회 | - |
| GET | `/api/reservation/cars/{carId}` | 차량 상세 조회 + 요금 계산 | - |
| GET | `/api/reservation/reservationCar/{carId}` | 반납 가능 대여소 목록 | - |
| GET | `/api/reservation/contract-details` | 결제 전 계약 정보 조회 | - |
| POST | `/api/reservation/complete` | 결제 검증 및 예약 확정 | JWT |

### 마이페이지 (Mypage)

| Method | URI | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/mypage/{userNum}` | 회원 정보 및 면허 정보 조회 | JWT |
| PUT | `/api/mypage/update` | 회원 정보 수정 | JWT |
| POST | `/api/mypage/save` | 면허 정보 등록/수정 | JWT |
| DELETE | `/api/mypage/{userNum}` | 면허 정보 삭제 | JWT |
| GET | `/api/mypage/reservation/{userNum}` | 내 예약 목록 조회 | JWT |
| GET | `/api/mypage/reservation/detail/{reservationId}` | 예약 상세 조회 | JWT |

### 관리자 (Admin) — 세션 인증 · Thymeleaf

| Method | URI | 설명 | 권한 |
|---|---|---|---|
| GET/POST | `/admin/login` | 관리자 로그인 | - |
| GET | `/admin/cars` | 차량 목록 (검색·필터·페이징) | MANAGER, ADMIN |
| GET/POST | `/admin/cars/register` | 차량 등록 | MANAGER, ADMIN |
| GET/POST | `/admin/cars/edit/{id}` | 차량 수정 | MANAGER, ADMIN |
| POST | `/admin/cars/delete/{id}` | 차량 삭제 | MANAGER, ADMIN |
| GET | `/admin/reservations` | 예약 목록 관리 | MANAGER, ADMIN |
| GET | `/admin/users` | 회원 목록 관리 | MANAGER, ADMIN |
| GET | `/admin/user/{id}` | 특정 회원 상세/관리 | **ADMIN 전용** |

<br>

## 프로젝트 구조

```
src/main/java/com/road_friends/everycar/
├── user/               # 인증·보안 (JWT, SecurityConfig, Filter)
│   ├── component/      # JwtUtil, CustomUserDetails
│   ├── config/         # SecurityConfig, JwtProperties
│   ├── filter/         # JwtAuthenticationFilter
│   ├── service/        # APIUserService, CustomUserDetailsService
│   └── mapper/
├── reservation/        # 예약 조회·요금 계산 API
├── payment/            # PortOne 결제 검증 및 예약 확정
├── mypage/             # 사용자 정보·예약 내역 API
└── admin/              # 관리자 페이지 (car, reservation, user)

src/main/resources/
├── mapper/             # MyBatis XML (SQL)
└── templates/admin/    # Thymeleaf 관리자 UI
```
