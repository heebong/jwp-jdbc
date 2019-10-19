# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 우아한테크코스 코드리뷰
* [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)


## DB

- [도커 다운로드](https://www.docker.com/products/docker-desktop)

### 실행 방법

1. IntelliJ IDEA에서 `docker-compose.yml`로 이동 후, Run 버튼 클릭
2. 혹은 프로젝트 디렉터리에서 아래의 명령어를 터미널에 입력

```bash
cd docker
docker-compose up -d
```

- 대용량 데이터 처리를 위한 Dataset Download

> [stack-overflow-2018-developer-survey](https://www.kaggle.com/stackoverflow/stack-overflow-2018-developer-survey)

- 아래의 메뉴얼을 확인하여 다운받은 CSV파일을 import한다.

> https://dev.mysql.com/doc/workbench/en/wb-admin-export-import-table.html



## GIT CONVENTION
https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716


## STEP1 요구사항
- **컴파일 에러가 나지 않게 리팩토링하기**
- [x] ```UserDao```의 ```findAll()``` 구현 (테스트 통과)
- [x] ```ConnectionManager.getConnection()```
- [x] ```UserDao```의 ```update()``` 구현 ( 테스트 통과)
- [x] ```pstmt.executeUpdate();``` 사용하는 부분 ```Template```으로 빼기 (```insert()```)
- [x] ```pstmt.executeQuery();``` 사용하는 부분 ```Template```으로 빼기 (```findById()```)
- [x]  ```ResultSet``` 부분 ```Template```으로 빼기 (```findById()```)
- [ ] ```JDBC Template``` ```static```으로 사용할 수 있게 변경
- [x] ```try-with-resource```로 바꾸기
- [ ] SQL 인자 매핑시, 사용자가 값만 넘겨줘도(배열) 매핑 되게끔 변경
- [x] ```JdbcTemplate.executeQuery``` 개선
    - [x] executeQueryForNs는 모든 로우 리턴, executeQueryForN 하면 무조건 최상위 로우 하나만 리턴되게 개선
    - [x] 가져온 로우를 특정 데이터 타입으로 바꿔주는 기능 추가
- [x] slipp에 있는 Controller의 ```DataBase``` 사용 부분을 ```UserDao```로 바꾸기
- [ ] ```UserDao```... 싱글 인스턴스로 만들게...? ㅠㅠ

