package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    private PostService postService;


    //create blog post rest api
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody  PostDto postDto){

        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);

    }
    //get all posts rest api
    @GetMapping
    public List<PostDto> getAllPost(){
        return postService.listPosts();

    }
    //get post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id){

        return new ResponseEntity<>(postService.getPostById(id),HttpStatus.OK);
    }

    //update post by id rest api
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable(name = "id") long id){

         PostDto postdto=postService.updatePost(postDto,id);
         return new ResponseEntity<>(postdto,HttpStatus.OK);

    }
     //delete post rest api
     @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
       postService.deletePost(id);
       return new ResponseEntity<>("post entity delete successfully", HttpStatus.OK);
    }




}
