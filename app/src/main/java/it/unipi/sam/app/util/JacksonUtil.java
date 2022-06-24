package it.unipi.sam.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.HashMap;

public class JacksonUtil {
    /**
     * Get a string representation of obj ignoring some field
     * @param obj
     * @param filterName the name of the filter
     * @param ignoredFields the fields of obj to ignore
     * @return the string representation of obj
     * @throws JsonProcessingException
     */
    public static String getStringFromObjectIgnoreFields(Object obj, String filterName, String... ignoredFields)
            throws JsonProcessingException{
        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
                .serializeAllExcept(ignoredFields);
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter(filterName, theFilter);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writer(filters).writeValueAsString(obj);
    }

    public static String getStringFromObject(Object obj)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    public static Object getObjectFromString(String string, Class<?> clas)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(string, clas);
    }

    public static Object getObjectMapObjectFromString(String string, TypeReference<HashMap<String, Object>> clas)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(string, clas);
    }

    /*public static Object getTeamMapObjectFromString(String string, TypeReference<HashMap<String, Team>> clas)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(string, clas);
    }

    public static Object getPeopleMapObjectFromString(String string, TypeReference<HashMap<String, Person>> clas)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(string, clas);
    }*/
}