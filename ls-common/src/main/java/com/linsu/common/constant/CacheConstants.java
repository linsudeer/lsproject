package com.linsu.common.constant;

/**
 * 缓存的key 常量
 *
 * @author linsu
 */
public class CacheConstants {

    private static final String PREFIX = "zmproj:";
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = PREFIX + "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = PREFIX + "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = PREFIX + "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = PREFIX + "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = PREFIX + "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = PREFIX + "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = PREFIX + "pwd_err_cnt:";
}
