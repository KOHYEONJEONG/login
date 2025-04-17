package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;


    //@GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId", required = false) Long memberId, Model model){

        //required = false <-- 로그인을 안한 사용자도 들어올 수 있게

        //쿠키받게
        if(memberId == null){
            return "home";
        }

        //로그인(쿠키가 있는 사용자)
        Member loginMember= memberRepository.findById(memberId);//멤버 저장소에서 꺼내기

        //db에서 못찾았다면
        if(loginMember == null){//쿠키가 없어지면 home으로 이동
            return "home";
        }

        //로그인시 넘어옴(DB에서 찾았다면~)
        model.addAttribute("member",loginMember);
        return "loginHome";
    }


    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        //세션 메니저에서 다 가져와서 사용할거임!
        /**세션 관리자에 저장된 회원 정보 조회*/
        Member member = (Member)sessionManager.getSession(request);

        //로그인
        if(member == null){
            return "home";
        }

        //로그인 성공 했다면  요청헤더=> Cookie: mySessionId=ee541731-7bfb-403a-a20e-764d526e8095
        model.addAttribute("member", member);
        return "loginHome";
    }


   // @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        //세션은 메모리를 사용하기 때문에 맨 처음 기본값이 true여서 바로 만들어진다.
        //처음 페이지에 들어올떄는 생성 안하기위해 false;(로그인 됐을때만 LoginContoller에서 로그인 되면 만들거임)
        //로그인 하고 나면 세션이 메모리에서 생기게 코딩했으므로 if문을 통과하게되며 아래 코드를 읽는다.
        HttpSession session = request.getSession(false);
        if(session == null){//false는 세션이 있으면 반환하지만, 없으면 null을 반환한다.
            return  "home";
        }

        //세션이 있으면 아래 단계를 실행한다.
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면(세션이 끊긴다고 보면 되겠지?)
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
       return "loginHome";
    }

   // @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        //spring에서 제공하는 SessionAttribute! ,  세션을 생성하지 않음!! 찾아만 옴
        //ㄴ required = false 은 세션이 없을 수 있으니까 무조건 받는다고 하면 안되겠지!!

        //http://127.0.0.1:8091/;jsessionid=F5BD349B363673B7B094F2BFA64D58FC <-- url로 세션을 관리함..
        //모든 링크에 붙여줘야하기에 현실성이 없어~~~ 거의 사용하지 않아.

        //세션에 회원 데이터가 없으면(세션이 끊긴다고 보면 되겠지?)
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

    //@Login으로 자동으로 세션을 찾아서 비교하게~ (ArgumentResolve 를 활용, 개념은 mvc1편 참고하자)
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {
        //만들어서 사용하면 공통 컨트롤러에 사용을 어노테이션 하나로 간편하게 사용가능하다.
        //@Login은 LoginMemberArgumentResolver.java에서 동작을 구현해놨다.
        //반환값은 session에 Member객체이다.

        //세션에 회원 데이터가 없으면(세션이 끊긴다고 보면 되겠지?)
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}