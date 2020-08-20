package com.github.tbosoft.kernal.qiniu;

import com.github.tbosoft.config.QiniuConfig;
import com.github.tbosoft.contracts.Uploader;
import com.github.tbosoft.kernal.CreateResult;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Md5;


import java.io.File;
import java.io.IOException;


public class QiniuUploader implements Uploader {


    private QiniuConfig config;

    UploadManager uploadManager = new UploadManager(new Configuration());

    private Auth auth;

   public QiniuUploader(QiniuConfig config){
       this.config = config;
       this.auth = Auth.create(config.getAccess(), config.getSecret());
   }

    public Auth getAuth() {
        return auth;
    }

    public UploadManager getUploadManager() {
        return uploadManager;
    }

    @Override
    public CreateResult upload(File file) throws IOException {
        String filepath = config.getPrefix() + "/" + Md5.md5(file);
        Response response = uploadManager.put(file, filepath, auth.uploadToken(config.getBucket()));

        if (response.isServerError() || !response.isOK()) {
            return CreateResult.fail(response.error);
        }

        return new CreateResult(config.getDomain() + filepath,file);
    }
}
