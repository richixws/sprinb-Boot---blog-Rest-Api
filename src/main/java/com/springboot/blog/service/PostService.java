package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import javafx.geometry.Pos;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);
    List<PostDto> listPosts();
    PostDto getPostById(long id);
    PostDto updatePost(PostDto postDto, long id);
    void deletePost(long id);

}
