/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpt;

import java.io.Serializable;

/**
 *
 * @author datht2@fpt.edu.vn
 */
public class Config implements Serializable{
    public String user;
    public String pass;   

    public Config(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
    
}
