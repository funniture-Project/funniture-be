package com.ohgiraffers.funniture.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    public final Cloudinary cloudinary;

    public Map<String, Object> uploadFile(MultipartFile file){
        Map<String, Object> response = new HashMap<>();

        try{
            File uploadedFile = convertMultiPartToFile(file);

            System.out.println("uploadedFile = " + uploadedFile);

            Map uploadResult = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());

            System.out.println("uploadResult = " + uploadResult);

            response.put("url", uploadResult.get("url").toString());
            response.put("id", uploadResult.get("public_id").toString());

            // 로컬 파일 삭제
            uploadedFile.delete();
            // 업로드된 파일의 URL 반환
            return response;
        } catch (Exception e){
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    private static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)){
            fos.write(file.getBytes());
        }

        return convFile;
    }

}
