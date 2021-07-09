package zuka.cloud.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//using when convert spring mvc to spring boot and call api on another domain
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilterConfiguration implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletResponse servletResp = (HttpServletResponse) servletResponse;
        servletResp.setHeader("Access-Control-Allow-Origin", "*");                                  //Cho phép những domain nào truy cập
        servletResp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");   //Cho phép những method nào truy cập
        servletResp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");       //Cho phép những thông tin ở header nhận
        //Mô tả thời gian hợp lệ của perflight, nếu quả hạn
        //Nếu quá hạn brower sẽ tự tạo một perflight mới
        servletResp.setHeader("Access-Control-Max-Age", "3600");

        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
            servletResp.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
