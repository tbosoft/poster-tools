package com.github.tbosoft.enums;

public interface FormatEnum {
    enum Format implements FormatEnum{
            PNG("png"), JPG("jpg");
            private String value;

            private Format(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }
        }
}
