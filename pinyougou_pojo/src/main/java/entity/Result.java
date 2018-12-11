package entity;

import java.io.Serializable;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/4
 */
public class Result implements Serializable {
    private boolean success;

    private String message;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
