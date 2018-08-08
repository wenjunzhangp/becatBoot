package com.baozi.util.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * [ json转换工具类 ]
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public class JsonUtil {

	/**
	 * 默认序列化mapper
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 大写驼峰序列化mapper
	 */
	private static ObjectMapper upperCamelMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

	/**
	 * 对象转Json字符串
	 * @param t
	 * @return
	 */
	public static <T> String toJsonString(T t) {
		try {
			return mapper.writeValueAsString(t);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Json字符串转Java对象
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(InputStream inputStream, Class<T> clazz) {
		try {
			return mapper.readValue(inputStream, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Json字符串转List<T>
	 * @param json
	 * @param elementClasses
	 *            List元素Class
	 * @return
	 */
	public static <T> List<T> toList(String json, Class<T> elementClasses) {
		try {
			JavaType javaType = getCollectionType(List.class, elementClasses);
			return mapper.readValue(json, javaType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取泛型的Collection Type
	 *
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * 序列化object->json
	 *
	 * @param t
	 * @param firstCharUpper
	 *            属性key的首字母是否大写, true大写,则结果为如：userName->UserName; false小写, 则结果为如：userName->userName
	 * @return
	 */
	public static <T> String toJsonString(T t, boolean firstCharUpper) {
		try {
			if (firstCharUpper) {
				return upperCamelMapper.writeValueAsString(t);
			} else {
				toJsonString(t);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反序列化json->object
	 *
	 * @param json
	 * @param clazz
	 * @param firstCharUpper
	 *            json串中属性key的首字母是否大写, true大写,则结果为如：UserName->userName; false小写, 则结果为如：userName->userName
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> clazz, boolean firstCharUpper) {
		try {
			if (firstCharUpper) {
				return upperCamelMapper.readValue(json, clazz);
			} else {
				toObject(json, clazz);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将Json串中所有属性名的首字母转为小写
	 *
	 * @param json
	 * @return
	 */
	public static String firstUpperCharToLower(String json) {
		try {
			return transToFirstChar(mapper.readTree(json), false).toString();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 将Json串中所有属性名的首字母转为大写
	 *
	 * @param json
	 * @return
	 */
	public static String firstLowerCharToUpper(String json) {
		try {
			return transToFirstChar(mapper.readTree(json), true).toString();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * json首字母大小写转换
	 *
	 * @param jsonNode
	 * @param upper
	 *            true转换结果为首字母大写，否则转为小写
	 * @return
	 */
	private static JsonNode transToFirstChar(JsonNode jsonNode, boolean upper) {
		ObjectNode result = mapper.createObjectNode();
		Iterator<String> names = jsonNode.fieldNames();
		while (names.hasNext()) {
			String key = names.next();
			JsonNode node = jsonNode.get(key);
			key = firstChar(key, upper);
			if (node.isArray()) {
				result.set(key, transToArray((ArrayNode) node, upper));
			} else if (node.isValueNode()) {
				result.set(key, node);
			} else {
				result.set(key, transToFirstChar(node, upper));
			}
		}
		return result;
	}

	private static ArrayNode transToArray(ArrayNode array, boolean upper) {
		ArrayNode result = mapper.createArrayNode();
		for (int i = 0; i < array.size(); i++) {
			JsonNode node = array.get(i);
			if (node.isObject()) {
				result.add(transToFirstChar(node, upper));
			} else if (node.isArray()) {
				result.add(transToArray((ArrayNode) node, upper));
			}
		}
		return result;
	}

	/**
	 * 首字母大小写转换
	 *
	 * @param input
	 * @param upper
	 *            true转为大写，false转为小写
	 * @return
	 */
	public static String firstChar(String input, boolean upper) {
		char c = input.charAt(0);
		char uc;
		if (upper) {
			uc = Character.toUpperCase(c);
		} else {
			uc = Character.toLowerCase(c);
		}
		if (c == uc) {
			return input;
		}
		StringBuilder sb = new StringBuilder(input);
		sb.setCharAt(0, uc);
		return sb.toString();
	}
}
