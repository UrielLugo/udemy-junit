package com.uriellugo.udemyjunit.endpoints;

import com.uriellugo.udemyjunit.models.Usuario;
import lombok.Builder;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class byteArrayController {

    private static final String filename = "front_document_image_3";

    @GetMapping(value = "/image/array", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDto> getImageArray() throws IOException {
        Path fileDir = Paths.get("/Users/ulugo/Downloads/" + filename);
        byte[] array;

        array = Files.readAllBytes(fileDir);
        String arrayString = Base64.encodeBase64String(array);
        ImageDto imagenDto = ImageDto.builder().imageName("ImagenDemo").image(arrayString).build();

        return ResponseEntity.ok().body(imagenDto);
    }

    @GetMapping(value = "/entity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> getUser() {
        return ResponseEntity.ok().build();
    }
}

@Data
@Builder
class ImageDto {

    private String imageName;
    private String image;
}