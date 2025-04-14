package org.bbqqvv.backendeducation.service.img;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Upload ảnh (image/png, image/jpeg, ...)
     */
    public String uploadImage(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    /**
     * Upload nhiều ảnh
     */
    public List<String> uploadImages(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadImage(file));
        }
        return urls;
    }

    /**
     * Upload file bất kỳ (PDF, DOCX, ZIP...)
     */
    public String uploadFile(MultipartFile file) {
        try {
            Map<String, Object> options = ObjectUtils.asMap(
                    "resource_type", "raw"
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    options
            );
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}
