# ![RealWorld Example App](logo.png)


[![Java CI with Gradle](https://github.com/Shim8934/realworld-jpa/actions/workflows/gradle.yml/badge.svg?branch=JwtWithOauth2)](https://github.com/Shim8934/realworld-jpa/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/github/Shim8934/realworld-jpa/graph/badge.svg?token=1FHT31HX3H)](https://codecov.io/github/Shim8934/realworld-jpa)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

[ReadWorld.io](https://github.com/gothinkster/realworld) `spring-security`, `spring-data-jpa`, `Java 17`를 사용한 Backend 프로젝트 

# Getting started

## Run Application
``` shell
$ ./gradlew build bootRun
```

---

## Test Application
### [1. Test With Gradle](####1.-gradle-test)
### [2. Test Report By Jacoco](####2.-jacoco-test-report)
### [3. Test with Running Application](####3.-using-conduit.postman_collection.json)

#### 1. Gradle Test
`gradlew` 사용한 테스트 방법  
``` shell
$ ./gradlew clean test
```
![GradlewTest](./docs/study/gradlewtest.jpg)  


#### 2. Jacoco Test Report
```text
build/reports/jacoco/test/html/index.html
```
![JacocoTestReport](./docs/study/jacocoTestReport.jpg)  

---

#### 3. Using Conduit.postman_collection.json
1) `Postman` 프로그램 사용  
- `./docs/api/Conduit.postman_collection.json` 파일을 Postman 프로그램에서 `Import` 후 Test 진행.  
2) `shell` 사용  
    ```shell
    $ ./docs/api/run-api-tests.sh
    ```

---

