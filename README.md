# Accounting of books

Создать приложение хранящее информацию о книгах в библиотеке.
Использовать Spring JDBC и реляционную базу.

Опционально использовать настоящую реляционную БД, но можно использовать H2.

Предусмотреть таблицы авторов, книг и жанров.

Интерфейс на Spring Shell

Покрыть тестами, насколько это возможно.

НЕ делать AbstractDao.

НЕ делать наследования в тестах 

---
## Добавление для домашнего задания №2
- Переписать приложение для хранения книг на ORM.
- Использовать JPA, Hibernate только в качестве JPA-провайдера.
- Добавить комментарии к книгам, и высокоуровневые сервисы, оставляющие комментарии к книгам.
- Покрыть DAO тестами используя H2 базу данных и соответствующий H2 Hibernate-диалект.
- *Опционально, использовать liquibase*

---
## Добавление для домашнего задания №3
- Переписать библиотеку на Spring Data JPA.
- Реализовать весь функционал работы с БД в приложении книг с использованием spring-data-jpa репозиториев.