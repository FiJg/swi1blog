package cz.osu.swi1.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private Long id;
    private String name;
    private String content;


}
