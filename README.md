# SensorMonitoringService

RESTful API для управления датчиками и связанными с ними измерениями.
Позволяет пользователям получать данные о сенсорах и измерениях, добавлять новые измерения и 
получать статистику по определенным критериям.

## 💻 Tech Stack
- Java 17
- Spring Boot 3
- Gradle
- PostgreSQL
- Liquibase
- Docker
- JUnit 5, Mockito

## Инструкция для локального запуска
1) Клонировать репозиторий
         
         git clone https://github.com/deeekul/SensorMonitoringService.git
2) Создать в корне проекта файл `.env` и прописать значения переменных окружений, представленных ниже

   - SERVER_PORT=
   - POSTGRES_PORT_MAPPING=
   - POSTGRES_USERNAME=
   - POSTGRES_PASSWORD=
   - POSTGRES_DATABASE=
   - POSTGRES_URL=
     
   <details>
      <summary>Пример .env файла</summary>
   
   - SERVER_PORT=8080
   - POSTGRES_PORT_MAPPING="5433:5432"
   - POSTGRES_USERNAME=user
   - POSTGRES_PASSWORD=password
   - POSTGRES_DATABASE=monitoringServiceDb
   - POSTGRES_URL=jdbc:postgresql://postgres:5432/monitoringServiceDb
  </details>

3) Для запуска тестов и сборки проекта в jar-файл выполнить команду

         ./gradlew build
4) Для запуска docker контейнеров перейти в корневую директорию и выполнить команду 

         docker-compose up -d
5) Перейти в браузере по нижеприведённому URL-адресу для ознакомления с документацией с помощью Swagger UI

         http://localhost:8080/swagger-ui/index.html#/
