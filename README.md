# Auto Parts Shop

Пълнофункционално приложение за управление на магазин за авточасти. Проектът използва Spring Boot за бекенда и е контейнеризиран с Docker.

## Основни характеристики

- Бекенд със Spring Boot
- Структура на Maven проект
- Контейнеризация с Docker и Docker Compose
- Лесно изграждане и стартиране с `mvn package` и `docker-compose`

## Необходими изисквания

- Java 23 или по-нова
- Maven 3.6 или по-нова
- Инсталирани Docker и Docker Compose

## Структура на проекта

```
.
├── docker-compose.yml
├── backend/
│ ├── Dockerfile
│ ├── pom.xml
│ └── src/...
```
## Изграждане и стартиране

1. **Създаване на .jar файл с Maven**  
   От директорията `backend/` се изпълнява:

   ```bash
   mvn package
    ```

2. **Стартиране с Docker Compose**
   От основната директория на проекта (където е docker-compose.yml):

   ```bash
   docker-compose up --build
    ```

## Достъп то проекта

След като контейнерите се стартират, достъпа до проекта става чрез:
```
http://localhost:8080
```
