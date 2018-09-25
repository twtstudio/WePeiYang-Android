package com.tjuwhy.yellowpages2.utils;

public class FirstLetterUtil {
    private static int END = 63486; // 按照声母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字。
    // // i, u, v都不做声母, 自定规则跟随前面的字母
    private static char[] charTable = {'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈',
            '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌',
            '塌', '挖', '昔', '压', '匝',};
    // 二十六个字母区间对应二十七个端点
    // GB2312码汉字区间十进制表示
    private static int[] table = new int[27];
    // 对应首字母区间表
    private static char[] initialTable = {'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            't', 't', 'w', 'x', 'y', 'z',};

    // 初始化
    static {
        for (int i = 0; i < 26; i++) {
            table[i] = gbValue(charTable[i]);
            // 得到GB2312码的首字母区间端点表，十进制。
        }
        table[26] = END;
        // 区间表结尾
    }

    public static String getFirstLetter(String sourceStr) {
        StringBuilder result = new StringBuilder();
        String str = sourceStr.toLowerCase();
        int StrLength = str.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                result.append(Char2Initial(str.charAt(i)));
            }
        } catch (Exception e) {
            result = new StringBuilder();
        }
        return result.toString();
    }

    private static char Char2Initial(char ch) {
        // 对英文字母的处理：小写字母转换为大写，大写的直接返回
        if (ch >= 'a' && ch <= 'z') {
            return ch;
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        // 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
        // 若不是，则直接返回。
        // 若是，则在码表内的进行判断。
        int gb = gbValue(ch);// 汉字转换首字母
        int BEGIN = 45217;
        if ((gb < BEGIN) || (gb > END))// 在码表区间之前，直接返回
        {
            return ch;
        }
        int i;
        for (i = 0; i < 26; i++) {
            // 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
            if ((gb >= table[i]) && (gb < table[i + 1])) {
                break;
            }
        }
        if (gb == END) {
            // 补上GB2312区间最右端
            i = 25;
        }
        return initialTable[i];
        // 在码表区间中，返回首字母
    }

    private static int gbValue(char ch) {
        // 将一个汉字（GB2312）转换为十进制表示。
        String str = "";
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }

}
