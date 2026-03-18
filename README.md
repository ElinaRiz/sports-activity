# SportsActivity - система для записи на тренировки

Микросервисное приложение для управления записями пользователей на тренировки в спорткомплексе.  
Позволяет вести учёт пользователей, управлять записями и собирать аналитическую статистику.

## Архитектура

Приложение построено на микросервисной архитектуре.  
Сервисы взаимодействуют через REST API (с использованием WebClient) и асинхронные сообщения (RabbitMQ). 
Для обнаружения и регистрации сервисов используется Eureka Server.

## Структура приложения

- **auth-service** - сервис аутентификации и авторизации пользователей;
- **management-service** - управление пользователями и записями на тренировки;
- **analytics-service** - сбор и анализ статистики записей на тренировки;
- **frontend** - клиентская часть с пользовательским интерфейсом;
- **eureka-server** - реестр сервисов (Service Discovery);
- **api-gateway** - единая точка входа для клиентских запросов.

## Запуск

1. Запустить eureka-server.
2. Запустить auth-service, management-service, analytics-service.
3. Запустить api-gateway.
4. Запустить frontend.

## Стек технологий

- **auth-service**: Java, Spring Boot, Spring Data JPA, Spring Security, JWT, Eureka Client
- **management-service**: Java, Spring Boot, Spring Data JPA, Jackson Databind, Spring Security, Eureka Client, Spring AMQP, WebClient
- **analytics-service**: Python, FastAPI, Uvicorn, SQLAlchemy, PostgreSQL (psycopg2-binary), python-dotenv, aio-pika (RabbitMQ), py-eureka-client, requests
- **frontend**: Java, Spring Boot, Spring Security, JWT, Eureka Client
- **eureka-server**: Java, Spring Boot, Eureka Server
- **api-gateway**: Java, Spring Boot, Spring Cloud Gateway, Spring Cloud LoadBalancer, Eureka Client