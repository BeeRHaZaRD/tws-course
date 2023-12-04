# Лабораторные работы по курсу "Технологии веб-сервисов"

### lab1: Поиск с помощью SOAP-сервиса

В данной работе требуется создать таблицу в базе данных, содержащую не менее пяти полей, а также реализовать возможность поиска по любым комбинациям полей с помощью SOAP-сервиса. Данные для поиска должны передаваться в метод сервиса в качестве аргументов. 

Веб-сервис необходимо реализовать в виде standalone-приложения и J2EEприложения. При реализации в виде J2EE-приложения следует на стороне сервера приложений настроить источник данных и осуществлять его инъекцию в коде сервиса. 

Для демонстрации работы разработанных сервисов следует также разработать и клиентское консольное приложение.

### lab2: Реализация CRUD с помощью SOAP-сервиса

В данной работе в веб-сервис, разработанный в первой работе, необходимо добавить методы для создания, изменения и удаления записей из таблицы.

Метод создания должен принимать значения полей новой записи, метод изменения – идентификатор изменяемой записи, а также новые значения полей, а метод удаления – только идентификатор удаляемой записи.

Метод создания должен возвращать идентификатор новой записи, а методы обновления или удаления – статус операции. В данной работе следует вносить изменения только в standalone-реализацию сервиса.

В соответствии с изменениями сервиса необходимо обновить и клиентское приложение.