-- V2__Seed_Products.sql
-- Dados iniciais: Produtos do inventário de exemplo

INSERT INTO products (name, description, price, category, stock) VALUES
('Teclado Mecânico', 'Teclado mecânico RGB com switches azuis, ideal para digitação e jogos.', 250.00, 'Periféricos', 15),
('Monitor 144Hz', 'Monitor Gamer de 24 polegadas, painel IPS, 144Hz e tempo de resposta de 1ms.', 1200.00, 'Hardware', 50),
('Mouse Gamer', 'Mouse ergonômico com 10.000 DPI ajustáveis e iluminação customizável.', 180.00, 'Periféricos', 100),
('Placa de Vídeo RTX', 'Placa de vídeo de última geração com suporte a Ray Tracing e DLSS 3.0.', 3500.00, 'Hardware', 10);
