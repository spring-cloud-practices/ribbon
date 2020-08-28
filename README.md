# Spring REST API
Spring REST API 예제입니다.  

# 목차  
- [Getting Started](#Getting-Started)  
- [Overview](#Overview)  
- [Version](#Version)
- [Restdocs](#Restdocs)
- [Client](#Client)
- [References](#References)    


---  


# 버전에 따른 코드수정 사항
> ## spring boot 1.5.x -> spring boot 2.2.x 수정사항
- JPA method `findOne()` -> `findById()` or `getOne()`  
[springboot 1.5.x 에서 springboot 2.0.x 넘어가면서 생긴일..](https://hspmuse.tistory.com/entry/springboot-15x-%EC%97%90%EC%84%9C-springboot-20x-%EB%84%98%EC%96%B4%EA%B0%80%EB%A9%B4%EC%84%9C-%EC%83%9D%EA%B8%B4%EC%9D%BC)  
- `EmbeddedServletContainerInitializedEvent` removed  
https://stackoverflow.com/questions/56982909/why-i-am-getting-this-error-embeddedservletcontainerinitializedevent-cannot-be
- `ResourceSupport` changed to `RepresentationModel`
- `Resource` changed to `EntityModel`
- `Resources` changed to `CollectionModel`
- `PagedResources` changed to `PagedModel`
- `ResourceAssembler` changed to `RepresentationModelAssembler`  
https://stackoverflow.com/questions/25352764/hateoas-methods-not-found

# Project 구조
```cmd
❯ tree
├─client                                <-- rest template / traverson client
├─http                                  <-- http request file
│   ├── restdocs.http
│   └── version.http
├─restdocs                              <-- spring rest docs
├─scripts                               <-- start scripts
│   └── start.sh
└─version                               <-- api versioning
```  


# version module (API Versioning)
> ## version server 시작하기  
```cmd
$ ./scripts/start.sh version
```  


> ## api 요청
아래와 같은 4가지 Endpoint가 존재한다.
```java
package version.demo;

import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class VersionedRestController {

    private static final String V1_MEDIA_TYPE_VALUE = "application/vnd.bootiful.demo-v1+json";
    private static final String V2_MEDIA_TYPE_VALUE = "application/vnd.bootiful.demo-v2+json";

    @GetMapping(value = "/{version}/hi", produces = APPLICATION_JSON_VALUE)
    public Greeting greetingWithPathVariable(@PathVariable ApiVersion version) {
        return greet(version, "path-variable");
    }

    @GetMapping(value = "/hi", produces = APPLICATION_JSON_VALUE)
    public Greeting greetWithHeader(@RequestHeader("X-API-Version") ApiVersion version) {
        return greet(version, "header");
    }

    @GetMapping(value = "/hi", produces = V1_MEDIA_TYPE_VALUE)
    public Greeting greetWithContentNegotiationV1() {
        return greet(ApiVersion.v1, "content-negotiation");
    }

    @GetMapping(value = "/hi", produces = V2_MEDIA_TYPE_VALUE)
    public Greeting greetWithContentNegotiationV2() {
        return greet(ApiVersion.v2, "content-negotiation");
    }

    private Greeting greet(ApiVersion version, String how) {
        return new Greeting(how, version);
    }

}
```


- http 디렉토리의 `version.http`의 http 요청을 실행한다.  
```
### /api/{version}hi path-variable
GET {{host}}/api/v1/hi

### /api/hi header
GET {{host}}/api/hi
X-API-Version: v2

### /api/hi content-negotiation
GET {{host}}/api/hi
Accept: application/vnd.bootiful.demo-v2+json
```


- `path-variable`을 이용한 버저닝 
```
GET http://localhost:8080/api/v1/hi

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 18 Aug 2020 14:05:56 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "how": "path-variable",
  "version": "v1"
}

Response code: 200; Time: 271ms; Content length: 38 bytes
```  


- `header(X-API-Version)`를 이용한 버저닝
```
GET http://localhost:8080/api/hi

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 18 Aug 2020 14:09:39 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "how": "header",
  "version": "v2"
}

Response code: 200; Time: 31ms; Content length: 31 bytes
```


- `컨텐츠 협상(Content Negotiation)`을 이용한 버저닝
```
GET http://localhost:8080/api/hi

HTTP/1.1 200 
Content-Type: application/vnd.bootiful.demo-v2+json
Transfer-Encoding: chunked
Date: Tue, 18 Aug 2020 14:10:21 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "how": "content-negotiation",
  "version": "v2"
}

Response code: 200; Time: 31ms; Content length: 44 bytes
```

요청에 따라 응답값(version, how)이 변경된 것을 확인할 수 있다.


---


# restdocs module (spring restdocs)
> ## restdocs 생성하기
```cmd
$ ./scripts/install.sh restdocs
```  


> ## 생성된 restdocs 파일 확인하기  
```cmd
$ ls restdocs/target/generated-snippets/
error-example/  index-example/
$ ls restdocs/target/generated-docs/
api-guide.html
```  


> ## restdocs server 시작하기  
```cmd
$ ./scripts/start.sh restdocs
```  

> ## 생성된 html 웹 서버에서 확인하기   
- http 디렉토리의 `restdocs.http`의 http 요청을 실행한다.  
```
### /docs/api-guide.html
GET {{host}}/docs/api-guide.html
```

- restdocs html 문서 내용 중략
```
GET http://localhost:8080/docs/api-guide.html

HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Last-Modified: Tue, 18 Aug 2020 14:27:21 GMT
Cache-Control: no-store
Accept-Ranges: bytes
Content-Type: text/html
Content-Length: 39335
Date: Tue, 18 Aug 2020 14:32:34 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"><![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="generator" content="Asciidoctor 1.5.8">

    ......

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/github.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/highlight.min.js"></script>
<script>hljs.initHighlighting()</script>
</body>
</html>

Response code: 200; Time: 0ms; Content length: 39335 bytes
```

> ## 빌드 플러그인 설정 
`pom.xml` 파일의 `build` -> `plugins`에 설정이 필요하다.

- `asciidoctor-maven-plugin` 설정 시 `generated-snippets` 폴더 및 하위파일과 `generated-docs` 폴더 및 하위파일이 생성된다.
- `maven-resources-plugin` 설정은 웹 서버 기동 시 `generated-docs/api-guide.html` 파일을 `resources/static/docs/` 에 복사한다.


---


# client module (`RestTemplate` / `Traverson`)
> ## client server 시작하기
```cmd
$ ./scripts/start.sh client
```

> ## rest api 확인하기
`spring-boot-starter-data-rest` 의존성을 주입하면 생성한 Repository에 대한 rest api를 자동으로 생성해준다.
- http 디렉토리의 `client.http`의 http 요청을 실행한다.
```
### /movies
GET {{host}}/movies

### /movies/1
GET {{host}}/movies/1

...

### /actors
GET {{host}}/actors

### /actor/search
GET {{host}}/actors/search

### /actor/search/by-movie{?movie,page,size,sort}
GET {{host}}/actors/search/by-movie?movie=Cars&page=0&size=20
```

- get /actors 요청 결과 중 `_links` 정보를 확인하여 다른 api 요청을 할 수 있다.
```
GET http://localhost:8080/actors

HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Tue, 18 Aug 2020 16:46:11 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "_embedded": {
    "actors": [
      {
        "fullName": "Owen Wilson",
        "_links": {
          "self": {
            "href": "http://localhost:8080/actors/2"
          },
          "actor": {
            "href": "http://localhost:8080/actors/2"
          },
          "movie": {
            "href": "http://localhost:8080/actors/2/movie"
          }
        }
      },
      ...
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/actors"
    },
    "profile": {
      "href": "http://localhost:8080/profile/actors"
    },
    "search": {
      "href": "http://localhost:8080/actors/search"
    }
  },
  "page": {
    "size": 20,
    "totalElements": 6,
    "totalPages": 1,
    "number": 0
  }
}

Response code: 200; Time: 78ms; Content length: 2428 bytes
```

- `_links` 정보 중 `search` api 호출 결과
```
GET http://localhost:8080/actors/search

HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Tue, 18 Aug 2020 16:49:10 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "_links": {
    "by-movie": {
      "href": "http://localhost:8080/actors/search/by-movie{?movie,page,size,sort}",
      "templated": true
    },
    "self": {
      "href": "http://localhost:8080/actors/search"
    }
  }
}

Response code: 200; Time: 31ms; Content length: 243 bytes
```
- `_links` 정보 중 `by-movie` api 호출 결과
```
GET http://localhost:8080/actors/search/by-movie?movie=Cars&page=0&size=20

HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Tue, 18 Aug 2020 16:50:36 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "_embedded": {
    "actors": [
      {
        "fullName": "Owen Wilson",
        "_links": {
          "self": {
            "href": "http://localhost:8080/actors/2"
          },
          "actor": {
            "href": "http://localhost:8080/actors/2"
          },
          "movie": {
            "href": "http://localhost:8080/actors/2/movie"
          }
        }
      },
      ...
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/actors/search/by-movie?movie=Cars&page=0&size=20"
    }
  },
  "page": {
    "size": 20,
    "totalElements": 3,
    "totalPages": 1,
    "number": 0
  }
}

Response code: 200; Time: 187ms; Content length: 1294 bytes
```

- 위와 같은 일련의 요청들(actors, search, by-movie)을 Traverson 라이브러리를 사용하면 간편하게 호출할 수 있다.
```java
@Slf4j
public class TraversonTests {

    private Traverson traverson;

    @Test
    public void tearTraverson() throws Exception {

        String nameOfMovie = "Cars";

        // <1>
        CollectionModel<Actor> actorCollectionModel = traverson
                .follow("actors", "search", "by-movie")
                .withTemplateParameters(Collections.singletonMap("movie", nameOfMovie))
                .toObject(new ParameterizedTypeReference<>() {});

        assert actorCollectionModel != null;
        actorCollectionModel.forEach(a -> log.info(a.toString()));
        Assertions.assertTrue(actorCollectionModel.getContent().size() > 0);
        Assertions.assertEquals(
                (int) actorCollectionModel.getContent().stream()
                        .filter(a -> a.fullName.equals("Owen Wilson")).count(), 1
        );
    }
}
```  
 

---


# References  
- https://github.com/cloud-native-java/rest  