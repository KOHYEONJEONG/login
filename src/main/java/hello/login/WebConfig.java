package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    //스프링 컨테이너 초기화 시점에 자동으로 등록됨
    
    //WebMvcConfigurer 역할 : 인터셉터, 메시지 컨버터, 정적 리소스 경로, 포맷터 등 Web 관련 설정 확정

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        /*
         * ArgumentResolver 등록(개념은 MVC1편 참고)
         *
        */

        //⭐@Login 사용 등록
        resolvers.add(new LoginMemberArgumentResolver());

        //콘솔에 resolverArgument 실행( @Login) <-  있으면 실행된거임.
    }

    /**인터셉터 등록*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) { //implements WebMvcConfigurer의 구현체

        // 🚨필터 작성 시 whiteList 설정을 해야했지만
        // 🚨인터셉터는 인터셉터 등록 시 whiteList 및 제외할 경로까지 등록이 가능하다.

        //인터셉터 다중 등록도 가능

        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")//모든 경로에 로그체크 해!
                .excludePathPatterns("/css/**","/*.ico","/error");//exclude 이 경로들은 빼!, 이외에 것들만 로그인터센터가 찍힘.

        //로그인을 하지않고 바로 http://127.0.0.1:8091/items로 이동 시 '로그인 페이지'로 이동된다.
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")//모든 경로에 로그체크 해!
                .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error"); //exclude 이 경로는 제외! (/home , /item 들어가기전에 막음)
    }

    /**아래 @Bean 주석처리 한 이유는 필터보다 스프링에서 제공하는 인터셉터를 사용하는게 더 효율적이다. 위에 addInterceptors()를 보자*/
   // @Bean  //스프링부트가 was를 띄울때 필터도 같이 띄워준다.
    public  FilterRegistrationBean logFilter(){
      FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
      filterRegistrationBean.setFilter(new LogFilter());
      filterRegistrationBean.setOrder(1);//순서
      filterRegistrationBean.addUrlPatterns("/*");//모든 url

        return filterRegistrationBean;
        //hello.login.web.filter.LogFilter         : log filter init <- 출력된다.
    }

    /**⏬스프링 인터셉터를 사용할거기에 로그인 체크 두번되기 때문에 주석처리*/
  //  @Bean
    public  FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");//모든 url (LoginCheckFilter.java에서 화이트 리스트에 적힌 경로 제외하고 다 체크할거야~)

        return filterRegistrationBean;
    }

}
