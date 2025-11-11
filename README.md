# Silicium 3.0 Tests

## Общее описание
Проект содержит автотесты пользовательского интерфейса для страницы [Form Fields сайта Practice Automation](https://practice-automation.com/form-fields/) ,банковского приложения [XYZ Bank](https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager) и Rest API https://github.com/bondarenkokate73/simbirsoft_sdet_project. 

Набор сценариев для сайта practice-automation покрывает ключевые проверки формы: корректное заполнение всех полей, позитивный сценарий с заполнением только обязательных полей и негативный кейс с пропуском заполнения обязательных. 

Тест кейсы для сервиса XYZ Bank касаются исключительно функционала менеджера, включая создание аккаунта клиента банка, его удаление ,а так же корректность работы интерфейса, в частности сортировки в таблице со списком клиентов банка.
Тест кейсы для REST API касаются базового функционала CRUD, проверяется корректность чтения записи обновления и удаления записей.

Тесты запускаются в Chrome, снимаются скриншоты при падениях кейсов, а результат собирается в [Allure отчет](https://zen2281488.github.io/Silicium_3_0).

## Стек
- Java 17
- Maven 3.8+
- Selenium 4 (ChromeDriver через Selenium Manager)
- JUnit 5
- Allure Framework
- AspectJ (javaagent для вложений Allure)
- Hibernate
- Rest Assured
- Jackson
- Lombok

## Структура репозитория
- `pom.xml` — агрегирующий POM и общие версии зависимостей.
- `.github/workflows/workflow.yml` — GitHub Actions для сборки, запуска тестов и публикации Allure-отчёта в `gh-pages`.
- `UI/pom.xml` — модуль UI-тестов и конфигурация Surefire/Allure.
- `UI/src/main/java/page/anui/FormFieldsPage.java` — Page сайта practice-automation.
- `UI/src/main/java/page/xyzbank/ManagerPage.java` — Page сайта XYZ Bank.
- `UI/src/main/java/util` — расширения JUnit для WebDriver и Allure attach, утилиты, вынесенные из page методы с логикой.
- `UI/src/test/java/anui` — пакет с тестовым классом `FormFieldsTest` для сайта practice-automation.
- `UI/src/test/java/xyzbank` — тестовый класс `ManagerPage` для сайта XYZ Bank.
- `UI/src/test/resources/conf.properties` — базовые настройки запуска.
- `API/pom.xml` — модуль API-тестов (RestAssured, Allure, Hibernate, PostgreSQL).
- `API/src/main/java/models` — DTO для HTTP-пейлоадов и проверок БД.
- `API/src/main/java/utils/db` — `DbUtils`, `HibernateSessionFactoryUtil`, сервисы для чтения из PostgreSQL.
- `API/src/test/java/tests/api` — базовый тест и `CreationTests` (T‑007/T‑008).
- `API/src/test/java/utils/RestAssuredUtils.java` — хелперы для `/api/create` и `/api/delete/{id}`.
- `API/src/test/resources/conf.properties` — тестовые данные (title, addition, important numbers).


## Требования
- Установленные Java 17+ и Maven 3.8+ (`java -version`, `mvn -version`).
- Google Chrome (стабильный или Chromium) — Selenium Manager автоматически подберёт драйвер.
- Allure Commandline (опционально) для локального просмотра отчётов.

## Подготовка окружения
```bash
git clone https://github.com/zen2281488/Silicium_3_0.git
cd Silicium_3_0
```
Убедитесь, что Maven видит зависимости (`mvn -pl UI dependency:resolve`). Первичный прогон `mvn clean test` скачает необходимые артефакты.

## Конфигурация
Файл `UI/src/test/resources/conf.properties` содержит:
- `practiceAutomationBaseUrl` — адрес тестируемой страницы сайта practice-automation (по умолчанию `https://practice-automation.com/form-fields/`).
- `xyzBaseUrl` — адрес тестируемой страницы сайта XYZ Bank (по умолчанию `https://www.globalsqa.com/angularJs-protractor/BankingProject/#/`).
- `headlessMode` — `true` для headless запуска (используется в CI). Измените на `false`, если нужно увидеть браузер локально.

## Запуск тестов
- Полный прогон из корня проекта:
  ```bash
  mvn clean test \
    -DDB_USER={Имя пользователя в бд} \
    -DDB_PASSWORD={Пароль пользователя в бд} \
    -DDB_URL={Эндпоинт}
  ```
- Запуск только UI-модуля:
  ```bash
  mvn -pl UI -am clean test
  ```
- Избирательный запуск класса или метода (пример):
  ```bash
  mvn -pl UI -Dtest=FormFieldsTest#fillFormPTest test
  ```
- Запуск только API-модуля (не забудьте указать подключение к БД/сервису):
  ```bash
  mvn -pl API -am clean test \
    -DDB_USER=test \
    -DDB_PASSWORD=test \
    -DDB_URL=jdbc:postgresql://93.113.171.2:5432/test
  ```
  Также API-тесты требуют, чтобы удалённый сервис `http://93.113.171.2:8080/api/` был доступен.

Результаты Allure сохраняются в `UI/target/allure-results` и `API/target/allure-results` соответственно.

## Отчёты Allure
Для локального просмотра используйте любой из вариантов:
```bash
mvn -pl UI allure:report && mvn -pl UI allure:serve
# или, при установленном allure CLI
allure serve UI/target/allure-results
```
![img.png](img.png)
GitHub Actions генерирует отчёт в ветке `gh-pages` ([пример доступен по адресу](https://zen2281488.github.io/Silicium_3_0)) и сохраняет историю между прогонами.

## Список тест кейсов

---
<details>
  <summary>Кейсы для practice-automation.com</summary>

| № | — |
|---|---|
| **ID** | **T-001** |
| **Описание** | Smoke-проверка полной отправки формы Form Fields. |
| **Предусловие** | Открыта страница `https://practice-automation.com/form-fields/`. |
| **Шаги** | 1. Ввести «Testovlev Test Testovich» в Name (`id="name-input"`).<br>2. Ввести «qwertyTest123» в Password (`input[type="password"]`).<br>3. Отметить чекбоксы «Coffee» и «Milk».<br>4. Выбрать цвет «Yellow».<br>5. В селекторе Do you like automation? выбрать «Yes».<br>6. Ввести «test@test.ru» в Email (`id="email"`).<br>7. Ввести «5 Katalon Studio» в Message (`id="message"`).<br>8. Нажать Submit (`id="submit-btn"`). |
| **Ожидаемый результат** | Появляется alert с текстом «Message received!». |

---

| № | — |
|---|---|
| **ID** | **T-002** |
| **Описание** | Отправка формы с заполнением только обязательного поля Name. |
| **Предусловие** | Страница `https://practice-automation.com/form-fields/` открыта. |
| **Шаги** | 1. Ввести имя в поле Name.<br>2. Нажать Submit. |
| **Ожидаемый результат** | Alert «Message received!». |

---

| № | — |
|---|---|
| **ID** | **T-003** |
| **Описание** | Проверка нативной валидации Name при пустом значении. |
| **Предусловие** | Страница `https://practice-automation.com/form-fields/` открыта. |
| **Шаги** | 1. Заполнить все поля, кроме Name.<br>2. Нажать Submit. |
| **Ожидаемый результат** | Поле Name подсвечено `:invalid`, alert не появляется. |

</details>

---
<details>
  <summary>Кейсы для XYZ Bank</summary>

| № | — |
|---|---|
| **ID** | **T-004** |
| **Описание** | Создание нового клиента банка. |
| **Предусловие** | Открыта страница `https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager`. |
| **Шаги** | 1. Нажать Add Customer.<br>2. Заполнить First Name (рандом), Last Name = «Testovlev», Post Code (рандом).<br>3. Нажать Submit.<br>4. В Open Account проверить наличие клиента в селекторе.<br>5. Перейти в Customers и найти созданного клиента. |
| **Ожидаемый результат** | Alert «Customer added successfully with customer id : <id>». Клиент доступен в Open Account и Customers. |

---

| № | — |
|---|---|
| **ID** | **T-005** |
| **Описание** | Проверка сортировки клиентов по First Name. |
| **Предусловие** | Раздел Customers открыт. |
| **Шаги** | 1. Кликнуть по заголовку First Name (ASC).<br>2. Убедиться, что значения возрастают.<br>3. Кликнуть повторно (DESC).<br>4. Убедиться, что значения убывают. |
| **Ожидаемый результат** | После первого клика — сортировка по возрастанию, после второго — по убыванию. |

---

| № | — |
|---|---|
| **ID** | **T-006** |
| **Описание** | Удаление клиента Neville Longbottom. |
| **Предусловие** | Раздел Customers открыт. |
| **Шаги** | 1. Найти строку Neville / Longbottom.<br>2. Нажать Delete в строке клиента.<br>3. Обновить список или перейти в Open Account. |
| **Ожидаемый результат** | Клиент отсутствует в таблице и селекторе. |

</details>

---
<details>
  <summary>Кейсы для API https://github.com/bondarenkokate73/simbirsoft_sdet_project</summary>

| № | — |
|---|---|
| **ID** | **T-007** |
| **Описание** | Позитивное создание сущности через `POST /api/create` с проверкой БД. |
| **Предусловие** | Сервис и БД доступны; заданы `DB_USER`, `DB_PASSWORD`, `DB_URL`. |
| **Шаги** | 1. Отправить `POST /api/create` с телом из `conf.properties` (title, verified, important_numbers, addition).<br>2. Получить ID из ответа (ожидаемый код 200 и `text/plain`).<br>3. Найти запись в таблицах `entities` и `additions` через Hibernate-сервисы.<br>4. Сравнить поля с отправленными значениями. |
| **Ожидаемый результат** | Сущность сохранена в обеих таблицах, данные совпадают с запросом. |

---

| № | — |
|---|---|
| **ID** | **T-008** |
| **Описание** | Позитивное удаление сущности через `DELETE /api/delete/{id}`. |
| **Предусловие** | Как в T-007; сущность существует (создана в тесте). |
| **Шаги** | 1. Создать сущность (как в T-007) и получить ID.<br>2. Убедиться, что запись присутствует в БД.<br>3. Отправить `DELETE /api/delete/{id}` (ожидаемый код 204).<br>4. Проверить, что записи в `entities` и `additions` с этим ID удалены. |
| **Ожидаемый результат** | Сущность отсутствует в таблицах, API вернул 204 без тела. |

---

| № | — |
|---|---|
| **ID** | **T-009** |
| **Описание** | Позитивное получение сущности по ID через `GET /api/get/{id}` с проверкой БД. |
| **Предусловие** | Как в T-007. |
| **Шаги** | 1. Создать сущность и сохранить её ID.<br>2. Выполнить `GET /api/get/{id}` (ожидаемый код 200, `application/json`).<br>3. Считать запись из БД (`entities`, `additions`).<br>4. Сравнить, что поля ответа API (`id`, `title`, `verified`, `important_numbers`, addition) совпадают с записью БД. |
| **Ожидаемый результат** | Возвращается корректный JSON-объект, полностью совпадающий с данными в БД. |

---

| № | — |
|---|---|
| **ID** | **T-010** |
| **Описание** | Позитивное получение списка через `GET /api/getAll` с проверкой наличия созданных сущностей. |
| **Предусловие** | Как в T-007; требуется минимум две созданные записи. |
| **Шаги** | 1. Создать две сущности и получить их ID.<br>2. Убедиться, что записи есть в БД.<br>3. Выполнить `GET /api/getAll` (ожидаемый код 200, `application/json`).<br>4. Найти созданные ID в ответе и сравнить их поля с БД. |
| **Ожидаемый результат** | В списке присутствуют обе созданные сущности, значения всех полей совпадают с данными БД. |

</details>

