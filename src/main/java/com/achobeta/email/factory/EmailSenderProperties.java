package com.achobeta.email.factory;

import lombok.Data;

import java.util.Properties;

@Data
public class EmailSenderProperties {

    private String username;

    private String password;

    private String host;

    private Integer port;

    private String protocol;

    private String defaultEncoding;

    private Properties properties;

}