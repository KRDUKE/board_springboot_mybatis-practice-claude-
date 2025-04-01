package com.dukefirstboard.board; // 패키지 선언: 이 클래스가 속한 경로 (루트 패키지)

import org.springframework.boot.SpringApplication; // Spring Boot 애플리케이션을 실행하기 위한 클래스 임포트
import org.springframework.boot.autoconfigure.SpringBootApplication; // Spring Boot 자동 설정을 활성화하는 애너테이션 임포트

@SpringBootApplication // Spring Boot 애플리케이션임을 선언하며 자동 설정과 컴포넌트 스캔을 활성화
public class BoardSpringbootMybatisPracticeApplication { // Spring Boot 애플리케이션의 메인 클래스

	// 애플리케이션의 진입점(entry point) 메서드
	public static void main(String[] args) { // 프로그램 실행 시 호출되는 정적 메서드
		SpringApplication.run(BoardSpringbootMybatisPracticeApplication.class, args); // Spring Boot 애플리케이션 시작
	}
}