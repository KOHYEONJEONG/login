package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter { //WebConfig.java에서 실행해줌. (loginCheckFilter 메서드)

    //인증체크 안하는 경로, 들어갈 수 있는 것들은 허용가능하도록 리스트
    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};//인증 필터를 적용해도 홈, 회원가입, 로그인 화면, css 같은 리소스에는 접근할 수 있어야 한다

    /** public default void init(FilterConfig filterConfig) throws ServletException
     * ㄴ 최근에 'default' 키워드가 들어가서 이제는 모든걸 구현하지 않아도 된다!!*/
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();//경로

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 페크 필터 시작 {}", requestURI);
            if (isLoginCheckPath(requestURI)){
                //화이트 리스트가 아닐때 실행
                log.info("인증 체크 로직 실행 {}", requestURI);

                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    log.info("미인증 사용자 요청 {}", requestURI);

                    //로그인으로 redirect
                    //로그인 후에 다시 바꾸당한 페이지로 돌아갔으면 하기에~(서비스 품질 올라가!)
                    httpResponse.sendRedirect("/login?redirectURL="+requestURI);

                    return;//여기가 중요!, 미인증 사용자는 다음으로(더 이상 컨트롤러 진행하지 마) 진행하지 않고 끝!

                }//if

            }//if

            chain.doFilter(request,response);//(중요) 다음 필터 있으면 진행해~

        }catch (Exception e){
            throw  e;

        }finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }

    }


    /**
     * 화이트 리스트의 경우 인증 체크 안함
     * */
    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
}
