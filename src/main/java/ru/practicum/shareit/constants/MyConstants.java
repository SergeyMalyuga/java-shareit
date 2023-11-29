package ru.practicum.shareit.constants;

public class MyConstants {
    public static final String ADD_USER_POINTCUT = "execution(public ru.practicum.shareit.user.User " +
            "ru.practicum.shareit.user.service.UserServiceImpl.addUser(ru.practicum.shareit.user.User))";
    public static final String REMOVE_USER_POINTCUT = "execution(public void " +
            "ru.practicum.shareit.user.service.UserServiceImpl.removeUserById(*))";

    public static final String GET_ALL_USERS_POINTCUT = "execution(public java.util.List<ru.practicum.shareit.user.User> " +
            "ru.practicum.shareit.user.service.UserServiceImpl.getAllUsers())";

    public static final String UPDATE_USER_POINTCUT = "execution(public ru.practicum.shareit.user.dto.UserDto " +
            "ru.practicum.shareit.user.service.UserServiceImpl.UpdateUser(..))";

    public static final String ADD_ITEM_POINTCUT = "execution(public ru.practicum.shareit.item.dto.ItemDto " +
            "ru.practicum.shareit.item.service.ItemServiceImpl.addItem(..))";

    public static final String UPDATE_ITEM_POINTCUT = "execution(public ru.practicum.shareit.item.dto.ItemDto " +
            "ru.practicum.shareit.item.service.ItemServiceImpl.updateItem(..))";

    public static final String GET_ITEM_BY_ID_POINTCUT = "execution(public ru.practicum.shareit.item.dto.ItemDto " +
            "ru.practicum.shareit.item.service.ItemServiceImpl.getItemDtoById(..))";

    public static final String GET_ALL_ITEM_POINTCUT = "execution(public java.util.List<ru.practicum.shareit.item.dto.ItemDto> " +
            "ru.practicum.shareit.item.service.ItemServiceImpl.getAllItemForOwner(..))";

    public static final String SEARCH_ITEM_POINTCUT = "execution(public java.util.List<ru.practicum.shareit.item.dto.ItemDto> " +
            "ru.practicum.shareit.item.service.ItemServiceImpl.searchItem(..))";

    public static final String SHEMA_SQL = "DROP TABLE IF EXISTS comments;\n" +
            "DROP TABLE IF EXISTS bookings;\n" +
            "DROP TABLE IF EXISTS items;\n" +
            "DROP TABLE IF EXISTS requests;\n" +
            "DROP TABLE IF EXISTS users;\n" +
            "\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS users(\n" +
            "id INTEGER GENERATED ALWAYS AS IDENTITY,\n" +
            "name varchar(250) NOT NULL,\n" +
            "email varchar(250)NOT NULL,\n" +
            "CONSTRAINT pk_user PRIMARY KEY (id),\n" +
            "CONSTRAINT UQ_USER_EMAIL UNIQUE (email));\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS requests(\n" +
            "id INTEGER GENERATED ALWAYS AS IDENTITY,\n" +
            "requester_id INTEGER REFERENCES users(id),\n" +
            "description VARCHAR(2000),\n" +
            "created TIMESTAMP WITHOUT TIME ZONE,\n" +
            "CONSTRAINT request_id PRIMARY KEY(id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS items(\n" +
            "id INTEGER GENERATED ALWAYS AS IDENTITY,\n" +
            "name VARCHAR(250) NOT NULL,\n" +
            "description VARCHAR(500) NOT NULL,\n" +
            "available BOOLEAN NOT NULL,\n" +
            "owner_id INTEGER REFERENCES users(id),\n" +
            "request_id INTEGER REFERENCES requests(id),\n" +
            "CONSTRAINT item_id PRIMARY KEY(id));\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS bookings(\n" +
            "id INTEGER GENERATED ALWAYS AS IDENTITY,\n" +
            "start_time TIMESTAMP WITHOUT TIME ZONE,\n" +
            "end_time TIMESTAMP WITHOUT TIME ZONE,\n" +
            "item_id INTEGER REFERENCES items(id),\n" +
            "user_id INTEGER REFERENCES users(id),\n" +
            "status VARCHAR(15),\n" +
            "CONSTRAINT booking_id PRIMARY KEY(id));\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS comments(\n" +
            "id INTEGER GENERATED ALWAYS AS IDENTITY,\n" +
            "text VARCHAR(1000),\n" +
            "item_id INTEGER REFERENCES items(id) ON UPDATE CASCADE,\n" +
            "author INTEGER REFERENCES users(id) ON UPDATE CASCADE,\n" +
            "created TIMESTAMP WITHOUT TIME ZONE,\n" +
            "CONSTRAINT comments_id PRIMARY KEY(id)\n" +
            ");";

}
