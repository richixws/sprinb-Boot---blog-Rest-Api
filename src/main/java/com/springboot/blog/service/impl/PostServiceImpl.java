package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl  implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public PostDto createPost(PostDto postDto) {

        Post post=mapToEntity(postDto);
        Post newPost=postRepository.save(post);
        PostDto postDtoResp=mapToDTO(newPost);
        return postDtoResp;

    }

    @Override
    public PostResponse listPosts(int pageNo,int pageSize,String sortBy, String sortDir) {

        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

         //create pageable instance
        Pageable pageable= PageRequest.of(pageNo,pageSize, Sort.by(sortBy).ascending());

        Page<Post> posts=postRepository.findAll(pageable);

         //get content for page object
        List<Post> listOfPost=posts.getContent();

        List<PostDto>  content=listOfPost.stream().map( post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse= new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }

    @Override
    public PostDto getPostById(long id) {

        Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {

        Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
       // post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post postupdate=postRepository.save(post);
        PostDto maDto=mapToDTO(postupdate);
        return maDto;
    }

    @Override
    public void deletePost(long id) {

        Post post =postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    //convert Entity into DTO
    private PostDto mapToDTO(Post post){

        PostDto postDto= mapper.map(post, PostDto.class);

        /**
        PostDto postDto=new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
         return postDto;
        **/

        return postDto;
    }

    //convertir DTO to entity
    private Post mapToEntity(PostDto postDto){

        Post post=mapper.map(postDto,Post.class);
       /**
        Post post=new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return  post;
        **/
       return post;
    }


}
