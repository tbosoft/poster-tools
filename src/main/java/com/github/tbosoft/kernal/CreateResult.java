package com.github.tbosoft.kernal;

import com.github.tbosoft.contracts.Result;
import lombok.Data;

import java.io.File;

@Data
public class CreateResult extends JsonAble implements Result {
    private  String url;
    private String msg;
    private File poster;

    public CreateResult(String url) {
        this.url = url;
        this.msg = Result.SUCCESSFUL;
    }


    public CreateResult(String url, String msg) {
        this.url = url;
        this.msg = msg;
    }
    public CreateResult(File file){
        this.poster = file;
        this.msg = Result.SUCCESSFUL;
    }
    public CreateResult(String url,File file){
        this.poster = file;
        this.url = url;
        this.msg = Result.SUCCESSFUL;
    }

    public static CreateResult fail(String msg) {
        return new CreateResult(null, msg);
    }

    @Override
    public boolean isSuccessful() {
        return msg.equals(Result.SUCCESSFUL);
    }
}
