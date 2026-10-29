# Dash RPG — Forge 1.12.2 (початковий прототип)

Це стартовий проєкт Java-моду для Minecraft 1.12.2 Forge. Він містить:
- меню навичок на клавішу **K**;
- ривок на **R**;
- перекат/швидке ухилення на **V**;
- додаткові стрибки в повітрі (натискай Space повторно);
- базові покращення: стрибки, шкода, здоров'я, броня, швидкість копання та шанс ухилення.

## Важливо
Це **початковий прототип**, а не перевірений готовий реліз. Керування рухом зроблене на клієнті; для повноцінної роботи в мультиплеєрі потрібні серверні пакети та синхронізація характеристик. Перевіряй спершу в окремому тестовому світі та зроби резервну копію світу.

## Як зібрати JAR
Потрібен комп'ютер із JDK 8 (64-bit) та інтернетом для завантаження залежностей. PojavLauncher призначений для запуску гри, а не для зручної компіляції модів на телефоні.

1. Завантаж Forge **1.12.2 - 14.23.5.2859 MDK** з офіційної сторінки: https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html
2. Розпакуй MDK в окрему папку.
3. Скопіюй із цього архіву папку `src` і файли `build.gradle`, `gradle.properties` у папку MDK, погодившись на заміну лише якщо створюєш чистий проєкт.
4. Відкрий термінал/командний рядок у цій папці та виконай:
   - Windows: `gradlew.bat build`
   - Linux/macOS: `./gradlew build`
5. Якщо збірка успішна, JAR буде в `build/libs/`. Скопіюй його в `.minecraft/mods/` профілю Forge 1.12.2.
6. У PojavLauncher вибери Minecraft 1.12.2 із Forge. Якщо гра вилітає, прибери мод і надішли `latest.log` або crash-report.

Офіційна документація Forge: https://docs.minecraftforge.net/en/1.12.x/gettingstarted/

## Структура
- `src/main/java/ua/dashrpg/DashRPG.java`
- `src/main/java/ua/dashrpg/ClientEvents.java`
- `src/main/java/ua/dashrpg/SkillGui.java`
- `src/main/resources/mcmod.info`
- `src/main/resources/assets/dashrpg/lang/en_us.lang`

## Клавіші
- **K** — меню навичок
- **R** — ривок
- **V** — перекат
- **Space** — додатковий стрибок у повітрі

Клавіші можна змінити в Options → Controls.
