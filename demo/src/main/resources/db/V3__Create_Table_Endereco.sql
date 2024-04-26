CREATE TABLE IF NOT EXISTS `endereco` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pessoa_id` int NOT NULL,
  `logradouro` varchar(255) NOT NULL,
  `cep` varchar(200) NOT NULL,
  `numero` int NOT NULL,
  `cidade` varchar(255) NOT NULL,
  `estado` varchar(255) NOT NULL,
  `endereco_principal` boolean,
  PRIMARY KEY (`id`)
  FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);



