package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    //필터를 등록해야 사용 가능하다. WebConfig.java 고~

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    /** HttpServletRequest의 부모가 ServletRequest*/
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        //ServletRequest(부모)를 쓰는게 아닌 HttpServletRequest(자식)을 사용해야하기에 다운캐스팅 해줘야 해
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();//모든 사용자의 url를 남겨보자.

        //요청 온걸 구부한기 위해서
        String uuid = UUID.randomUUID().toString();
        try{
            log.info("REQEUST [{}][{}]", uuid, requestURI);
            chain.doFilter(request,response);//(중요) 다른 필터가 있다면 실행해라
        }catch (Exception e){
            throw  e;
        }finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);//실행마지막에 항상 찍힘
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }//서블릿 기술이기 때문에 구현체 3개가 생성된다.

}
