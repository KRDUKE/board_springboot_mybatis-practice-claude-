package com.dukefirstboard.board.controller; // 패키지 선언: 이 클래스가 속한 경로 (컨트롤러 패키지)

import lombok.RequiredArgsConstructor; // Lombok: final 필드에 대한 생성자를 자동 생성하는 애너테이션 임포트
import org.springframework.stereotype.Controller; // Spring MVC 컨트롤러임을 선언하기 위한 애너테이션 임포트
import org.springframework.web.bind.annotation.GetMapping; // GET 요청을 매핑하기 위한 애너테이션 임포트

@Controller // 이 클래스가 Spring MVC의 컨트롤러로 동작하도록 지정
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성 (현재 final 필드 없음, 불필요할 수 있음)
public class HomeController { // 홈 페이지를 처리하는 컨트롤러 클래스

    // 루트 URL("/")로 들어오는 GET 요청을 처리하는 메서드
    @GetMapping("/") // URL "/"에 대한 GET 요청을 이 메서드에 매핑
    public String index() { // 홈 페이지로 이동하는 메서드
        System.out.println("HomeController.index"); // 콘솔에 로그 출력 (디버깅용, 요청이 들어왔음을 확인)
        return "index"; // "index"라는 이름의 뷰(HTML 템플릿)를 반환하여 홈 페이지를 표시
    }
}