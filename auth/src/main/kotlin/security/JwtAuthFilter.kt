package org.example.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.repository.UserRepository
import org.example.service.JwtService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {

    private val filterLogger = LoggerFactory.getLogger(JwtAuthFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        filterLogger.info("JwtAuthFilter: Incoming request to ${request.requestURI}")
        val authHeader = request.getHeader("Authorization") ?: return filterChain.doFilter(request, response)

        if (!authHeader.startsWith(HEADER_PREFIX)) {
            return filterChain.doFilter(request, response)
        }

        val token = authHeader.substring(HEADER_PREFIX.length)

        val username = jwtService.extractUsername(token) ?: return filterChain.doFilter(request, response)

        val user = userRepository.findByUsername(username) ?: return filterChain.doFilter(request, response)

        if (jwtService.isTokenValid(token, user.username)) {
            val auth = UsernamePasswordAuthenticationToken(user.username, null, listOf())
            auth.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = auth
        }

        filterChain.doFilter(request, response)
    }
}

private const val HEADER_PREFIX = "Bearer "
