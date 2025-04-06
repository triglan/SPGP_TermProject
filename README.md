# 프로젝트 제목 : Deadlock Duel

## 게임컨셉 : 턴제전투와 덱빌딩이 합쳐진 타일기반 전략RPG게임.
### 핵심 매카닉
********
- 턴제 전투 시스템
- 타일 기반 이동 및 전투
- 무기 대기열 시스템
- 덱 빌딩 요소


## 개발 범위
- 맵
- 주인공 캐릭터
- 적 캐릭터 3종(검사, 중갑병, 궁수) 및 AI
- 무기 대기열 시스템(칼, 도끼, 활)
     - 플레이어, 적 모두 공격 대기열에 무기를 넣어야 공격이 가능하다.
- 게임 모드 : 스테이지 형식, 3라운드까지 진행
- UI : 메인화면(시작, 조작법, 종료), 게임화면(이동 버튼, 무기 버튼)
- 오디오 : 배경음, 타격음

## 게임흐름
1. 메인화면 -> 시작버튼 클릭
2. 전투 화면 진입 -> 플레이어 & 적 배치
3. 플레이어는 턴마다 공격/이동/대기/회전을 진행
4. 플레이어의 행동이 끝나면 적도 AI에 따라서 행동
   ㄴ 검사, 중갑병은 플레이어와 1타일까지 접근 후 공격 시도
   ㄴ 궁수는 1턴 딜레이 이후 플레이어에게 사격 시도
5. 스테이지 클리어 시 무기강화 or 무기 추가를 통한 덱빌딩 가능
6. 클리어 시 엔딩
6.1. 사망시 다시 시작 버튼 활성화 후 처음 스테이지로 시작

   ![메인화면](https://github.com/user-attachments/assets/157fe56e-e4d2-45f0-991f-ce9381be49dc)



## 개발 일정
   - 1주차   4/8 ~ 4/14
        - 캐릭터 및 맵 리소스 찾기
        - 메인화면 버튼 클릭 이벤트 구현
   - 2주차	4/15 ~ 4/21
        - 맵 타일 그리기
        - 캐릭터 배치 및 이동 버튼 구현
   - 3주차	4/22 ~ 4/28
        - 무기 시스템 구현
        - 플레이어 공격 구현
   - 4주차	4/29 ~ 5/5 
        - 적 캐릭터 3종 구현
        - 턴 기반 전투 시스템 구현
   - 5주차	5/6 ~ 5/12 
        - 3라운드 로직 완성
        - 클리어 / 사망 처리 화면
   - 6주차	5/13 ~ 5/19 
        - 적 AI 구현
        - 타격 / 배경 음악 연결
   - 7주차	5/20 ~ 5/26 
        - 플레이어 및 적 애니메이션 구현
        - 무기강화 / 추가 UI 구현
   - 8주차	5/27 ~ 6/2  
        - 애니메이션 마무리
        - 디버깅 및 버그 수정
        - 최종 발표 준비
