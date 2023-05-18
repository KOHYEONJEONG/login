package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**인터셉터 순서 1 */

/**
 * 인터셉터는 aop와 다르게 HTTP의 헤더 또는 URL의 정보들이 필요할 때 사용하며 HttpServeltRequest를 제공한다.
 * ㄴ 인터셉터는 스프링 MVC 구조에 특화된 필터 기능을 제공한다고 이해하면 된다
 * */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {//HandlerInterceptor는 스프링이 제공

    //스프링 인터셉터 흐름 : HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러

    //ctrl + o : 구현해야할 함수들 보여줌

    public static final String LOG_ID = "logId";


    /**
     * 컨트롤러 호출 전에 호출된다.
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        //(중요)uuid를 전역변수로 만들면 싱글톤으로 만들어지기 때문에 큰일난데! 그러면?? request.setAttribute()를 사용하자. request에 담아서 가지고 다니는 거지
        request.setAttribute(LOG_ID, uuid);//엑셀런트!(다른 구현 메소드에서 uuid를 사용하고 싶을때 이렇게 보내주자. , afterCompletion()에서 사용한다. )

        if(handler instanceof HandlerMethod){
            //@RequestMapping: HandlerMethod 가 넘어온다.(true)
            //정적 리소스: ResourceHttpRequestHandler  가 넘어온다(false)
            HandlerMethod hm = (HandlerMethod) handler;//호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
            //log.info("getMethod={}", hm.getMethod());
            //getMethod=public java.lang.String hello.login.web.login.LoginController.loginV3(hello.login.web.login.LoginForm, org.springframework.validation.BindingResult, javax.servlet.http.HttpServletRequest)

            //이 밑에 handler면 충분해 보인다.
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler );

        return true;//true 면 정상 호출이다. 다음 인터셉터나 컨트롤러가 호출된다.(false면 진행하지 않는다.)
    }

    /**
     * 컨트롤러 호출 후에 호출된다(컨트롤러에 예외가 발생하면 호출되지 않아~)
     * ㄴ컨트롤러에서 예외가 발생하면 postHandler은 호출되지 않는다.
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        //만약에 컨트롤러에서 예외가 터진 뒤 해결하고 싶다면 HandlerExceptionResolver를 구현하면된다.(selector 9. 강의에 있다)
        log.info("postHandler [{}]",modelAndView);
    }

    /**
     * 뷰가 렌더링 된 이후에도 호출된다~
     * ㄴ 예외가 발생한 경우 postHandle 가 호출되지 않기 때문이다. afterCompletion 은 예외가 발생해도 호출 되는 것을 보장하기에 예외로그는 여기다가 찍기
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        //preHandle()에서 request에 담았기 때문에 uuid를 같이 사용가능하다.
        Object logId = request.getAttribute(LOG_ID);//uuid

        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

        if(ex != null){
            log.error("afterCompletion error!!",ex);//에러는 중괄호 필요 없음
        }

    }
}
