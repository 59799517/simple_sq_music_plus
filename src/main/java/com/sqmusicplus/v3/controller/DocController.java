package com.sqmusicplus.v3.controller;

import com.sqmusicplus.v3.config.RequireLogin;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * API文档控制器 - Docsify
 */
@RequireLogin(value = false)
@Controller
@RequestMapping("/api/doc")
public class DocController {

    /**
     * 文档首页 - 支持 /api/doc, /api/doc/, /api/doc/index.html
     */
    @GetMapping(value = {"", "/", "/index.html"}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String index() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/index.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * README.md
     */
    @GetMapping(value = "/README.md", produces = "text/markdown;charset=UTF-8")
    @ResponseBody
    public String readme() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/README.md");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * _sidebar.md
     */
    @GetMapping(value = "/_sidebar.md", produces = "text/markdown;charset=UTF-8")
    @ResponseBody
    public String sidebar() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/_sidebar.md");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * _coverpage.md
     */
    @GetMapping(value = "/_coverpage.md", produces = "text/markdown;charset=UTF-8")
    @ResponseBody
    public String coverpage() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/_coverpage.md");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * docsify.min.js
     */
    @GetMapping(value = "/docsify.min.js", produces = "application/javascript;charset=UTF-8")
    @ResponseBody
    public byte[] docsifyJs() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/docsify.min.js");
        return resource.getInputStream().readAllBytes();
    }

    /**
     * vue.css
     */
    @GetMapping(value = "/vue.css", produces = "text/css;charset=UTF-8")
    @ResponseBody
    public byte[] vueCss() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/vue.css");
        return resource.getInputStream().readAllBytes();
    }

    /**
     * docsify-pagination.min.js
     */
    @GetMapping(value = "/docsify-pagination.min.js", produces = "application/javascript;charset=UTF-8")
    @ResponseBody
    public byte[] paginationJs() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/docsify-pagination.min.js");
        return resource.getInputStream().readAllBytes();
    }
}
