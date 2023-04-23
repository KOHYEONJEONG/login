package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    //@PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        /**
         * 쿠키(영속쿠키와 세션쿠키가 있음)
         * -> 서버에서 로그인에 성공하면 HTTP 응답에 쿠키를 담아서 브라우저에 전달하자. 그러면 앞으로 해당 쿠키를 지속해서 보내준다.
         *
         * 영속 쿠키 : 만료 날짜를 입력하면 해당 날짜까지 유지
         * 세션 쿠키 : 만료 날짜를 생략하면 브라우저 종료시 까지만 유지
         * (브라우저 종료 시 로그아웃이 되길 기대하므로, 우리에게 필요한것은 세션 쿠키)
         *
         * */

        //쿠키에 시간 정보를 주지 않으면 세션 쿠기(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);//Reponse Headers에서 확인하면 Set-Cookie에서 memberId=1 확인 가능.
        return "redirect:/";

    }

    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        /**로그인 성공 처리*/
        //세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember, response);  //응답헤더 => Set-Cookie: mySessionId=ee541731-7bfb-403a-a20e-764d526e8095

        return "redirect:/";

    }



    //@PostMapping("/logout")
    public String logoutV1(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);//request를 줘야지 쿠키에 값을 꺼내서 삭제하잖아~
        return "redirect:/";
    }

    //로그아웃 메소드(logoutV1에서만 사용했음)
    private void expireCookie(HttpServletResponse response, String cookieName) {
        //쿠키를 없앨려면 시간을 다 없애면 된다!!!
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);// 0 (웹브라우저 종료)
        response.addCookie(cookie);
    }
}
