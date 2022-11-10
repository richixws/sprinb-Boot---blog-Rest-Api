package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment=mapToEntity(commentDto);

        //retrieve post entity by id
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        //set post to comment entity
        comment.setPost(post);

        //comment entity to DB
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {

        //retrieve comments by postId;
        List<Comment> comments=commentRepository.findByPostId(postId);

        //convert list of comment entities to list of comment dtos
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {

        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        //retrieve comment by id
        Comment comment= commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment does not belong to post");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {

        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        //retrieve comment by id
        Comment comment= commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment does not belong to post");
        }

        comment.setEmail(commentRequest.getEmail());
        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());

        Comment commentUpdate = commentRepository.save(comment);

        return mapToDTO(commentUpdate);

    }

    @Override
    public void deleteComment(long postId, long commentId) {

        //retrieve post entity by id
        Post post=postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        //retrieve comment by id
        Comment comment= commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment does not belong to post");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDTO(Comment comment){

       CommentDto commentDto = mapper.map(comment,CommentDto.class);

       /**
        CommentDto commentDto=new CommentDto();
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());
        return commentDto;
        **/
       return commentDto;
    }

    //convertir DTO a entity
    private Comment mapToEntity(CommentDto commentDto){

       Comment comment= mapper.map(commentDto, Comment.class);
        /**
        Comment dto=new Comment();
        dto.setName(commentDto.getName());
        dto.setBody(commentDto.getBody());
        dto.setEmail(commentDto.getEmail());
        return dto;
       **/

        return comment;
    }

}
