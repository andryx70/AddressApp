package com.andry.address.oriontek.models;

import java.io.Serializable;

/**
 * The type User dto.
 */
public class UserDto extends User implements Serializable {

    private String keyFirestore;

    /**
     * Instantiates a new User.
     */
    public UserDto() {
    }

    /**
     * Instantiates a new User.
     *
     * @param keyFirestore the key firestore
     */
    public UserDto(String keyFirestore) {
        this.keyFirestore = keyFirestore;
    }



    /**
     * Gets key firestore.
     *
     * @return the key firestore
     */
    public String getKeyFirestore() {
        return keyFirestore;
    }

    /**
     * Sets key firestore.
     *
     * @param keyFirestore the key firestore
     */
    public void setKeyFirestore(String keyFirestore) {
        this.keyFirestore = keyFirestore;
    }
}
