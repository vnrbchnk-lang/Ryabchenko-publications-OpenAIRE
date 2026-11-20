# Публикации (OpenAIRE) — данные из сети на Compose Multiplatform

ЛР4, веха В2 «Данные». Курс кроссплатформенной разработки, группа ПИН-252т.

## Предметная область

Каталог научных публикаций из [OpenAIRE Graph](https://graph.openaire.eu/). OpenAIRE собирает
статьи, датасеты и программы из репозиториев и журналов в один открытый граф. В основном в каталоге
статьи, но встречаются и датасеты с программами, это видно по полю `type`.

Что есть в приложении:

- список публикаций: название, авторы, дата, подгрузка следующих страниц;
- поиск по названию на сервере;
- карточка публикации: авторы, дата, издатель, тип, ключевые слова, аннотация;
- похожие публикации на карточке: те, у которых есть общие ключевые слова;
- загрузка, ошибка с «Повторить» и пустой список;
- светлая и тёмная тема;
- интерфейс на русском и английском.

## Данные

Источник — OpenAIRE Graph API v1, сущность
[research product](https://graph.openaire.eu/docs/data-model/entities/research-product/).
Запросы идут в `data/network/PublicationApi`, ответ из DTO в доменную модель переводит маппер.

Базовый адрес задаётся в `gradle.properties` (`BASE_URL`), из него BuildKonfig генерирует
константу — адрес не зашит в код.

| Поле модели | Поле в API | Что это |
|---|---|---|
| `id` | `id` | идентификатор записи |
| `mainTitle` | `mainTitle` | название |
| `authors` | `authors[].fullName` | авторы |
| `publicationDate` | `publicationDate` | дата публикации |
| `description` | `descriptions[]` | аннотация |
| `subjects` | `subjects[].subject.value` | ключевые слова |
| `type` | `type` | тип: `publication`, `dataset`, `software` |
| `publisher` | `publisher` | издатель |

Содержимое каталога на английском и не переводится. Переводится только интерфейс.

## Запуск

```bash
./gradlew :desktopApp:run                        # desktop, основной таргет
./gradlew :desktopApp:run -Plocale=en            # desktop с английским интерфейсом
./gradlew :webApp:wasmJsBrowserDevelopmentRun    # web
./gradlew :androidApp:assembleDebug              # android, нужен SDK
python tools/check-strings.py                    # сверка ключей ru/en
```

## Как устроен проект

Весь общий код лежит в `shared/src/commonMain/kotlin/ru/omgtu/ryabchenko/publications`.

| Пакет | Что там |
|---|---|
| `domain` | модель `Publication`, интерфейс репозитория, ошибки загрузки, пагинация |
| `data/network` | HTTP-клиент, `PublicationApi`, DTO, маппер DTO → модель |
| `data` | репозиторий поверх сети |
| `ui/model` | UI-модели карточки и детали |
| `ui/list` | экран списка, его состояние, намерения, ViewModel и фабрика |
| `ui/detail` | экран публикации, его состояние, намерения, ViewModel и фабрика |
| `ui/components` | общие виджеты (ошибка загрузки) |
| `ui/navigation` | маршруты, бэкстек и NavDisplay (Navigation 3) |
| `ui/theme` | тема на токенах MaterialTheme |
| `composeResources/values`, `values-en` | подписи интерфейса, две локали |

Между сетью и экраном три модели: DTO (форма ответа), домен (логика) и UI (что рисует экран).
Экран получает состояние и колбэк, а не ViewModel. Данные ViewModel берёт у репозитория,
переходы делает через `Navigator`. Движок HTTP-клиента задан зависимостью таргета:
CIO на JVM и Android, Darwin на iOS, Js на web.
