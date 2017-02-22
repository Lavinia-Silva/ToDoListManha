package br.senai.sp.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.auth0.jwt.JWTVerifier;

import br.senai.sp.controller.UsuarioController;

public class JWTInterceptor extends HandlerInterceptorAdapter {
	/**
	// VOLTANDO DO CONTROLE
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}*/
	// INDO PRO CONTROLE
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod method = (HandlerMethod) handler;
		System.out.println("Método Chamado ==>"+method.getMethod().getName());
		System.out.println("Controle Chmado ==>"+method.getBean().getClass().getSimpleName());
		//return super.preHandle(request, response, handler);
		
		// CASO A REQUISIÇÃO SEJA PARA /login OU /usuario, LIBERA O ACESSO
				String uri = request.getRequestURI();
				if(uri.endsWith("login") || uri.endsWith("usuario")) {
					return true;
				}else{
					String token = null;
					try {
						token = request.getHeader("Authorization");
						JWTVerifier verifier = new JWTVerifier(UsuarioController.SECRET);
						Map<String, Object> claims = verifier.verify(token);
						System.out.println("Nome do usuário:" + claims.get("nome_user"));
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						if (token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value());
							return false;
						} else {
							response.sendError(HttpStatus.FORBIDDEN.value());
							return false;
						}
					}
				}
	}
	
	
}
