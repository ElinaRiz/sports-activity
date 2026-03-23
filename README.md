# SportsActivity - система для записи на тренировки

Микросервисное приложение для управления записями пользователей на тренировки в спорткомплексе.  
Позволяет вести учёт пользователей, управлять записями и собирать аналитическую статистику.

## Архитектура

Приложение построено на микросервисной архитектуре.  
Сервисы взаимодействуют через REST API (с использованием WebClient) и асинхронные сообщения (RabbitMQ). 
Для обнаружения и регистрации сервисов используется Eureka Server. Данные хранятся в PostgreSQL.

## Структура приложения

- **auth-service** - аутентификация и авторизация пользователей (Spring Boot);
- **management-service** - управление пользователями и записями на тренировки (Spring Boot);
- **analytics-service** - сбор и анализ статистики записей на тренировки (FastAPI);
- **frontend** - клиентская часть с пользовательским интерфейсом (Angular);
- **eureka-server** - сервис обнаружения (Eureka Server);
- **api-gateway** - единая точка входа для клиентских запросов (Spring Cloud Gateway).

## Запуск

1. Запустить eureka-server.
2. Запустить auth-service, management-service, analytics-service.
3. Запустить api-gateway.
4. Запустить frontend.

## Стек технологий

- **auth-service**: Java, Spring Boot, Spring Data JPA, Spring Security, JWT, Eureka Client
- **management-service**: Java, Spring Boot, Spring Data JPA, Jackson Databind, Spring Security, Eureka Client, Spring AMQP, WebClient
- **analytics-service**: Python, FastAPI, Uvicorn, SQLAlchemy, PostgreSQL (psycopg2-binary), python-dotenv, aio-pika (RabbitMQ), py-eureka-client, requests
- **frontend**: TypeScript, Angular, Bootstrap
- **eureka-server**: Java, Spring Boot, Eureka Server
- **api-gateway**: Java, Spring Boot, Spring Cloud Gateway, Spring Cloud LoadBalancer, Eureka Client
