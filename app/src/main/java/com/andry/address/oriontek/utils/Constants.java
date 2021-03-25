package com.andry.address.oriontek.utils;

public interface Constants {
    /**
     * Pattern for email
     */
    String emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    /**
     * nameFileTheSharedPreferences
     */
    String nameFileTheSharedPreferences = "UserInfo";

    /**
     * uid
     */
    String uid = "uid";

    /**
     * Fields for route Users in Firestore
     */
    String routeUsers = "users"; //Route
    String UID = "uid";//field
    String EMAIL = "email";//field
    String NAME = "name";//filed
    String PHONE_NUMBER = "phoneNumber";//field
    String ROL = "rol";//field
    String STATUS = "status";
    String ADDRESS = "address";
}
