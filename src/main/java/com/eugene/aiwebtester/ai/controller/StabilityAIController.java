package com.eugene.aiwebtester.ai.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.eugene.aiwebtester.ai.model.ErrorResp;
import com.eugene.aiwebtester.ai.model.ImageDesc;
import com.eugene.aiwebtester.ai.model.ImageResp;
import com.eugene.aiwebtester.ai.model.StabilityImageStyle;
import com.eugene.aiwebtester.ai.service.ImageStabilityService;

import org.springframework.ui.Model;

@CrossOrigin
@Controller
public class StabilityAIController {

    private static final Logger LOG = LogManager.getLogger(AIController.class);
    private ImageStabilityService imageService;

    StabilityAIController(ImageStabilityService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/image")
    public String imageForm(Model model) {
        LOG.info("Setting form");

        model.addAttribute("imagedesc", new ImageDesc());
        model.addAttribute("imagestylelist", StabilityImageStyle.getEnumList(StabilityImageStyle.class));
        return "imagestability";
    }

    @PostMapping("/createImage")
    public String createImage(@ModelAttribute ImageDesc imageDesc, Model model) {
        LOG.info("Image Desc asked is :" + imageDesc.getDescription());
        LOG.info("Image Desc asked is :" + imageDesc.getImageStyle());
        LOG.info("Image Desc asked is :" + imageDesc.getHeight());
        LOG.info("Image Desc asked is :" + imageDesc.getWidth());
        try {

            ImageResp qr = imageService.generate(imageDesc);// , conversationId, 10);
            model.addAttribute("imageResp", qr);
        } catch (Exception ex) {
            ErrorResp er = new ErrorResp();
            er.setErrorMsg(ex.getMessage());
            model.addAttribute("error", er);
            model.addAttribute("imageResp", null);
            LOG.error(ex.getMessage(), ex);
        } finally {
            model.addAttribute("imagedesc", imageDesc);
            model.addAttribute("imagestylelist", StabilityImageStyle.getEnumList(StabilityImageStyle.class));
        }

        // model.addAttribute("resp", resp);
        // LOG.info("Response is " + imageData);
        return "imagestability";
    }

}