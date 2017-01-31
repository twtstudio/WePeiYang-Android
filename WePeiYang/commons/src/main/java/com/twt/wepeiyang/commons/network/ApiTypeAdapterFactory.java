package com.twt.wepeiyang.commons.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.twt.wepeiyang.commons.network.ApiException;

import java.io.IOException;

/**
 * @deprecated Something wrong happened while handling GPA , ApiResponse<T> recommended
 * Created by dingzhihu on 15/5/7.
 * modified by retrox on 17/1/21.
 */
public class ApiTypeAdapterFactory implements TypeAdapterFactory {
    private String dataElementName;

    public ApiTypeAdapterFactory(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementTypeAdapter = gson.getAdapter(JsonElement.class);


        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementTypeAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("error_code")) {
                        int error_code = jsonObject.get("error_code").getAsInt();
                        String message = jsonObject.get("message").getAsString();
                        if (error_code == -1) {
                            //do nothing
                        } else {
                            throw new ApiException(error_code, message);
                        }
                    }
                    if (jsonObject.has(dataElementName)) {
                        jsonElement = jsonObject.get(dataElementName);
                    }
                }
                return delegate.fromJsonTree(jsonElement);
            }

        }.nullSafe();
    }
}
