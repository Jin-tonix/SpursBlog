package com.ohgiraffers.blog.jinhee.controller;

import com.ohgiraffers.blog.jinhee.model.dto.BlogDTO;
import com.ohgiraffers.blog.jinhee.service.JinheeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/jinhee")
public class JinheeController {

    private final JinheeService jinheeService;
    private BlogDTO currentBlog;

    @Autowired
    public JinheeController(JinheeService jinheeService) {
        this.jinheeService = jinheeService;
    }

    @GetMapping("/main")
    public String indexJinhee(){
        return "jinhee/main";
    }
    @GetMapping("/post")
    public String post(Model model) {
        return "jinhee/post";
    }

    @PostMapping("/post")
    public RedirectView postBlog(BlogDTO blogDTO, RedirectAttributes redirectAttributes) {
        if (blogDTO.getBlogTitle() == null || blogDTO.getBlogTitle().isEmpty() ||
                blogDTO.getBlogContent() == null || blogDTO.getBlogContent().isEmpty()) {
            return new RedirectView("/jinhee/post");
        }

        int result = jinheeService.post(blogDTO);

        if (result <= 0) {
            return new RedirectView("/jinhee/error/page");
        } else {
            redirectAttributes.addFlashAttribute("confirm", true);
            return new RedirectView("/jinhee/post");
        }
    }


    @GetMapping("/postpage")
    public String postPage(Model model) {
        if (currentBlog != null) {
            model.addAttribute("blogTitle", currentBlog.getBlogTitle());
            model.addAttribute("blogContent", currentBlog.getBlogContent());
        }
        return "jinhee/postpage";
    }

    @GetMapping("/postpage/{id}")
    public String postPage(@PathVariable Long id, Model model) {
        BlogDTO blogDTO = jinheeService.getBlogById(id);
        if (blogDTO != null) {
            model.addAttribute("blogTitle", blogDTO.getBlogTitle());
            model.addAttribute("blogContent", blogDTO.getBlogContent());
            model.addAttribute("blogDTO", blogDTO); // 이 부분은 선택적으로 필요할 수 있습니다.
            // 좋아요 수를 가져오는 로직을 여기에 추가해야 합니다.
            model.addAttribute("likeCount", jinheeService.getLikes(id));
        }
        return "jinhee/postpage";
    }

    @PostMapping("/postpage/{id}/like")
    public String likePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        jinheeService.likePost(id);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/jinhee/postpage/{id}";
    }

    @GetMapping("/edit/{id}")
    public String editBlog(@PathVariable Long id, Model model) {
        BlogDTO blogDTO = jinheeService.getBlogById(id);
        if (blogDTO == null) {
            return "redirect:/jinhee/journey";
        }
        model.addAttribute("blogDTO", blogDTO);
        return "jinhee/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@ModelAttribute BlogDTO blogDTO) {
        jinheeService.updateBlog(blogDTO);
        return "redirect:/jinhee/journey";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable Long id, Model model) {
        BlogDTO blogDTO = jinheeService.getBlogById(id);
        if (blogDTO == null) {
            return "redirect:/jinhee/journey";
        }
        model.addAttribute("blogDTO", blogDTO);
        return "jinhee/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        jinheeService.deleteBlogById(id);
        return "redirect:/jinhee/journey";
    }


    @GetMapping("/journey")
    public String share(Model model) {
        List<BlogDTO> blogs = jinheeService.getAllBlogs();
        model.addAttribute("blogs", blogs);
        return "jinhee/journey";
    }
}
