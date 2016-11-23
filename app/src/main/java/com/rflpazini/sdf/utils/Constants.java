package com.rflpazini.sdf.utils;

/**
 * Created by rflpazini on 10/28/16.
 */

public class Constants {
    public static final String WS_PORT = "http:/192.168.1.104:8087";
    public static final String AUTH_USER_API_URL = WS_PORT + "/MessagesAPI/api/users/auth/";
    public static final String MESSAGE_POST_URL = WS_PORT + "/MessagesAPI/api/messages/add";
    public static final String MESSAGE_GET_URL = WS_PORT + "/MessagesAPI/api/messages";

    /**
     * CONNECTION TYPES API
     */
    public static final String GET_REQUEST = "GET";
    public static final String POST_REQUEST = "POST";
    public static final String DELETE_REQUEST = "DELETE";
    public static final String PUT_REQUEST = "PUT";

    public static final String USER_LOCAL_INFO = "user_login_info";
}
