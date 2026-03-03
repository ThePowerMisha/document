
# Сборка проекта:

## Зависимотси 
* JDK 21 or later
* Gradle 8.x

## Сборка проекта

```bash
./gradlew build
```

## Запуск основного проекта 
```bash
.\gradlew :document-processing:bootRun
```

При запуске проекта liquibase создает схемы в БД

---

### Для запуска модуля для обновления статусов документов
```bash
.\gradlew :background-worker:bootRun
```
В файле application.yaml хранятся настройки
```yaml
batch-size: 100       # размер пачки сколько документов берется для перевода статуса SUBMIT/APPROVE
submit-delay: 10000   # время в мс через которая будет происходить перевод документов в статус SUBMITTED
approve-delay: 15000  # время в мс через которая будет происходить перевод документов в статус APPROVED
```


После запуска появятся логи в консоле 

```text
2026-02-26T20:11:54.166+05:00  INFO 27896 --- [   scheduling-1] c.t.s.service.DocumentSchedulerService   : (SUBMIT) Draft documents found: 100
2026-02-26T20:11:54.362+05:00  INFO 27896 --- [   scheduling-1] c.t.s.service.DocumentSchedulerService   : (SUBMIT) Submitted documents count: 100
```

По логам можно отслеживать:

* количество найденных документов
* количество переведённых в следующий статус
* работу планировщика
---
### Для запуска генерации новых документов
```bash
.\gradlew :document-generator:bootRun
```

```text
2026-02-26T20:11:21.109+05:00  INFO 22044 --- [           main] c.t.generator.service.GeneratorService   : Created document 181/200 Status: 200 OK
2026-02-26T20:11:21.111+05:00  INFO 22044 --- [           main] c.t.generator.service.GeneratorService   : Created document 182/200 Status: 200 OK
```

* количество созданных документов
* статус созданного документа
* время создания документа

В файле application.yaml хранятся настройки
```yaml
document-count: 200                           # ко-во документов для генерации
api-url: http://localhost:8080/api/document   # адрес куда отправлять запрос для генерации
```

---

# API

## AuthorController

---

## 🔑 Получить токен автора

Получение токена пользователя по имени.

### Endpoint

```text
/api/user/{authorName}/token
```

### Path Parameters

| Параметр | Тип    | Описание              |
|----------|--------|-----------------------|
| name     | String | Имя автора (username) |

### Пример запроса

```http
GET http://localhost:8080/api/user/Author/token
```

### Пример ответа
```text
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Описание

Возвращает токен автора в виде строки.

Используется для авторизации или идентификации пользователя в системе.

---

## 👥 Получить список имён авторов

Возвращает список всех зарегистрированных авторов.

### Endpoint

```text
GET /api/user/get-authors-names
```

### Пример запроса

```http
GET http://localhost:8080/api/user/get-authors-names
```
Пример ответа
```text
[
"Author1",
"Author2",
"Author3"
]
```
### Описание
Возвращает список имён авторов.

Используется для отображения доступных авторв.


## DocumentController


