package com.rgouzal.reactnativecardano.utils;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Iterator;

public class RNMapUtils {

    public static JsonObject toJsonObject(Object obj) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(obj);
        JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        return jsonObject;
    }

    public static WritableMap fromJsonObjectToMap(JsonObject jsonObject) {
        WritableMap writableMap = new WritableNativeMap();
        Iterator<String> keysIterator = jsonObject.keySet().iterator();
        while(keysIterator.hasNext()) {
            String key = keysIterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof JsonObject) {
                writableMap.putMap(key, fromJsonObjectToMap((JsonObject) value));
            } else if (value instanceof  JsonObject) {
                writableMap.putArray(key, fromJsonObjectToArray((JsonArray) value));
            } else if (value instanceof  Boolean) {
                writableMap.putBoolean(key, (Boolean) value);
            } else if (value instanceof  Integer) {
                writableMap.putInt(key, (Integer) value);
            } else if (value instanceof  Double) {
                writableMap.putDouble(key, (Double) value);
            } else if (value instanceof String)  {
                writableMap.putString(key, ((String) value).replace("\"", ""));
            } else {
                writableMap.putString(key, value.toString().replace("\"", ""));
            }
        }
        return writableMap;
    }

    public static WritableArray fromJsonObjectToArray(JsonArray jsonArray) {
        WritableArray writableArray = new WritableNativeArray();
        for(int i=0; i < jsonArray.size(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JsonObject) {
                writableArray.pushMap(fromJsonObjectToMap((JsonObject) value));
            } else if (value instanceof  JsonArray) {
                writableArray.pushArray(fromJsonObjectToArray((JsonArray) value));
            } else if (value instanceof  Boolean) {
                writableArray.pushBoolean((Boolean) value);
            } else if (value instanceof  Integer) {
                writableArray.pushInt((Integer) value);
            } else if (value instanceof  Double) {
                writableArray.pushDouble((Double) value);
            } else if (value instanceof String)  {
                writableArray.pushString((String) value);
            } else {
                writableArray.pushString(value.toString());
            }
        }
        return writableArray;
    }
}
