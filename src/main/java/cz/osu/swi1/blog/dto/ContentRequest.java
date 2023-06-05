package cz.osu.swi1.blog.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ContentRequest {
    private Long id;
    private String title;
    private String content;


}
