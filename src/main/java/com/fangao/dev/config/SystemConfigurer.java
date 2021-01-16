package com.fangao.dev.config;

import com.fangao.dev.common.FangaoConfigurer;
import com.fangao.dev.common.LoginHandlerInterceptor;
import com.baomidou.kisso.web.interceptor.SSOSpringInterceptor;
import com.fangao.dev.sys.entity.Resource;
import com.fangao.dev.sys.service.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;


/**
 * <p>
 * WEB 初始化相关配置
 * </p>
 *
 * @author jobob
 * @since 2018-03-31
 */
@ControllerAdvice
@Configuration
public class SystemConfigurer extends FangaoConfigurer {

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private IResourceService resourceService;

    /**
     * 将自定义拦截器作为Bean写入配置
     * @return
     */
    @Bean
    public FunctionHandlerInterceptor functionHandlerInterceptor() {
        return new FunctionHandlerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 正式演示环境开启方法阻断拦截器
        if (systemProperties.isOnline()) {
            registry.addInterceptor(new BlockMethodHandlerInterceptor()).excludePathPatterns("/sso/**", "/sys/user/unlock");
        }
        // SSO 授权拦截器
        SSOSpringInterceptor ssoInterceptor = new SSOSpringInterceptor();
        ssoInterceptor.setHandlerInterceptor(new LoginHandlerInterceptor());
        registry.addInterceptor(ssoInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/sso/**", "/alipay/**", "/resource/nav_menu");

        // 功能权限拦截器
        // 查询所有功能权限
        List<Resource> functionList = resourceService.listFunctions();
        if(functionList != null && functionList.size()>0){
            // 当存在功能权限时，注入功能权限拦截器
            InterceptorRegistration interceptorRegistration = registry
                    .addInterceptor(functionHandlerInterceptor());
            // 将功能的请求地址加入拦截器
            for(Resource fuc : functionList)
                interceptorRegistration.addPathPatterns(
                        // 替换“{xxx}”为“*”
                        fuc.getUri().replaceAll("\\{\\w+\\}","*")
                );
        }

        // 注入跟踪访问日志
//        registry.addInterceptor(new LogInterceptor());
    }

    /**
     * <p>
     * 跨域同源策略配置
     * </p>
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:8088");
        config.addAllowedOrigin("http://106.14.63.23:8088");
        config.addAllowedOrigin("http://crab.baomidou.com");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
