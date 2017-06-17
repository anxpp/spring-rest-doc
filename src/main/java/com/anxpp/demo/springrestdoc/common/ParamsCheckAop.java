package com.anxpp.demo.springrestdoc.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 切面用于检查参数
 * Created by yangtao on 2017/5/5.
 */
@Component
@Aspect
public class ParamsCheckAop {

    //日志工具
    private final Logger log = LoggerFactory.getLogger(getClass() + "____AOP____");

    @Resource
    private ObjectMapper objectMapper;

    //API接口调用切面配置
    @Pointcut("execution(* com.anxpp.demo.springrestdoc.api..*.*(..))")
    public void executeForAPI() {
    }

    /**
     * 环绕通知
     *
     * @param proceedingJoinPoint 切点信息
     * @return object
     * @throws Throwable ...
     */
    @Around("executeForAPI()")
    public Object aroundExecuteForAPI(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //------------开始时间------------
        long start = System.currentTimeMillis();
        //------------统计------------
        //方法名
        String method = proceedingJoinPoint.getSignature().getName();
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        //如果要获取Session信息的话，可以这样写：
        //HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        //请求参数
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, String> parameterMap = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter, request.getParameter(parameter));
        }
        //------------执行目标方法------------
        Object object = proceedingJoinPoint.proceed();
        //请求时间
        int time = (int) (System.currentTimeMillis() - start);
        //请求路径
        String path = request.getRequestURI();
        //请求log
        int size = 0;
        if (object instanceof List) {
            size = ((List) object).size();
        }
        log.info(String.format("path=%s  time=%sms  size=%d  method=%s  %s", path, time, size, method, StringUtils.trimAllWhitespace(objectMapper.writeValueAsString(parameterMap))));
        return object;
    }

}
