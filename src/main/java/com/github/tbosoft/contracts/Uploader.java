package com.github.tbosoft.contracts;

import com.github.tbosoft.kernal.CreateResult;

import java.io.File;
import java.io.IOException;

public interface Uploader {
    public CreateResult upload(File file) throws IOException;
}
