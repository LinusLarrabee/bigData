## CORS

[【精选】别用 Filter 了，试试 Spring 自带的方式处理 CORS 跨域问题_corsfilter禁用cors-CSDN博客](https://blog.csdn.net/zzuhkp/article/details/120631687)



### @CrossOrigin

加到单个controller或公共父类

### 二、处理跨域请求的Configuration

增加一个配置类，CrossOriginConfig.java。继承WebMvcConfigurerAdapter或者实现WebMvcConfigurer接口，其他都不用管，项目启动时，会自动读取配置。

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * AJAX请求跨域
 * @author Mr.W
 * @time 2018-08-13
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {
    static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods(ORIGINS).maxAge(3600);
    }
```

### 三、采用过滤器（filter）的方式

同方法二加配置类，增加一个CORSFilter 类，并实现Filter接口即可，其他都不用管，接口调用时，会过滤跨域的拦截。

```java
 @Component
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");
        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            response.getWriter().println("ok");
            return;
        }
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
```



## Response Headers

[How to Set a Header on a Response with Spring 5 | Baeldung](https://www.baeldung.com/spring-response-header)

```java
@Component
public class AddResponseHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        if (httpServletRequest.getHeader("origin").equals("http://localhost:9000"))
            httpServletResponse.addHeader("Access-Control-Allow-Origin", "http://localhost:9000");
        if (httpServletRequest.getHeader("origin").equals("https://tauc-beta.tplinkcloud.com"))
            httpServletResponse.addHeader("Access-Control-Allow-Origin", "https://tauc-beta.tplinkcloud.com");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "POST,OPTIONS");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type");
        httpServletResponse.addHeader("Connection", "keep-alive");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
```

[Spring实用系列-深入了解SpringMVC OncePerRequestFilter过滤器原理_onceperfilter-CSDN博客](https://blog.csdn.net/nihui123/article/details/103422837)



## 请求header

[Spring Boot 读取http headers内容_springboot httpheaders-CSDN博客](https://blog.csdn.net/abu935009066/article/details/112554194)
