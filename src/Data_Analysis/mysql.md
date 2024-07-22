为了查看MySQL数据库和表的操作，您可以使用以下SQL命令：

### 查看所有数据库

要查看MySQL服务器上的所有数据库，可以使用以下命令：

```mysql
SHOW DATABASES;
```

### 查看某个数据库中的所有表

首先，您需要选择要查看的数据库，然后使用以下命令查看该数据库中的所有表：

```mysql
USE database_name;
SHOW TABLES;
```

### 查看某个表的结构

要查看某个表的结构，可以使用以下命令：

```mysql
DESCRIBE table_name;
```

### 查看某个表中的数据

要查看某个表中的数据，可以使用以下命令：

```mysql
SELECT * FROM table_name;
```

