//package com.jwt.practice.jwtpractice.filter;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//public class MyFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//
//        // 토큰 : cos를 만들어줘야 함. 근데 id,pw가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그것을 응답으로 준다.
//        // 그리고 요청할때마다 header에 Authorization에 value값으로 토큰을 가지고 온다.
//        // 그 떄, 가져온 토큰이 토큰이 내가 만든 토큰이 맞는지만 검증하면 된다.( RSA OR HS256 )
//        if(req.getMethod().equals("POST")){
//            String headerAuth = req.getHeader("Authorization");
//
//            if(headerAuth.equals("cos")){
//                chain.doFilter(req,res);
//            }else {
//                PrintWriter out = res.getWriter();
//                out.println("인증안됨");
//            }
//        }
//    }
//}
