package com.ohgiraffers.blog.jinhee.service;

import com.ohgiraffers.blog.jinhee.model.dto.BlogDTO;
import com.ohgiraffers.blog.jinhee.model.entity.JinheeBlog;
import com.ohgiraffers.blog.jinhee.repository.JinheeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JinheeService {

    private final JinheeRepository jinheeRepository;

    @Autowired
    public JinheeService(JinheeRepository jinheeRepository) {
        this.jinheeRepository = jinheeRepository;
    }

    @Transactional
    public int post(BlogDTO blogDTO) {
        JinheeBlog newBlog = new JinheeBlog();
        newBlog.setBlogTitle(blogDTO.getBlogTitle());
        newBlog.setBlogContent(blogDTO.getBlogContent());
        newBlog.setCreateDate(new Date());

        JinheeBlog savedBlog = jinheeRepository.save(newBlog);

        return savedBlog != null ? 1 : 0; // 저장 결과에 따라 1 또는 0 반환
    }

    public List<BlogDTO> getAllBlogs() {
        List<JinheeBlog> jinheeBlogs = jinheeRepository.findAll();
        List<BlogDTO> blogDTOs = new ArrayList<>();

        for (JinheeBlog blog : jinheeBlogs) {
            BlogDTO blogDTO = new BlogDTO();
            blogDTO.setId(blog.getId());
            blogDTO.setBlogTitle(blog.getBlogTitle());
            blogDTO.setBlogContent(blog.getBlogContent());
            blogDTO.setCreateDate(blog.getCreateDate());
            blogDTOs.add(blogDTO);
        }

        return blogDTOs;
    }

    public BlogDTO getBlogById(Long id) {
        JinheeBlog blog = jinheeRepository.findById(id).orElse(null);
        if (blog != null) {
            BlogDTO blogDTO = new BlogDTO();
            blogDTO.setId(blog.getId());
            blogDTO.setBlogTitle(blog.getBlogTitle());
            blogDTO.setBlogContent(blog.getBlogContent());
            blogDTO.setCreateDate(blog.getCreateDate());
            return blogDTO;
        }
        return null;
    }

    @Transactional
    public void deleteBlogById(Long id) {
        jinheeRepository.deleteById(id);
    }

    @Transactional
    public void updateBlog(BlogDTO blogDTO) {
        JinheeBlog blog = jinheeRepository.findById(blogDTO.getId()).orElse(null);
        if (blog != null) {
            blog.setBlogTitle(blogDTO.getBlogTitle());
            blog.setBlogContent(blogDTO.getBlogContent());
            jinheeRepository.save(blog);
        }
    }

    @Transactional
    public void likePost(Long id) {
        JinheeBlog blog = jinheeRepository.findById(id).orElse(null);
        if (blog != null) {
            blog.setLikes(blog.getLikes() + 1); // 좋아요 수 증가
            jinheeRepository.save(blog);
        }
    }

    public int getLikes(Long id) {
        JinheeBlog blog = jinheeRepository.findById(id).orElse(null);
        return blog != null ? blog.getLikes() : 0;
    }
}
