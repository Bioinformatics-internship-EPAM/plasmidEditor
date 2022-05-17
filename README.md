# Plasmid Editor

Веб-приложение для создания, редактирования, аннотирования и визуализации структур плазмид.


Перед запуском приложения необходимо инициализировать базу данных. Есть 2 способа:
1) вручную. Для этого нужно, находясь в корневой папке проекта, прописать следующие команды


docker build -t plasmid_postgres_database:1.0 .


docker run -d --name plasmid_postgres_database -p 5432:5432 plasmid_postgres_database:1.0


2) через gradle. В файле gradle.build прописан запуск контейнеров (docker и dockerRun)

## Миграция БД при помощи goose

Для мигарции схемы базы данных используется goose. Необходимые действия для миграции:

* Установить goose любым удобным способом: https://github.com/pressly/goose#install
* Находясь в папке /src/main/resources/db/migration выполнить команду goose up

При необходимости создать новую мигарцию, необходимо воспользоваться командой goose create https://github.com/pressly/goose#create

## Участники
* Брусницына Анна, гр. 3540904/10202
* Варламов Дмитрий, гр. 3540904/10202
* Гераськин Евгений, гр. 3540904/10202
* Ткачук Андрей, гр. 3540904/10202
* Шалгуева София, гр. 3540904/10202
* Шаповалова Ирина, гр. 3540904/10202