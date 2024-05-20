package io.paletaweb.client;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import io.paleta.logging.Logger;
import okhttp3.Headers;

/**
 *<p>HTTP header parser class.</p>
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
public class HeaderParser {

 private static final Logger logger = Logger.getLogger(HeaderParser.class.getName());

  /* private constructor */
  private HeaderParser() {}

  /**
   * Sets destination object from Headers object.
   */
  public static void set(Headers headers, Object destination) {

    Field[] publicFields;
    Field[] privateFields;
    Field[] fields;

    Class<?> cls = destination.getClass();
    publicFields = cls.getFields();
    privateFields = cls.getDeclaredFields();
    fields = new Field[publicFields.length + privateFields.length];
    System.arraycopy(publicFields, 0, fields, 0, publicFields.length);
    System.arraycopy(privateFields, 0, fields, publicFields.length, privateFields.length);

    for (Field field : fields) {
      Annotation annotation = field.getAnnotation(Header.class);
      if (annotation == null) {
        continue;
      }

      Header httpHeader = (Header) annotation;
      String value = httpHeader.value();
      String setter = httpHeader.setter();

      if (setter.isEmpty()) {
        // assume setter name as 'setFieldName'
        String name = field.getName();
        setter = "set" + name.substring(0, 1).toUpperCase(Locale.US) + name.substring(1);
      }

      try {
        Method setterMethod = cls.getMethod(setter, new Class[]{String.class});
        String valueString = headers.get(value);
        if (valueString != null) {
          setterMethod.invoke(destination, valueString);
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
    	  logger.error( "setter: " + setter + " | " + "annotation: " + value + " | " + "value: " + headers.get(value));
    	  logger.error(e);
      }
    }
  }
}
