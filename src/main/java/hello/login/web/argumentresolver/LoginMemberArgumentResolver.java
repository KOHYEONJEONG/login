package hello.login.web.argumentresolver;

import hello.login.domain.member.Member; //🚨java.lang.reflect 로 import 하지 말기!! 항상 조심!
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

    /**아래처럼 구현하면 됨.*/

    // LoginMemberArgumentResolver.java을 사용하려면 등록해야하며 WebConfig.java에 등록해줬다.
    // ㄴ addArgumentResolvers()오버라이딩해서 등록해주자.

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //✅supportsParameter는 캐싱처리되기에 💥한번 실행💥되면 또 실행되지 않아~ 로그도 한번만 찍힌다.
        log.info("supportsParameter 실행");

       //HomeController.java에 메서드 중 ✅ homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) { ... }
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class); //@Login 로그인에 어노테이션이 붙어 있는지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());//넘어온 Member 클래스가 파라미터이다.

        //두개 다 만족하면 resolveArgument()가 실행된다.🔽
        return hasLoginAnnotation && hasMemberType;
    }

    // ⏬ Argument 를 만들어서 반환
    //ㄴ 🚨🚨컨트롤러 호출 직전에 호출되기 때문에 필요한 파라미터 정보를 생성할 수 있다!!!🚨🚨
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

       log.info("resolverArgument 실행( @Login )");

       HttpServletRequest request = (HttpServletRequest)  webRequest.getNativeRequest();
       HttpSession session = request.getSession(false);//false로 해야지 세션이 없으면 null을 반환하고 만들지 않는다.(세션은 메모리를 사용하기에 항상 만들지 말자)
        
       //⏬의미없는 세션을 만드는걸 방지하기 위해서
       if(session == null){//session이 null이면 반환값에 NULL을 넣어줌 <-- (@Login Member loginMember)에 Member 말하는 거임
          return  null;// Member는 Null값으로 반환됨.
       }

        //⏬session이 있으면 값이 들어간 Member가 반환됨.
       return  session.getAttribute(SessionConst.LOGIN_MEMBER);
    }

    //⭐ 이제 ArgumentResolver를 등록하러 WebConfig.java로 가자.
}
