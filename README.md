# Публикации (OpenAIRE) — каркас на Compose Multiplatform

ЛР1, веха В1 «Каркас». Курс кроссплатформенной разработки, группа ПИН-252т.

## Предметная область

Каталог научных публикаций из [OpenAIRE Graph](https://graph.openaire.eu/). OpenAIRE собирает
статьи, датасеты и программы из репозиториев и журналов в один открытый граф. В основном в каталоге
статьи, но встречаются и датасеты с программами, это видно по полю `type`.

Что есть в приложении:

- список публикаций: название, авторы, дата;
- поиск по названию;
- карточка публикации: авторы, дата, издатель, тип, ключевые слова, аннотация;
- похожие публикации на карточке: те, у которых есть общие ключевые слова;
- светлая и тёмная тема;
- интерфейс на русском и английском.

## Данные

Источник — OpenAIRE Graph API, сущность
[research product](https://graph.openaire.eu/docs/data-model/entities/research-product/).

Сети в этой вехе нет. 20 записей написаны вручную в `data/MockPublications.kt`, поля названы так же,
как в ответе API. В В2 моки заменит сеть, модель и экраны при этом не меняются.

| Поле модели | Поле в API | Что это |
|---|---|---|
| `id` | `id` | идентификатор записи |
| `mainTitle` | `mainTitle` | название |
| `authors` | `authors[].fullName` | авторы |
| `publicationDate` | `publicationDate` | дата публикации |
| `description` | `descriptions[]` | аннотация |
| `subjects` | `subjects[].subject.value` | ключевые слова |
| `type` | `type` | тип: `publication`, `data`, `software` |
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
| `domain` | модель `Publication` и интерфейс репозитория |
| `data` | моки и репозиторий, который их отдаёт |
| `ui/list` | экран списка, его состояние, намерения, ViewModel и фабрика |
| `ui/detail` | экран публикации, его состояние, намерения, ViewModel и фабрика |
| `ui/navigation` | маршруты, бэкстек и NavDisplay (Navigation 3) |
| `ui/theme` | тема на токенах MaterialTheme |
| `composeResources/values`, `values-en` | подписи интерфейса, две локали |

Экран получает состояние и колбэк, а не ViewModel. Данные ViewModel берёт у репозитория,
переходы делает через `Navigator`.
