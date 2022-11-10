package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import javafx.geometry.Pos;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);
    PostResponse listPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostById(long id);
    PostDto updatePost(PostDto postDto, long id);
    void deletePost(long id);

}
