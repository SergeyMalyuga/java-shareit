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
}
