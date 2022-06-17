package it.unipi.sam.app.util;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Maps are not Parcelable and this is an issue in Android when they need to be passed to activities and services via Intents.
 * The corresponding Map-like object in Android is the Bundle.
 * Bundle is a more generic container, it doesn't enforce types via generics and
 * isn't supported natively by JSON deserializers such as Gson.
 */
public class CollectionUtils {
    public static Bundle toBundle(Map<String, ? extends Parcelable> input) {
        Bundle output = new Bundle();
        for(String key : input.keySet()) {
            output.putParcelable(key, input.get(key));
        }
        return output;
    }

    public static <T extends Parcelable> Map<String, T> fromBundle(Bundle input, Class<T> c) {
        Map<String, T> output = new HashMap<String, T>();
        for(String key : input.keySet()) {
            output.put(key, c.cast(input.getParcelable(key)));
        }
        return output;
    }
}
