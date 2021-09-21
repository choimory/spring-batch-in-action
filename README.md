# 개요

- H2의 특정조건, 특정기간의 데이터를 읽어 별도의 테이블에 작성하는 배치잡을 작성해보자
    - 동시에 Message Queue에도 메시지를 쏴보자
- Quartz로 스케줄링을 걸어 주기적으로 Migration 할 수 있도록 해보자
    - 브랜치와 yml을 이용하여 스케줄을 동적으로 관리하도록 해보자
- 단일 Entity가 아닌 List<Entity>를 Insert할 수 있는 ItemWriter를 작성해보자
- 버그를 수정한 RunIdIncrementer를 작성해보자
- Spring Batch QuerydslItemReader를 작성하여 쿼리를 Querydsl로 작성해보자
- Slack으로 Job 정보를 전송해보자
- Spring Batch에서의 테스트 코드를 작성해보자

# 스택

- Java 11
- Spring boot 2.5.4
- Spring Data JPA
- Querydsl
- H2, MariaDB
- Spring batch
- Quartz
- RabbitMQ, Apache Kafka
- Java mail
- Slack
