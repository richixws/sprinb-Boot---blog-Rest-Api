package com.springboot.blog.service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(long postId, long commentId);

    CommentDto updateComment(long postId, long commentId, CommentDto commentRequest);

    void deleteComment(long postId, long commentId);




}
