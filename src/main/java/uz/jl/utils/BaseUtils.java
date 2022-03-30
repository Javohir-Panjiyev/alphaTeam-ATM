package uz.jl.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Elmurodov Javohir, Mon 5:23 PM. 12/6/2021
 */
public class BaseUtils {
    public static String genID() {
        return System.nanoTime() + RandomStringUtils.random(26, true, true);
    }
}
