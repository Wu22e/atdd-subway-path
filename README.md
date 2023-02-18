# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 실습 - 단위 테스트 작성
## 요구사항
### 기능 요구사항
- 지하철 구간 관련 단위 테스트를 완성하세요.
  - 구간 단위 테스트 (LineTest)
  - 구간 서비스 단위 테스트 with Mock (LineServiceMockTest)
  - 구간 서비스 단위 테스트 without Mock (LineServiceTest)
- 단위 테스트를 기반으로 비즈니스 로직을 리팩터링 하세요.

## 요구사항 설명
### 단위 테스트 코드 작성하기
- 기존 기능에 대한 테스트 작성이기 때문에 테스트 작성 시 바로 테스트가 성공해야 함.

### 비즈니스 로직 리팩터링
- 구간 추가/삭제 기능에 대한 비즈니스 로직은 현재 LineService 에 대부분 위치하고 있음.
- 비즈니스 로직을 도메인 클래스(Line)으로 옮기기
- 리팩터링 시 LineTest 의 테스트 메서드를 활용하여 TDD 사이클로 리팩터링을 진행
- 리팩터링 과정에서 Line 이외 추가적인 클래스가 생겨도 좋음
  - 구간 관리에 대한 책임을 Line 외 별도의 도메인 객체가 가지게 할 수 있음

# 1단계 - 지하철 구간 추가 기능 개선
## 요구사항
### 기능 요구사항
- `요구사항 설명`에서 제공되는 추가된 요구사항을 기반으로 **지하철 구간 관리 기능**을 리팩터링하세요.
- 추가된 요구사항을 정의한 **인수 조건**을 도출하세요.
- 인수 조건을 검증하는 **인수 테스트**를 작성하세요.
- 예외 케이스에 대한 검증도 포함하세요.

> 모든 케이스에 대해 인수 테스트를 만들 필요는 없습니다. 세부적인 요구사항은 단위 테스트로 검증해도 좋습니다.

### 프로그래밍 요구사항
- **인수 테스트 주도 개발 프로세스**에 맞춰서 기능을 구현하세요.
  - `요구사항 설명`을 참고하여 인수 조건을 정의
  - 인수 조건을 검증하는 인수 테스트 작성
  - 인수 테스트를 충족하는 기능 구현
- 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
  - 뼈대 코드의 인수 테스트를 참고
- **인수 테스트 이후 기능 구현은 TDD로 진행하세요.**
  - 도메인 레이어 테스트는 필수
  - 서비스 레이어 테스트는 선택

## 요구사항 설명
### 변경된 스펙 - 구간 추가 제약사항 변경
#### 역 사이에 새로운 역을 등록할 경우
- 기존 구간의 역을 기준으로 새로운 구간을 추가
  - 기존 구간 A-C에 신규 구간 A-B를 추가하는 경우 A역을 기준으로 추가
  - 결과로, A-B, B-C 구간이 생김
  - 기존 구간과 신규 구간이 모두 같을 순 없음
- 새로운 길이를 뺀 나머지를 새롭게 추가된 역과의 길이로 설정

#### 새로운 역을 상행 종점으로 등록할 경우
- 기존 구간의 상행 종점역이 하행역이되고, 새로운 역이 상행 종점으로 등록됨.

#### 새로운 역을 하행 종점으로 등록할 경우
- 기존 구간의 하행 종점역이 상행역이 되고, 새로운 역이 하행 종점으로 등록됨.

### 변경된 스펙 - 예외 케이스
- 역 사이에 새로운 역을 등록할 경우, 기존 역 사이 길이보다 크거나 같으면 등록할 수 없음.
- 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음.
  - A-B, B-C 구간이 등록된 상황에서 B-C 구간을 등록할 수 없음. (A-C 구간도 등록할 수 없음.)
- 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음.


## 요구사항 기반 시나리오
- 구간 등록 기능 인수 테스트 (역 사이에 새로운 역을 등록할 경우)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 양재역을 생성한다.
  - 강남역-양재역 구간(4km)을 생성한다. (양재역이 등록하고자 하는 구간의 하행역인 경우)
  - 강남역-양재역 구간을 신분당선에 등록한다.
  - 청계산입구역을 생성한다.
  - 청계산입구역-판교역 구간(3km)를 생성한다. (청계산입구역이 등록하고자 하는 구간의 상행역인 경우)
  - 청계산입구역-판교역 구간을 신분당선에 등록한다. 
  - 신분당선을 조회한다.
  - 신분당선에는 강남역-(4km)-양재역-(3km)-청계산입구역-(3km)-판교역이 조회된다.

- 구간 등록 기능 인수 테스트 (새로운 역을 상행 종점으로 등록할 경우)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 신논현역을 생성한다.
  - 신논현역-강남역 구간(2km)을 생성한다.
  - 신논현역-강남역 구간을 신분당선에 등록한다.
  - 신분당선을 조회한다.
  - 신분당선에는 신논현역-(2km)-강남역-(10km)-판교역이 조회된다.

- 구간 등록 기능 인수 테스트 (새로운 역을 하행 종점으로 등록할 경우)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 정자역을 생성한다.
  - 판교역-정자역 구간(3km)을 생성한다.
  - 판교역-정자역 구간을 신분당선에 등록한다.
  - 신분당선을 조회한다.
  - 신분당선에는 강남역-(10km)-판교역-(3km)-정자역이 조회된다.

### 예외 테스트
- 구간 등록 기능 예외 테스트 1 (역사이 새로운 역 등록 시, 기존 역 사이 길이 보다 큰 경우)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 정자역을 생성한다.
  - 강남역-정자역 구간(구간 길이 >= 10km)을 생성한다.
  - 강남역-정자역 구간을 신분당선에 등록한다.
  - 예외가 발생한다.

- 구간 등록 기능 예외 테스트 2 (등록할 구간의 역이 모두 등록된 경우)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 강남역-판교역 구간을 또 신분당선에 등록한다.
  - 예외가 발생한다.

- 구간 등록 기능 예외 테스트 3 (등록할 구간의 역이 모두 등록되지 않은 경우)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 신사역, 신논현역을 생성한다.
  - 신사역-신논현역 구간(3km)을 생성한다.
  - 신사역-신논현역 구간을 신분당선에 등록한다.
  - 예외가 발생한다.

# 2단계 - 지하철 구간 제거 기능 개선
## 요구사항
### 기능 요구사항
- `요구사항 설명`에서 제공되는 추가된 요구사항을 기반으로 **지하철 구간 관리 기능**을 리팩터링하세요.
- 추가된 요구사항을 정의한 **인수 조건**을 도출하세요.
- 인수 조건을 검증하는 **인수 테스트**를 작성하세요.
- 예외 케이스에 대한 검증도 포함하세요.

> 모든 케이스에 대해 인수 테스트를 만들 필요는 없습니다. 세부적인 요구사항은 단위 테스트로 검증해도 좋습니다.

### 프로그래밍 요구사항
- **인수 테스트 주도 개발 프로세스**에 맞춰서 기능을 구현하세요.
  - `요구사항 설명`을 참고하여 인수 조건을 정의
  - 인수 조건을 검증하는 인수 테스트 작성
  - 인수 테스트를 충족하는 기능 구현
- 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
  - 뼈대 코드의 인수 테스트를 참고
- **인수 테스트 이후 기능 구현은 TDD로 진행하세요.**
  - 도메인 레이어 테스트는 필수
  - 서비스 레이어 테스트는 선택

## 요구사항 설명
### 변경된 스펙
#### 구간 삭제에 대한 제약 사항 변경 구현
- 기존에는 마지막 역 삭제만 가능헀는데 위치에 상관없이 삭제가 가능하도록 수정
- 종점이 제거될 경우 다음으로 오던 역이 종점이 됨
- 중간역이 제거될 경우 재배치를 함
  - 노선에 A-B-C 역이 연결되어 있을 때 B 역을 제거할 경우 A-C 로 재배치 됨
  - 거리는 A-B, B-C 두 구간의 합으로 정함.

#### 구간이 하나인 노선에서 마지막 구간을 제거할 때
- 제거할 수 없음

#### 이 외 예외 케이스를 고려하기
- 기능 설명을 참고하여 예외가 발생할 수 있는 경우를 검증할 수 이쓴ㄴ 인수 테스트를 만들고 이를 성공 시키세요.
> 예시) 노선에 등록되어있지 않은 역을 제거하려 한다.

## 요구사항 기반 시나리오
- 구간 제거 기능 인수 테스트 (중간역을 제거할 경우)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 정자역을 생성한다.
  - 판교역-정자역 구간(3km)을 생성한다.
  - 판교역-정자역 구간을 신분당선에 등록한다.
  - 신분당선에서 판교역을 제거한다.
  - 신분당선을 조회한다.
  - 신분당선에는 강남역-(13km)-정자역이 조회된다.

- 구간 제거 기능 인수 테스트 (종점을 제거할 경우)
  - 신사역, 강남역, 판교역, 정자역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 신사역-강남역 구간(5km)을 생성한다.
  - 신사역-정자역 구간을 신분당선에 등록한다.
  - 판교역-정자역 구간(3km)을 생성한다.
  - 판교역-정자역 구간을 신분당선에 등록한다.
  - 신분당선에서 신사역과 정자역을 제거한다.
  - 신분당선을 조회한다.
  - 신분당선에는 강남역-(10km)-판교역이 조회된다.

### 예외 테스트
- 구간 제거 기능 예외 테스트 (구간이 하나인 노선 제거시)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
    - 신분당선에서 강남역을 삭제한다.
      - 예외가 발생한다.
    - 신분당선에서 판교역을 삭제한다.
      - 예외가 발생한다.

- 구간 제거 기능 예외 인수 테스트 (노선에 등록되지 않은 역을 제거할 경우)
  - 강남역, 판교역을 생성한다.
  - 강남역-판교역 구간(10km)이 포함된 신분당선을 생성한다.
  - 정자역을 생성한다.
  - 신분당선에서 정자역을 제거한다.
    - 예외가 발생한다.