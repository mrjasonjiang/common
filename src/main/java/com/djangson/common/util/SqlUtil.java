package com.djangson.common.util;

import com.djangson.common.constant.Constants;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class SqlUtil {

    private static final char blankChar = ' ';
    private static final String escapeStr = " ESCAPE '" + Constants.SQL_ESCAPE_CHARACTER + "' ";
    private static final char[][] likeArr = new char[][]{new char[]{'L', 'l'}, new char[]{'I', 'i'}, new char[]{'K', 'k'}, new char[]{'E', 'e'}};
    private static final char[] mongoEscapeArr = new char[]{'\\', '$', '(', ')', '*', '+', '.', '[', ']', '?', '^', '{', '}', '|'};

    /**
     * 左模糊匹配，格式 %param
     * @param param
     * @return
     */
    public static String leftLike(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return StringUtil.concat(Constants.SQL_MULTI_LIKE_CHARACTER, param.trim());
    }

    /**
     * 右模糊匹配，格式 param%
     * @param param
     * @return
     */
    public static String rightLike(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return StringUtil.concat(param.trim(), Constants.SQL_MULTI_LIKE_CHARACTER);
    }

    /**
     * 全模糊匹配，格式 %param%
     * @param param
     * @return
     */
    public static String fullLike(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return StringUtil.concat(Constants.SQL_MULTI_LIKE_CHARACTER, param.trim(), Constants.SQL_MULTI_LIKE_CHARACTER);
    }

    /**
     * 左模糊匹配，格式 %param，并且特殊字符转义
     * @param param
     * @return
     */
    public static String leftLikeWithEscape(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return StringUtil.concat(Constants.SQL_MULTI_LIKE_CHARACTER, sqlParamFormat(param.trim()));
    }

    /**
     * 右模糊匹配，格式 param%，并且对特殊字符转义
     * @param param
     * @return
     */
    public static String rightLikeWithEscape(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return StringUtil.concat(sqlParamFormat(param.trim()), Constants.SQL_MULTI_LIKE_CHARACTER);
    }

    /**
     * 全模糊匹配，格式 %param%，并且对特殊字符转义
     * @param param
     * @return
     */
    public static String fullLikeWithEscape(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return StringUtil.concat(Constants.SQL_MULTI_LIKE_CHARACTER, sqlParamFormat(param.trim()), Constants.SQL_MULTI_LIKE_CHARACTER);
    }

    /**
     * 左模糊匹配，格式化 param，并且特殊字符转义
     * @param param
     * @return
     */
    public static Pattern mongoLeftLikeWithEscape(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return Pattern.compile(StringUtil.concat("^.*", mongoFormatWithEscape(param), "$"), Pattern.CASE_INSENSITIVE);
    }

    /**
     * 右模糊匹配，格式 param，并且特殊字符转义
     * @param param
     * @return
     */
    public static Pattern mongoRightLikeWithEscape(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return Pattern.compile(StringUtil.concat("^.*", mongoFormatWithEscape(param), "$"), Pattern.CASE_INSENSITIVE);
    }

    /**
     * 全模糊匹配，格式 param，并且特殊字符转义
     * @param param
     * @return
     */
    public static Pattern mongoFullLikeWithEscape(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return Pattern.compile(StringUtil.concat("^.*", mongoFormatWithEscape(param), ".*$"), Pattern.CASE_INSENSITIVE);
    }

    /**
     * 特殊字符过滤
     * @param param
     * @return
     */
    public static String sqlParamFormat(String param) {
        StringBuilder formatStr = new StringBuilder(param.length());
        for (int index = 0; index < param.length(); index++) {
            char character = param.charAt(index);
            if (isWildcardChar(character)) {
                formatStr.append(Constants.SQL_ESCAPE_CHARACTER);
            }
            formatStr.append(character);
        }
        return formatStr.toString();
    }

    /**
     * SQL语句格式化
     * @param sqlStr
     * @return
     */
    public static String sqlFormatWithEscape(String sqlStr) {

        // 预估新生成的SQL语句长度为当前SQL语句长度
        StringBuilder sql = new StringBuilder(sqlStr.length());

        // 忽略字符记录标识，若前一个字符为空，后续相同的空字符将忽略
        boolean ignoreFlag = isIgnoreChar(sqlStr.charAt(0));

        // 初始化匹配字符串的长度
        int matchPointer = likeArr.length;

        // 定义匹配指针，用于记录当前匹配字符下标
        int pointer = 0;

        // 开始循环遍历原SQL语句
        for (int index = 0; index < sqlStr.length(); index++) {

            // 获取当前字符
            char character = sqlStr.charAt(index);

            // 判断本次是否是忽略字符
            boolean isIgnore = isIgnoreChar(character);

            // 若本次字符是忽略字符，则默认以空格代替
            character = isIgnore ? blankChar : character;

            // 若上一字符不是忽略字符或本次不是忽略字符，则拼接该字符
            if (!ignoreFlag || !isIgnore) {
                sql.append(character);
            }

            // 记录本次字符标识
            ignoreFlag = isIgnore;

            // 若还未完全匹配，则继续匹配
            if (pointer != matchPointer) {
                pointer = character == likeArr[pointer][0] || character == likeArr[pointer][1] ? pointer + 1 : 0;
                continue;
            }

            // 若已匹配完毕，但当前字符既不是忽略字符亦不是通配符，则本次匹配失效
            if (!ignoreFlag && !isPlaceholder(character)) {
                pointer = 0;
                continue;
            }

            // 满足占位符条件
            if (isPlaceholder(character)) {

                // 拼接转义语句
                sql.append(escapeStr);

                // 清除本次匹配指针
                pointer = 0;
                ignoreFlag = true;
            }
        }
        return sql.toString();
    }

    /**
     * 对Mongo模糊查询条件参数做转义格式化
     * @param param
     * @return
     */
    public static String mongoFormatWithEscape(String param) {
        StringBuilder formatStr = new StringBuilder(param.length());
        for (int index = 0, length = param.length(); index < length; index++) {
            char character = param.charAt(index);
            if (isMongoEscapeChar(character)) {
                formatStr.append("\\");
            }
            formatStr.append(character);
        }
        return formatStr.toString();
    }

    /**
     * 判断字符是否是SQL语句占位符
     * @param character
     * @return
     */
    private static boolean isPlaceholder(char character) {
        return character == '?';
    }

    /**
     * 判断字符是否是SQL语句通配符
     * @param character
     * @return
     */
    private static boolean isWildcardChar(char character) {
        return character == '%' || character == '_' || character == '/';
    }

    /**
     * 判断字符是否是Mongo需要转义的字符
     * @param character
     * @return
     */
    private static boolean isMongoEscapeChar(char character) {
        for (char escapeChar : mongoEscapeArr) {
            if (character == escapeChar) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符是否是SQL语句忽略字符
     * @param character
     * @return
     */
    private static boolean isIgnoreChar(char character) {
        return character == ' ' || character == '\n' || character == '\t' || character == '\r';
    }
}
