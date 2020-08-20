package com.github.tbosoft.config;

import lombok.Data;


@Data
public class QiniuConfig {
    private String domain;
    private String bucket;
    private String access;
    private String secret;
    private String prefix;
}
