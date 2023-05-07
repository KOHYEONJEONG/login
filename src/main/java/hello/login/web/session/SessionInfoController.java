package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    //http://127.0.0.1:8091/session-info
  @GetMapping("/session-info")
    public String sesseionInfo(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session == null){
            return "세션이 없습니다.";
        }

        //세션 데이터 모두 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId = {}", session.getId());//세션 아이디
        log.info("getMaxInactiveInerval={}",session.getMaxInactiveInterval());//세션
        log.info("createTime={}",new Date(session.getCreationTime()));//기본이 long이여서 date로 바꿔주기 위해서~
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));//마지막으로 접근한 시간
        log.info("isNew={}",session.isNew());//이미 생성된 세션을 가져다 쓴다면 false

/**
      session name=loginMember, value=Member(id=1, loginId=test, name=테스터, password=test!)

      sessionId = 466759E3352B3CF81A157CAD04AF513D
      getMaxInactiveInerval=1800  (30초 , 비활성화 시키는 최대 인터발)
      createTime=Sun Apr 30 19:53:55 KST 2023  (생성 일자)
      lastAccessedTime=Sun Apr 30 19:53:55 KST 2023 (마지막 접근 일자)
      isNew=false
 */

      return "세션 출력";
  }
}
