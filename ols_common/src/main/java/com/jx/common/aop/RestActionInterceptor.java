package com.jx.common.aop;

import com.jx.util.RestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@Order(100)
public class RestActionInterceptor {
    private Log logger = LogFactory.getLog(this.getClass());

    public RestActionInterceptor() {
    }

    @Pointcut("execution(* com.jx..*.*(..)) && (@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping)) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping) || @annotation(org.springframework.web.bind.annotation.PatchMapping)")
    private void anyMethod() {
    }

    @Around("anyMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
        String action = joinPoint.getSignature().toLongString();
        Object[] args = joinPoint.getArgs();
        StringBuilder argsBuilder = new StringBuilder();
        Exception exception = null;
        StringBuilder logBuilder = new StringBuilder();
        //请求头信息--暂时无用，没有需要打印的头信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logBuilder.append("--servletPath").append(request.getServletPath()).append("--methodInfo").append(action);
        long startTime = System.currentTimeMillis();
        String retStr;
        if (args.length > 0) {
            if (args[0] instanceof Map) {
                retStr = this.getServletPath(request);
                Map argsMap = this.skipServletPathKeys(retStr, "abc", (Map) args[0], Arrays.asList("dataPackage"));
                argsBuilder.append(new JSONObject(argsMap));
            } else {
                argsBuilder.append(args[0]);
            }
        }

        for (int i = 0; i < args.length; i++) {
            argsBuilder.append(", ").append(args[i]);
        }

        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Exception var20) {
            exception = var20;
            result = RestUtil.fail(RestUtil.ERROR_INTERNAL_CODE, RestUtil.ERROR_INTERNAL_MSG);
        }

        retStr = JSONObject.valueToString(result);
        logBuilder.append(" args(").append(argsBuilder).append(")").append(" return").append(retStr).append(")");
        if (exception != null) {
            logBuilder.append(" EXCEPTION:").append(ThrowableUtils.getString(exception)).append("\n");
        }

        Long timeSpan = System.currentTimeMillis() - startTime;
        logBuilder.append(" TOOK:").append(timeSpan).append(" ms.");
        if (exception == null && timeSpan <= 1000L) {
            this.logger.info(logBuilder.toString());
        } else {
            this.logger.warn(logBuilder.toString());
        }

        return result;
    }

    public String getServletPath(HttpServletRequest request) {
        String[] servletPaths = request.getServletPath().split("/");
        return servletPaths[servletPaths.length - 1];
    }

    /**
     * 替换特殊字段为**
     *
     * @param curServletPath
     * @param skipServletPath
     * @param map
     * @param skipKeys
     * @return
     */
    public Map skipServletPathKeys(String curServletPath, String skipServletPath, Map map, List<String> skipKeys) {
        if (!StringUtils.isEmpty(skipServletPath) && curServletPath.equals(skipServletPath)) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.putAll(map);
            //替换特殊字段，目前用不到
            resultMap.forEach((key, value) -> {
                if (skipKeys.contains(key)) {
                    resultMap.put(key, "**");
                }
            });
            return resultMap;
        } else {
            return map;
        }
    }
}
