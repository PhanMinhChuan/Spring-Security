package zuka.cloud.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/*
    Đồng nghĩa với @Controller nhưng chỉ được sử dụng cho các điểm cuối được cung cấp bởi framework
    using when convert to spring boot and call delete API
*/
@FrameworkEndpoint
public class CustomRevokeTokenEndpoint {
    /*
        Để tránh trường hợp nhiều lớp implement từ 1 lớp và @Autowire (Dependency Injection) cùng loại.
        Sẽ báo lỗi vì spring không phân biệt được giữa 2 bean đó. ta sẽ dùng thêm @Qualifier
    */
    ConsumerTokenServices tokenServices;

    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
    @ResponseBody
    public void revokeToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer")) {
            String tokenId = authorization.substring("Bearer".length() + 1);
            tokenServices.revokeToken(tokenId);
        }
    }
}
