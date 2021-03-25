package com.andry.address.oriontek.models;


import java.io.Serializable;
import java.util.List;

/**
 * The type User.
 */
public class User implements Serializable {
    /**
     * id
     */
    private String uid;
    /**
     * name
     */
    private String name;

    /**
     * email
     */
    private String email;

    /**
     * phoneNumber
     */
    private String phoneNumber;

    /**
     * status
     */
    private UserStatus status;

    /**
     * rol
     */
    private Rol rol;

    private List<String> address;

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param uid         the uid
     * @param name        the name
     * @param email       the email
     * @param phoneNumber the phone number
     * @param status      the status
     * @param rol         the rol
     */
    public User(String uid, String name, String email, String phoneNumber, UserStatus status, Rol rol, List<String> address) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.rol = rol;
        this.address = address;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Gets rol.
     *
     * @return the rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Sets rol.
     *
     * @param rol the rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public List<String> getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(List<String> address) {
        this.address = address;
    }

}
