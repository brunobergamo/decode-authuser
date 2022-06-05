package com.ead.authuser.client;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    @Autowired
    RestTemplate restTemplate;

    @Value("${ead.api.url.course}")
    private String REQUEST_URI;

    //@Retry(name = "retryInstance",fallbackMethod = "retryfallback")
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable){
        List<CourseDto> searchResult = null;

        String url = getURL(userId, pageable);

        log.info("Resquest URL : {}" + url);
        try{
            ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<CourseDto>> (){};
            ResponseEntity<ResponsePageDto<CourseDto>> result = restTemplate.exchange(url , HttpMethod.GET,null,responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of elements : {}", searchResult.size());
        }catch(HttpStatusCodeException e){

            log.error("Error request/course {}", e);
        }
        log.info("Ending request / courses userId {}" , userId);
        return new PageImpl<>(searchResult);
    }

    public Page<CourseDto> retryfallback(UUID userId, Pageable pageable,Throwable t){
        log.error("Inside retry retryfallback , cause - {}" , t.toString());
        return new PageImpl<>(Collections.emptyList());
    }

    private String getURL(UUID userId, Pageable pageable) {
        return new StringBuilder(REQUEST_URI)
                .append("/courses?userId=").append(userId)
                .append("&page=").append(pageable.getPageNumber())
                .append("&size=").append(pageable.getPageSize())
                .append("&sort=").append(pageable.getSort()).toString().replace(": ",",");
    }
}