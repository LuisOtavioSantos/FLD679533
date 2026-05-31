package com.example.rest.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Serviço responsável pelo armazenamento de arquivos (SRP - Single Responsibility).
 *
 * Padrão utilizado por empresas como Amazon, Mercado Livre e Shopee:
 * - O arquivo físico é armazenado em um diretório (equivalente ao Amazon S3)
 * - O banco de dados armazena apenas a URL/caminho para o arquivo
 *
 * Em produção, trocaria a pasta local por um serviço de Object Storage (S3, GCS, Azure Blob)
 */
@Service
public class FileStorageService {

    // Diretório onde as imagens serão salvas
    private final Path uploadDir = Paths.get("uploads");

    /**
     * Inicializa o diretório de uploads se não existir.
     */
    public FileStorageService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads", e);
        }
    }

    /**
     * Salva um arquivo no diretório de uploads.
     *
     * @param file    O arquivo enviado pelo usuário (MultipartFile)
     * @param prefix  Prefixo para o nome do arquivo (ex: "product-1")
     * @return        O caminho relativo do arquivo salvo (ex: "/uploads/product-1-abc123.jpg")
     */
    public String saveFile(MultipartFile file, String prefix) {
        try {
            // Validar tipo de arquivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Apenas arquivos de imagem são permitidos");
            }

            // Gerar nome único: product-1-uuid.ext
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";

            String uniqueFilename = prefix + "-" + UUID.randomUUID().toString().substring(0, 8) + extension;

            // Salvar arquivo no disco
            Path targetPath = uploadDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Retornar o caminho relativo (será servido como recurso estático)
            return "/uploads/" + uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar arquivo: " + e.getMessage(), e);
        }
    }
}
