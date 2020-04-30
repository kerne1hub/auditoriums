# Auditorium Agent

[![Build Status](https://travis-ci.com/kerne1hub/auditoriums.svg?branch=master)](https://travis-ci.com/kerne1hub/auditoriums)
[![codecov](https://codecov.io/gh/kerne1hub/auditoriums/branch/master/graph/badge.svg)](https://codecov.io/gh/kerne1hub/auditoriums)

Auditorium Agent -- сервис, разрабатываемый для учебных  заведений, который позволяет смотреть занятость аудиторий.

  - Просмотр занятости аудиторий
  - Просмотр расписания занятий
  - Управление занятиями

# Что уже сделано?

  - Структура базы данных
  - Эндпоинт auditoriums для управления аудиториями
  - Эндпоинт buildings для управления корпусами *[NEW]*
  - Эндпоинт groups для управления группами
  - Эндпоинт lecturers для управления преподавателями
  - Эндпоинт subjects для управления предметами
  - Эндпоинт lectures для управления лекциями



### Установка

Auditorium Agent требует для запуска JRE 8 [Java](https://www.java.com/ru/download/manual.jsp) v8+.

Также вам необходимо иметь базу данных MySQL. Параметры для базы данных:

```sh
database = auditoriums
user = kernel
password = ${dbPass}
```

Эти параметры заданы в файлах resources/application*.properties. Вам нужно будет заменить эти данные на свои. Создать базу данных и пользователя, и дать пользователю полные права на базу данных.

### Запуск

Для запуска приложения откройте терминал и выполните команды [UNIX]:

```sh 
$ cd backend
$ ./mvnw spring-boot run
```
Для Windows выполните в командной строке:
```sh 
$ cd backend
$ mvnw.cmd spring-boot run
```

Если вы не указали явно параметры (например, пароль) в properties, то перед запуском вам нужно выполнить следующую команды:

```sh
$ export dbPass=yourpassword
```
После запуска структура базы данных (таблицы) будет создана автоматически

### Тесты

Для запуска тестов откройте терминал и выполните команды [UNIX]:

```sh 
$ cd backend
$ ./mvnw spring-boot test
```
Для Windows выполните в командной строке:
```sh 
$ cd backend
$ mvnw.cmd spring-boot test
```

По умолчанию тесты используют базу данных auditoriums_test
