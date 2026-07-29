INSERT INTO tb_usuarios (id, email, senha) 
VALUES (1, 'admin@email.com', '123456')
ON DUPLICATE KEY UPDATE email=email;