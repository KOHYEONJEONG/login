package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //파라미터에만 사용
@Retention(RetentionPolicy.RUNTIME) // 리플렉션 등을 활용할 수 있도록 런타임까지 애노테이션 정보가 남아있음
public @interface Login { //커스텀 어노테이션이다! 자주 사용되는거 하나 만들어 두면 다른 개발자가 사용하기 편하다.

    // (순서1) 생성 : @Login 애노테이션 생성 (지금 페이지)
    // (순서2) 구현 : LoginMemberArgumentResolber.java 생성
    // (순서3) 등록 : webConfig.java에서 addArgumentResolvers() 오버라이딩해서 @Login 등록

}
