# Silicium 3.0 UI Tests

## Общее описание
Проект содержит автотесты пользовательского интерфейса для страницы [Form Fields сайта Practice Automation](https://practice-automation.com/form-fields/). Набор сценариев покрывает ключевые проверки формы: корректное заполнение всех обязательных полей, позитивный сценарий с заполнением только обязательных полей и негативный кейс с пропуском заполнения обязательных. Тесты запускаются в Chrome, снимается скриншоты при падениях кейсов, а результат собирается в [Allure отчет](https://zen2281488.github.io/Silicium_3_0).

## Стек
- Java 17
- Maven 3.8+
- Selenium 4 (ChromeDriver через Selenium Manager)
- JUnit 5
- Allure Framework
- AspectJ (javaagent для вложений Allure)

## Структура репозитория
- `pom.xml` — агрегирующий POM и общие версии зависимостей.
- `.github/workflows/workflow.yml` — GitHub Actions для сборки, запуска тестов и публикации Allure-отчёта в `gh-pages`.
- `UI/pom.xml` — модуль UI-тестов и конфигурация Surefire/Allure.
- `UI/src/main/java/page` — Page.
- `UI/src/main/java/util` — расширения JUnit для WebDriver и Allure attach, утилиты.
- `UI/src/test/java/anui` — тестовые классы (`BaseTest`, `FormFieldsTest`).
- `UI/src/test/resources/conf.properties` — базовые настройки запуска.

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
- `baseUrl` — адрес тестируемой страницы (по умолчанию `https://practice-automation.com/form-fields/`).
- `headlessMode` — `true` для headless запуска (используется в CI). Измените на `false`, если нужно увидеть браузер локально.

## Запуск тестов
- Полный прогон из корня проекта:
  ```bash
  mvn clean test
  ```
- Запуск только UI-модуля:
  ```bash
  mvn -pl UI -am clean test
  ```
- Избирательный запуск класса или метода (пример):
  ```bash
  mvn -pl UI -Dtest=FormFieldsTest#fillFormPTest test
  ```
Результаты Allure сохраняются в `UI/target/allure-results`.

## Отчёты Allure
Для локального просмотра используйте любой из вариантов:
```bash
mvn -pl UI allure:report && mvn -pl UI allure:serve
# или, при установленном allure CLI
allure serve UI/target/allure-results
```
GitHub Actions генерирует отчёт в ветке `gh-pages` ([ознакомится с примером отчета можно по этому адресу](https://zen2281488.github.io/Silicium_3_0)) и сохраняет историю между прогонами.

## Список тест кейсов

---

### **Тестрование основной функциональности форм**

| №1          | -                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| ----------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ID          | T-001                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| Описание    | Тестирование работоспособности базовой функциональности формы Form Fields.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| Предусловие | Открыта страница https://practice-automation.com/form-fields/                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| Шаги        | 1. Ввести «Testovlev Test Testovich» в поле имени `id="name-input"`.<br>2. Ввести «qwertyTest123» в поле пароля `css = input[type="password"]`.<br>3. Отметить чекбокс  «Coffee» и «Milk» из списка чекбоксов What is your favorite drink? `css = input[name='fav_drink']`.<br>4. Выбрать цвет «Yellow» в списке What is your favorite color? `css = input[name='fav_color']`.<br>5. Выбрать значение «Yes» в селекторе Do you like automation? `id="automation"`.<br>6. Ввести «test@test.ru» в поле email `id="email"`.<br>7. Ввести в поле Message `id="message"` текст "5 Katalon Studio".<br>8. Нажать кнопку Submit`id="submit-btn"`. |
| Ожидаемое   | Появился алерт с текстом «Message received!».                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
---

| №2          | -                                                                                                                       |
|-------------|-------------------------------------------------------------------------------------------------------------------------|
| ID          | T-002                                                                                                                   |
| Описание    | Тестирование работоспособности функциональности формы Form Fields при заполнении исключительно обязательного поля Name. |
| Предусловие | Открыта страница https://practice-automation.com/form-fields/                                                           |
| Шаги        | 1. Ввести «Testovlev Test Testovich» в поле Name `id="name-input"`.<br>2. Нажать кнопку Submit`id="submit-btn"`.        |
| Ожидаемое   | Появился алерт с текстом «Message received!».                                                                           |     |

---

| №3          | -                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ID          | T-003                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| Описание    | Проверка нативной валидации поля Name при попытке отправить форму без имени.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| Предусловие | Открыта страница https://practice-automation.com/form-fields/                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| Шаги        | 1. Ввести «qwertyTest123» в поле Password `css = input[type="password"]`.<br>2. Отметить чекбоксы «Coffee» и «Milk» из списка What is your favorite drink? `css = input[name='fav_drink']`.<br>3. Выбрать цвет «Yellow» в списке What is your favorite color? `css = input[name='fav_color']`.<br>4. Выбрать значение «Yes» в селекторе Do you like automation? `id="automation"`.<br>5. Ввести «test@test.ru» в поле Email `id="email"`.<br>6. Ввести в поле Message `id="message"` текст "5 Katalon Studio".<br>7. Нажать кнопку Submit`id="submit-btn"`. |
| Ожидаемое   | Поле Name подсвечено нативной валидацией `:invalid`, алерт с текстом «Message received!» не отображается.                                                                                                                                                                                                                                                                                                                                                                                                                                                   |


## CI/CD
Workflow `.github/workflows/workflow.yml` запускается на каждый push (кроме изменений только в Markdown-файлах), готовит среду с Chrome и Allure CLI, выполняет `mvn clean test`, собирает отчёт и публикует его в `gh-pages`. При падении тестов job помечается как неуспешный.



