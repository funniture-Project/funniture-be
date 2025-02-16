package com.ohgiraffers.funniture.auth.filter;

import com.ohgiraffers.funniture.common.AuthConstants;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("✅ jwtAuthorizationFilter - 요청 처리 중: " + request.getRequestURI());
        /*
        * 권한이 필요없는 리소스 (여기에 로그인 필요 없는 rest-api url 넣어야 함)
        * */

        List<String> roleLeessList = Arrays.asList(
                "/api/v1/product/\\d+",
                "/api/v1/product/\\w+",
                "/api/v1/product",
                "/api/product",
                "/api/v1/auth/login",
                "/api/v1/auth/signup",
                "/api/v1/inquiry",
                "/api/v1/inquiry/product",
                "/api/v1/inquiry/regist",
                "/api/v1/inquiry/delete",
                "/api/v1/inquiry/owner",
                "/api/v1/inquiry/modify",
                "/api/v1/inquiry/wait",
                "/api/v1/inquiry/complete",
                "/api/v1/rental",
                "/api/v1/rental/user",
                "/api/v1/rental/admin",
                "/api/v1/rental/owner",
                "/api/v1/review/product/\\d+",
                "/api/v1/product/search?s=\\w+",
                "/api/v1/review",
                "/api/v1/review/\\d++",
                "/api/v1/review/(\\d+)?offset=\\d+",
                "/swagger-ui/(.*)",        //swagger 설정
                "/swagger-ui/index.html",  //swagger 설정
                "/v3/api-docs",              //swagger 설정
                "/v3/api-docs/(.*)",         //swagger 설정
                "/swagger-resources",        //swagger 설정
                "/swagger-resources/(.*)"    //swagger 설정
        );

        if(roleLeessList.stream().anyMatch(uri -> roleLeessList.stream().anyMatch(pattern -> Pattern.matches(pattern, request.getRequestURI())))){
            chain.doFilter(request,response);
            return;
        }

        String header = request.getHeader(AuthConstants.AUTH_HEADER);

        try {
            if(header != null && !header.equalsIgnoreCase("")){
                String token = TokenUtils.splitHeader(header);

                if(TokenUtils.isValidToken(token)){
                    Claims claims = TokenUtils.getClaimsFromToken(token);

                    MemberDTO authentication = new MemberDTO();
                    authentication.setUserName(claims.get("userName").toString());
                    authentication.setEmail(claims.get("email").toString());
                    System.out.println("claims ==================== " + claims.get("memberRole"));

                    AbstractAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(authentication, token, authentication.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    chain.doFilter(request,response);
                }else{
                    throw new RuntimeException("토큰이 유효하지 않습니다.");
                }
            }else{
                throw new RuntimeException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = jsonresponseWrapper(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
    }

    /**
     * 토큰 관련된 Exception 발생 시 예외 응답
     * */
    private JSONObject jsonresponseWrapper(Exception e) {
        String resultMsg = "";
        if (e instanceof ExpiredJwtException) {
            resultMsg = "Token Expired";
        } else if (e instanceof SignatureException) {
            resultMsg = "TOKEN SignatureException Login";
        }
        // JWT 토큰내에서 오류 발생 시
        else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";
        }
        // 이외 JTW 토큰내에서 오류 발생
        else {
            resultMsg = "OTHER TOKEN ERROR";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        return jsonObject;
    }
}
