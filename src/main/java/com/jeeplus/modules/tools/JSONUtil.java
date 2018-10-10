package com.jeeplus.modules.tools;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类文件: JSONUtil
 * <p>
 * <p>
 * 类描述：JSON工具类
 * <p>
 * 作 者： toteny
 * <p>
 * 日 期： 2015-11-29
 * <p>
 * 时 间： 上午9:53:04
 * <p>
 */
public class JSONUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {

		// 允许单引号
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 字段和值都加引号
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
				true);
		// 数字也加引号
		objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS,
				true);
		objectMapper.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS,
				true);
	}

	/**
	 * 将ＪＳＯＮ转换成对象
	 * 
	 * @param paramString
	 *            ＪＳＯＮ字符串
	 * @param paramClass
	 *            　要转换的实体对象实例
	 * @return 返回转换后的实例
	 */
	public static <T> T readAsObject(String paramString, Class<T> paramClass) {
		try {
			return objectMapper.readValue(paramString, paramClass);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	/**
	 * 将对象实例转换成ＪＳＯＮ字符串
	 * 
	 * @param paramObject
	 *            对象实例
	 * @return 转换后的ＪＳＯＮ字符串
	 */
	public static String writeObject2Json(Object paramObject) {
		try {
			return objectMapper.writeValueAsString(paramObject);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将ＪＳＯＮ转换成对象
	 * 
	 * @param paramString
	 *            ＪＳＯＮ字符串
	 * @param paramTypeReference
	 * @return 返回转换后的实例
	 */
	public static <T> T readJson2Object(String paramString,
			TypeReference<T> paramTypeReference) {
		try {
			return objectMapper.readValue(paramString, paramTypeReference);
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
	 * 将集合格式的ＪＳＯＮ转换成对象集合
	 * 
	 * @param parameterStore
	 *            ＪＳＯＮ字符串
	 * @param clazz
	 *            　　　　　 类对象实例
	 * @return 返回转换后的对象集合
	 */
	public static <T> List<T> read2ObjectList(String parameterStore,
                                              Class<?> clazz) {
		try {
			TypeFactory typeFactory = TypeFactory.defaultInstance();
			List<T> entityList = objectMapper
					.readValue(parameterStore, typeFactory
							.constructCollectionType(ArrayList.class, clazz));
			return entityList;
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
	 * 将对象转换为Map对象
	 * 
	 * @param paramObject
	 *            要转换的对象
	 * @return 转换后的map对象实例
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readObject2Map(Object paramObject) {
		try {
			return objectMapper.readValue(writeObject2Json(paramObject),
					HashMap.class);
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
	 * 将对象转换为JSON流
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param object
	 *            对象
	 */
	public static void toJson(HttpServletResponse response, Object value) {
		Assert.notNull(response);
		Assert.notNull(value);
		try {
			objectMapper.writeValue(response.getWriter(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class User {
		int id;
		String username;
		String address;
		String phone;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

	}
}
