# archivist-backend
archivist-backend repo


* * *
### 개발 환경
- Build Tool: Gradle
- Framework: Spring Boot 3.0.12
- Programming Language: Java, JDK 17

* * *
### 브랜치 전략
이 프로젝트는 [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/) 전략을 사용합니다.
- `main` : 프로덕션 준비 상태의 코드를 유지.
- `develop` : 다음 출시에 대해 추가된 모든 개발 변경 사항을 반영.
- `feature-*` : 새로운 기능 개발. ( 각 기능은 `feature-기능명` 형식으로 명명 )
- `release-*` : 릴리스(새로 출시하는 버전) 준비. 
- `hotfix-*` : 운영에서 발생한 긴급한 버그를 수정. 

* * *
### 커밋 컨벤션
이 프로젝트는 아래의 커밋 컨벤션을 따릅니다.
- `feat`: 새로운 기능을 추가할 때 사용.
- `fix`: 버그를 수정할 때 사용.
- `test`: 테스트 코드, 리팩토링 없는 테스트 추가 시 사용.
- `chore`: 빌드 과정이나 보조 도구, 라이브러리 변경 등의 간단한 수정에 사용.
- `refactor`: 코드 리팩토링 시 사용. (기능 변경이나 버그 수정을 포함하지 않음)

* * *
### 패키지 구조
이 프로젝트는 계층형 구조를 따릅니다.

    controller
    └─ *Controller

    service
    ├─ *Service
    └─ *ServiceImpl

    repository
     └─ *Repository

    dto
     └─ *Dto

    entity
     └─ *

