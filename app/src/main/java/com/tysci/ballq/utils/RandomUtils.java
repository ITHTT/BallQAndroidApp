package com.tysci.ballq.utils;

/**
 * Created by LinDe on 2016/4/8.
 * Random
 */
public class RandomUtils {
    private static final char[] PARAMS_NUMBER = {
            /*'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M',*/
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
//    private static final char[] PARAMS = {
//            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
//            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M',
//            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    private RandomUtils() {
    }

    public static String getRandomNumber(final int passwordLength) {
        final int length = PARAMS_NUMBER.length;
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < passwordLength) {
            int random = (int) (Math.random() * length);
            char tmp = PARAMS_NUMBER[random];
            if (i == 0 && tmp == '0') {
                continue;
            }
            sb.append(tmp);
            i++;
        }
//        for (int i = 0; i < passwordLength; i++) {
//            final int random = (int) (Math.random() * length);
//            sb.append(PARAMS_NUMBER[random]);
//        }
        return sb.toString();
    }

    public static String getOnlyOneByTimeMillis(int length) {
        long time = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append(time);
        while (sb.toString().length() < length) {
            sb.append("0");
        }
        if (sb.toString().length() > 32) {
            return sb.toString().substring(0, 32);
        }
        return sb.toString();
    }
}
