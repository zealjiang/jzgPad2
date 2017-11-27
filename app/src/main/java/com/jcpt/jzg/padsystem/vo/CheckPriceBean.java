package com.jcpt.jzg.padsystem.vo;

/**
 * Created by wujj on 2017/4/17.
 * 邮箱：wujj@jingzhengu.com
 * 作用：价格预警
 */

public class CheckPriceBean {
    private int Code;
    private Message1 Message;
    private String Price;
    private int TaskStatus;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public Message1 getMessage() {
        return Message;
    }

    public void setMessage(Message1 message) {
        Message = message;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        TaskStatus = taskStatus;
    }

    public static class Message1 {
        private String MessageContent;
        private String MessageTimes;
        private String MessagePrice;
        private String MessageAlert;

        public String getMessageContent() {
            return MessageContent;
        }

        public void setMessageContent(String messageContent) {
            MessageContent = messageContent;
        }

        public String getMessageTimes() {
            return MessageTimes;
        }

        public void setMessageTimes(String messageTimes) {
            MessageTimes = messageTimes;
        }

        public String getMessagePrice() {
            return MessagePrice;
        }

        public void setMessagePrice(String messagePrice) {
            MessagePrice = messagePrice;
        }

        public String getMessageAlert() {
            return MessageAlert;
        }

        public void setMessageAlert(String messageAlert) {
            MessageAlert = messageAlert;
        }
    }
}
