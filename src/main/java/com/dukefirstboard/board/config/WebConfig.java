package com.dukefirstboard.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration: 이 클래스가 Spring의 설정 클래스임을 나타냄. 애플리케이션 시작 시 Spring이 이 클래스를 읽어 설정을 적용함.
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // resourcePath: 뷰(HTML)에서 파일에 접근할 때 사용할 URL 경로. 예: "/upload/파일명"으로 요청하면 파일을 제공함.
    private String resourcePath = "/upload/**";

    // savePath: 실제 파일이 저장된 물리적 경로. "file:///" 접두어를 붙여 로컬 파일 시스템을 가리킴.
    // 현재 설정은 "C:/Users/DUKE/Downloads/testsave" 디렉토리에 파일을 저장하며, 주석 처리된 경로는 이전에 사용했던 경로임.
    private String savePath = "file:///C:/Users/DUKE/Downloads/testsave";
//    private String savePath = "file:///C:/development/intellij_community/spring_upload_files/";

    // WebMvcConfigurer 인터페이스의 메서드를 오버라이드하여 정적 자원(파일)을 처리하는 방식을 커스터마이징.
    // 이 메서드는 파일 업로드 후 뷰에서 접근할 수 있도록 경로를 매핑해줌.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/upload/**"로 들어오는 요청을 처리하도록 리소스 핸들러를 등록.
        // 예: "http://localhost:8080/upload/파일명" 요청이 오면 savePath에 저장된 파일을 찾아 반환함.
        registry.addResourceHandler(resourcePath)
                // 실제 파일이 저장된 디렉토리를 지정. Spring이 이 경로에서 파일을 읽어 뷰로 제공.
                .addResourceLocations(savePath);
    }
}