package com.jcpt.jzg.padsystem.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jzg on 2015/12/22.
 */
public class BaseObject implements Parcelable{
     private int status;
     private String msg;

     public static final Parcelable.Creator<BaseObject> CREATOR = new Parcelable.Creator<BaseObject>() {
          @Override
          public BaseObject createFromParcel(Parcel in) {
               return new BaseObject(in);
          }

          @Override
          public BaseObject[] newArray(int size) {
               return new BaseObject[size];
          }
     };

     public String getMsg() {
          return msg;
     }

     public void setMsg(String msg) {
          this.msg = msg;
     }

     public int getStatus() {
          return status;
     }

     public void setStatus(int status) {
          this.status = status;
     }


     @Override
     public String toString() {
          return "BaseObject{" +
                  "msg='" + msg + '\'' +
                  ", status=" + status +
                  '}';
     }


     public BaseObject() {
     }

     @Override
     public int describeContents() {
          return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
          dest.writeInt(this.status);
          dest.writeString(this.msg);
     }

     protected BaseObject(Parcel in) {
          this.status = in.readInt();
          this.msg = in.readString();
     }
}
