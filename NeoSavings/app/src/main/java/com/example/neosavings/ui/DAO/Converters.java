package com.example.neosavings.ui.DAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.Date;


public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static byte[] fromBitmapToOutputStream(Bitmap value){
        if(value==null){
            return null;
        }else{
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            value.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            return outputStream.toByteArray();
        }
    }

    @TypeConverter
    public static Bitmap fromByteToBitmap(byte[] value){
        try{
            if(value==null){
                return null;
            }else{
                Bitmap bitmap= BitmapFactory.decodeByteArray(value,0,value.length);
                return bitmap;
            }
        }catch (Exception e){
            return null;
        }
    }

}
