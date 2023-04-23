package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object >sessionStore = new ConcurrentHashMap<>();

    /**
     *  세션 생성
     *  1. sessionId 생성(임의로 추정 불가능한 랜덤 값
     *  2. 세션 저장소에 sessionId와 보관할 값 저장
     *  3. sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
    * */

    public void createSession(Object value, HttpServletResponse response){
        //sessionId 생성(임의로 추정 불가능한 랜덤 값

        //세션id를 생성후, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();//자바가 제공하는 임의의 값.
        sessionStore.put(sessionId, value);//value는 넘어온 값으로 저장

        //쿠키 생성
       Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
       response.addCookie(mySessionCookie);


    }

    /**세션조회*/
    public  Object getSession(HttpServletRequest request){

        Cookie sessionCookie = findCookie(request,SESSION_COOKIE_NAME);
        if(sessionCookie == null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());//멤버객체를 가져옴.
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {

        if(request.getCookies() == null ){
            return  null;
        }

        //Optional Stream
        //배열을 스트림으로 만든 후 조건을 준 후 찾은 값을 보내고, 없으면 null로 보내라
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);

    }

    /**
     * 세션만료
     * */
    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());//map에서 지워줌!
        }
    }


//    public static final String SESSION_COOKIE_NAME = "mySessionId";
//    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
//
//    /**
//     * 세션 생성
//     */
//    public void createSession(Object value, HttpServletResponse response) {
//
//        //세션 id를 생성하고, 값을 세션에 저장
//        String sessionId = UUID.randomUUID().toString();
//        sessionStore.put(sessionId, value);
//
//        //쿠키 생성
//        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
//        response.addCookie(mySessionCookie);
//    }
//
//    /**
//     * 세션 조회
//     */
//    public Object getSession(HttpServletRequest request) {
//        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
//        if (sessionCookie == null) {
//            return null;
//        }
//        return sessionStore.get(sessionCookie.getValue());
//    }
//
//    /**
//     * 세션 만료
//     */
//    public void expire(HttpServletRequest request) {
//        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
//        if (sessionCookie != null) {
//            sessionStore.remove(sessionCookie.getValue());
//        }
//    }
//
//
//    public Cookie findCookie(HttpServletRequest request, String cookieName) {
//        if (request.getCookies() == null) {
//            return null;
//        }
//        return Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals(cookieName))
//                .findAny()
//                .orElse(null);
//    }



}
