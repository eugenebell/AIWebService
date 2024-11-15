package com.eugene.aiwebtester.ai.service;

import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.StabilityAiImageModel;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eugene.aiwebtester.ai.model.ImageDesc;
import com.eugene.aiwebtester.ai.model.ImageResp;

@Service
public class ImageStabilityService {

    private StabilityAiImageModel stabilityAiImageModel;

    @Autowired
    public ImageStabilityService(StabilityAiImageModel stabilityAiImageModel) {
        this.stabilityAiImageModel = stabilityAiImageModel;
    }

    public ImageResp generate(ImageDesc imageDesc) {
        ImageResponse ir = stabilityAiImageModel.call(
                new ImagePrompt(imageDesc.getDescription(),
                        StabilityAiImageOptions.builder()
                                .withStylePreset(imageDesc.getImageStyle())// "cinematic")
                                .withN(1)
                                .withHeight(imageDesc.getHeight())// 1024)
                                .withWidth(imageDesc.getWidth()).build()));// 1024).build()));

        ImageGeneration ig = ir.getResult();
        String imageData = ig.getOutput().getB64Json();

        ImageResp qr = new ImageResp();
        qr.setDescription(imageDesc.getDescription());
        qr.setId(imageDesc.getId());
        qr.setImage(imageData);
        return qr;
    }
}
