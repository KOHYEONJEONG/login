package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

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


    @GetMapping("/")
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
}