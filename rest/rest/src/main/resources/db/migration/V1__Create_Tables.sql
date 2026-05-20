-- V1__Create_Tables.sql
-- Migração inicial: Criação das tabelas do sistema de inventário
-- Flyway executa automaticamente ao iniciar a aplicação (em ordem de versão)

-- Tabela de Clientes (também são os usuários do sistema com autenticação JWT)
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    address VARCHAR(100) NOT NULL,
    gender VARCHAR(9) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de Produtos (Inventário)
CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    price DOUBLE NOT NULL,
    category VARCHAR(100) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de Compras (relaciona Client ↔ Product)
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
