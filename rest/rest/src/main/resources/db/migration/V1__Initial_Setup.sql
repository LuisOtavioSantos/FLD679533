-- V1__Initial_Setup.sql
-- Migração inicial: Criação de todas as tabelas e carga de dados de exemplo do Marketplace

-- 1. Tabela de Clientes (também são os usuários/vendedores do sistema com autenticação JWT)
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    address VARCHAR(100) NOT NULL,
    gender VARCHAR(9) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CLIENT',
    is_email_verified BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Tabela de Produtos (Inventário)
CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    price DOUBLE NOT NULL,
    category VARCHAR(100) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    vendor_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (vendor_id) REFERENCES clients(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Tabela de Compras (relaciona Client ↔ Product)
CREATE TABLE IF NOT EXISTS purchases (
    id BIGINT NOT NULL AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DOUBLE NOT NULL,
    total_price DOUBLE NOT NULL,
    purchase_date DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Tabela de Comentários e Avaliações de Produtos
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    text VARCHAR(1000) NOT NULL,
    rating INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    product_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (client_id) REFERENCES clients(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Tabela de Tokens de Verificação de E-mail
CREATE TABLE IF NOT EXISTS email_verification_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL UNIQUE,
    client_id BIGINT NOT NULL,
    expiry_date DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================================
-- INSERÇÃO DE DADOS INICIAIS (SEED DATA)
-- ==========================================

-- Cria um usuário System Admin que será o dono dos produtos de exemplo
INSERT INTO clients (id, first_name, last_name, address, gender, email, password, role, is_email_verified) VALUES
(1, 'System', 'Admin', 'Matrix', 'Other', 'admin@marketplace.com', '$2a$10$w85I6rL3nN5x1k/P9H1u4eS7L/0qXh8A7/kY5p9H6.9/X5u/P/9/S', 'ADMIN', 1);

-- Insere Produtos do inventário de exemplo vinculados ao System Admin e com imagens locais
INSERT INTO products (name, description, price, category, stock, vendor_id, image_url) VALUES
('Teclado Mecânico', 'Teclado mecânico RGB com switches azuis, ideal para digitação e jogos.', 250.00, 'Periféricos', 15, 1, '/uploads/keyboard.png'),
('Monitor 144Hz', 'Monitor Gamer de 24 polegadas, painel IPS, 144Hz e tempo de resposta de 1ms.', 1200.00, 'Hardware', 50, 1, '/uploads/monitor.png'),
('Mouse Gamer', 'Mouse ergonômico com 10.000 DPI ajustáveis e iluminação customizável.', 180.00, 'Periféricos', 100, 1, '/uploads/mouse.png'),
('Placa de Vídeo RTX', 'Placa de vídeo de última geração com suporte a Ray Tracing e DLSS 3.0.', 3500.00, 'Hardware', 10, 1, '/uploads/gpu.png');
