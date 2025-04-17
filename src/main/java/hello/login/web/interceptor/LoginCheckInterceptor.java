package hello.login.web.interceptor;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**인터셉터 체인 2*/
/**ㄴ인터셉터 순서 2 */

// 인증 체크 로그
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor { //LoginCheckFilter.java보다 훨씬 간결해진걸 알 수 있다.(안에서 보면 whiteList를 설정하는데 인터셉터는 등록할때 체크하면되기 때문이다)

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //로그인은 preHandle만 필요하다.

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();

        //LoginController::loginV3::session.setAttribute
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            //로그인으로 redirect(진짜 편리한다~)
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;//중요! 여기서 끝! 진행 안한다는 뜻
        }

        return true;//다음으로 진행해라(false면 진행 안함. 여기까지만 하겠다라는 뜻)
    }
}
