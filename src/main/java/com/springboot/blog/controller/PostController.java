package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostDtoV2;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping()
public class PostController {

    @Autowired
    private PostService postService;


    //create blog post rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody  PostDto postDto){

        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);

    }
    //get all posts rest api
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPost(@RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                   @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                   @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                   @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return postService.listPosts(pageNo,pageSize,sortBy,sortDir);

    }
    //get post by id
    @GetMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id){

        return new ResponseEntity<>(postService.getPostById(id),HttpStatus.OK);
    }

    //get post by id version 2
    @GetMapping("/api/v2/posts/{id}")
    public ResponseEntity<PostDtoV2> getPostByIdV2(@PathVariable(name = "id") long id){

        PostDto postDto= postService.getPostById(id);

        PostDtoV2 postDtoV2=new PostDtoV2();
        postDtoV2.setId(postDto.getId());
        postDtoV2.setTitle(postDtoV2.getTitle());
        postDtoV2.setDescription(postDtoV2.getDescription());
        postDtoV2.setContent(postDtoV2.getContent());

        List<String> tags=new ArrayList<>();
        tags.add("Java");
        tags.add("Spring Boot");
        tags.add("AWS");

        postDtoV2.setTags(tags);
        return  ResponseEntity.ok(postDtoV2);
    }

    //update post by id rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id){

         PostDto postdto=postService.updatePost(postDto,id);
         return new ResponseEntity<>(postdto,HttpStatus.OK);

    }
     //delete post rest api
     @PreAuthorize("hasRole('ADMIN')")
     @DeleteMapping("api/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
       postService.deletePost(id);
       return new ResponseEntity<>("post entity delete successfully", HttpStatus.OK);
    }




}
