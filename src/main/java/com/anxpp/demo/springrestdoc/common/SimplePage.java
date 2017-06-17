package com.anxpp.demo.springrestdoc.common;

import org.springframework.data.domain.PageRequest;

/**
 * 分页工具
 * Created by yangtao on 2017/6/15.
 */
public class SimplePage extends PageRequest {

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_SIZE = 4;

    /**
     * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing 0 for {@code page} will return the first
     * page.
     *
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     */
    private SimplePage(int page, int size) {
        super(page, size);
    }

    public static SimplePage build(Integer page, Integer size) {
        return new SimplePage(page==null||page<0?DEFAULT_PAGE:page, size==null||size<0?DEFAULT_SIZE:size);
    }

//    static int String2Int(String src) {
//        int result = 0;
//        if (StringUtils.isEmpty(src))
//            return result;
//        int zero = '0';
//        for (char c : src.toCharArray()) {
//            if (c >= '0' && c <= '9')
//                result = result * 10 + (c - zero);
//        }
//        return result;
//    }
}
