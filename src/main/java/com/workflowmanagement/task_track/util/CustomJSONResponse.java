package com.workflowmanagement.task_track.util;

import lombok.Getter;


@Getter
public class CustomJSONResponse<T> {
    private String message;
    private String error;
    private CustomStatus status;
    private Long timestamp;
    private T data;

    public CustomJSONResponse(String message, String error, CustomStatus status, T data) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }





//    public  Map<String, String> customJSONResponse(CustomStatus status, String message, String error) {
//        HashMap<String, String> resBody = new HashMap<>();
//        resBody.put( "message", message);
//        resBody.put("error", error);
//
//
//        switch (status) {
//            case ERR:
//                resBody.put("status", "err");
//                break;
//            case OK:
//                resBody.put("status", "ok");
//                break;
//
//        }
//
//        return  resBody;
//
//    }

//    public static ResponseEntity<T> customJSONResponseWithData(CustomStatus status, String message, String error, Object data) {
//        HashMap<String, Object> resBody = new HashMap<>();
//        resBody.put( "message", message);
//        resBody.put("error", error);
//        resBody.put("data", data);
//
//        switch (status) {
//            case ERR:
//                resBody.put("status", "err");
//                break;
//            case OK:
//                resBody.put("status", "ok");
//                break;
//
//        }
//
//        return  resBody;
//
//    }
}
