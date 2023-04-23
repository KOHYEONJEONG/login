package hello.login.web.session;

import hello.login.domain.member.Member;
//import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        /**
         * 간단하게 테스트를 진행해보자, 여기서는 HttpServeltRequest, HttpServletResponse 객체를 '직접 사용할 수 없기 떄문에'
         * 테스트에서 비슷한 역할을 해주는 '가짜' MockHttpServletRequest, MockHttpServletResponse를 사용했다.
         * */

        //HttpServletResponse 구현체는 톰캣이 별도로 관리하기에 스프링에서 제공하는 MockHttpServletResponse를 활용하자.
        MockHttpServletResponse response = new MockHttpServletResponse();

        /**세션생성*/
        //멤버가 잘 들어갔는지 확인
        Member member = new Member();
        sessionManager.createSession(member, response);

        /**요청*/
        //요청에 응답 쿠키 저장이 되었는지 확인.
        MockHttpServletRequest request = new MockHttpServletRequest();

        //요청 받았다고 가정하고 웹 부라우저에서 만든 쿠키 서버로 전송한다면 🔽
       request.setCookies(response.getCookies());//mySessionId=12321231-23212-jdfjak

        /**세션 조회*/
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        /**세션만료*/
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull(); //삭제됐는지 확인
    }
}
