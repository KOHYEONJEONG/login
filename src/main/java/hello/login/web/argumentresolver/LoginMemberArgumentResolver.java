package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    // LoginMemberArgumentResolver.java을 사용하려면 등록해야하며 WebConfig.java에 등록해줬다.
    // ㄴ addArgumentResolvers()오버라이딩해서 등록해주자.

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //supportsParameter는 캐싱처리되기에 한번 실행되면 또 실행되지 않아~ 로그도 한번 찍혀
       log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class); //@Login 로그인에 어노테이션이 붙어 있는지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());//넘어온 Member 객체가 파라미터이다.


        return hasLoginAnnotation && hasMemberType; //두개 다 만족하면 resolveArgument()가 실행된다.
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

       log.info("resolverArgument 실행");

       HttpServletRequest request = (HttpServletRequest)  webRequest.getNativeRequest();
       HttpSession session = request.getSession(false);//false로 해야지 세션이 없으면 null을 반환하고 만들지 않는다.(세션은 메모리를 사용하기에 항상 만들지 말자)

       if(session == null){
          return  null;
       }

       return  session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
