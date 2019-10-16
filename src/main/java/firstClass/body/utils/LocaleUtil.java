package firstClass.body.utils;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Author: wkit
 * @Date: 2019-10-15 22:38
 */
abstract public class LocaleUtil {
    public static Locale getCurrentLocale(){
        return LocaleContextHolder.getLocale();
    }

    public static String getCurrentLocaleString() {
        Locale locale = getCurrentLocale();
        String localeString = locale.toString();
        if (localeString.toLowerCase().startsWith("zh")) {
            return localeString.replace("-", "_");
        } else {
            return localeString.substring(0, 2);
        }
    }
}
