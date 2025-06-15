# 프로젝트 제목 : Deadlock Duel

# 발표 영상
- 1차 https://www.youtube.com/watch?v=MnolG0xyAEE
- 2차 https://youtu.be/smRpqMz60HA
- 3차 
# ReadMe
- 1차 https://github.com/triglan/SPGP_TermProject/blob/main/README.md](https://github.com/triglan/SPGP_TermProject/blob/8e31e3ab9821c4b35cc95ff7e5cd36a1dc7c1851/README.md)
- 2차https://github.com/triglan/SPGP_TermProject/blob/03928d194615b206ab3f7e7e1fc8760ea755870c/README.md
- 3차 https://github.com/triglan/SPGP_TermProject/blob/main/README.md

## 게임 소개
**Deadlock Duel**은 제한된 공간에서 전략적 이동과 공격 대기열을 기반으로 전투를 펼치는 턴제 퍼즐 액션 게임이다.  

### 시스템
- 턴제 시스템
- 공격 대기열 시스템
- 직업별 AI
- 스테이지 구성 관리
- 블럭 단위 맵 구현

---

###  참고 및 차용
- 버튼 및 정보 UI 구현
- 프레임워크 구조
- bgm 구현
- 애니메이션 구현

---

###  직접 구현한 것

- 공격 대기열 시스템 (공격 큐 추가 / 실행 구조) 및 무기 쿨타임
- Enemy 종류별 AI 패턴 (Rogue / Knight / Archer)
- 예고 공격, 머리 위 느낌표, 이펙트 타이밍 처리
- StageManager에 의한 스테이지 구성 및 적 배치
- UI 애니메이션
  

---

###  아쉬운 점

- 타이밍에 맞게 버튼을 눌러 피지컬 요소도 구현하고 싶었다. 수업  리듬게임에서 차용하고자 했으나 아쉽게도 못했다.
- 보스 및 3스테이지 클리어 구현 못함.
- 스테이지 클리어시 강화 이벤트 구현 못함.
- 게임이 진행되지 않는 버그로 인한 개발시간 감소.
더 좋은 수업이 되기 위해 변화할 점.
- 숙제를 너무 초반에 몰아쳐서 했던 것 같았다. 너무 빨랐던 수업 내용을 따라가기 벅찼는데,
숙제를 진행하면서 수업 내용을 따라갈 수 있었지만, 중반부터는 아에 숙제가 나가지 않아, 수업진도를 따라가기도 벅차고 진도도 매우 빨라 어려움을 겪었던 것 같다.
- 더 나은 수업을 위해서 숙제를 중간, 기말고사 기간 외에 일정하게 배분하고 숙제 내용과 관련해서 수업에서 언급하면서 학생들이 리마인드 할 수 있는 기회가 있으면
내가 공부했던게 여기서 쓰이구나 하면서 더 집중할 수 있을 것 같다.
- 추가로 많은 양의 프로젝트를 한 학기에 모두 하려다보니 오히려 수업의 질이 떨어진 것 같아 아쉬웠다. 학생들과 어느정도 템포를 맞출 수 있게, 위와같이 수업 내용에 대해 환기할 기회와 어느정도 수업 양을 압축해서 진행하면 더 얻어갈 것이 많은 수업이 될 수 있을 것 같다.

**********
## 현재까지의 진행 상황

| 주차   | 주요 작업 내용                                                         | 진행도 (%) |
|--------|------------------------------------------------------------------------|------------|
| 1주차  | 캐릭터 및 맵 리소스 찾기, 메인화면 버튼 클릭 이벤트 구현              | <p align="center">100%</p>      |
| 2주차  | 맵 타일 그리기, 캐릭터 배치 및 이동 버튼 구현                         | <p align="center">100%</p>      |
| 3주차  | 무기 시스템 구현, 플레이어 공격 구현                                   | <p align="center">70%</p>        |
| 4주차  | 적 캐릭터 3종 구현, 턴 기반 전투 시스템 구현                          | <p align="center">100%</p>       |
| 5주차  | 3라운드 로직 완성, 클리어 / 사망 처리 화면                            | <p align="center">100%</p>        |
| 6주차  | 적 AI 구현, 타격 효과 및 배경 음악 연결                                | <p align="center">100%</p>        |
| 7주차  | 플레이어/적 애니메이션, 무기강화, 추가 UI, 일시정지 기능 구현         | <p align="center">50%</p>        |
| 8주차  | 애니메이션 마무리, 디버깅 및 버그 수정, 최종 발표 준비                |   <p align="center">80%</p>    |


## git commit 을 얼마나 자주 했는지 알 수 있는 자료 (github-insights-commits 포함)
![Image](https://github.com/user-attachments/assets/69061e40-8e81-4060-948a-ec5053c94a71)



#### 인게임 이미지
![Image](https://github.com/user-attachments/assets/3cdae3ed-aa7e-4493-9367-7c671b6d05e5)

