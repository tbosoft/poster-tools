package com.github.tbosoft.enums;

public interface DrawEnum {
    enum DrawType implements DrawEnum{
        TEXT("1"),BLOCK("2"),LINE("3"),IMAGE("4");
        private String value;

        private DrawType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
