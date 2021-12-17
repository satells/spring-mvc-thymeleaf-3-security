DROP TABLE IF EXISTS agendamentos;
DROP TABLE IF EXISTS horas;


DROP TABLE IF EXISTS medicos_tem_especialidades;
DROP TABLE IF EXISTS medicos;
DROP TABLE IF EXISTS especialidades;

DROP TABLE IF EXISTS pacientes;


DROP TABLE IF EXISTS usuarios_tem_perfis;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS perfis;

/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
CREATE TABLE perfis (
	id		bigserial not null,
	descricao	varchar(255) not null,
	PRIMARY KEY	(id),
	UNIQUE		(descricao)
);
/*-----------------------------------------------------------------------------------------------------*/
CREATE TABLE usuarios (
	id			bigserial not null,
	ativo 			boolean not null,
	email 			varchar(255) not null,
	senha 			varchar(255) not null,
	codigo_verificador 	varchar(6) default null,
	PRIMARY KEY 		(id),
	UNIQUE			(email)
	
);
CREATE INDEX idx_usuario_email ON usuarios (email);



/*-----------------------------------------------------------------------------------------------------*/
CREATE TABLE usuarios_tem_perfis (
	usuario_id 	bigint not null,
	perfil_id 	bigint not null,
	PRIMARY KEY 	(usuario_id,perfil_id),
	FOREIGN KEY 	(usuario_id)	REFERENCES usuarios (id),
	FOREIGN KEY 	(perfil_id)	REFERENCES perfis (id)
);
/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/


CREATE TABLE especialidades (
	id		bigserial not null,
	descricao	text,
	titulo		varchar(255) not null,
	PRIMARY KEY	(id),
	UNIQUE		(titulo)
);
CREATE INDEX idx_especialidade_titulo ON especialidades (titulo);
/*-----------------------------------------------------------------------------------------------------*/
CREATE TABLE medicos (
	id		bigserial not null,
	crm		int not null,
	data_inscricao	date not null,
	nome 		varchar(255) not null,
	id_usuario	bigint default null,
	PRIMARY KEY	(id),
	UNIQUE		(crm),
	UNIQUE		(nome),
	UNIQUE		(id_usuario),
	FOREIGN KEY 	(id_usuario) REFERENCES usuarios (id)
);
/*-----------------------------------------------------------------------------------------------------*/
CREATE TABLE medicos_tem_especialidades (
	id_especialidade	bigint not null,
	id_medico		bigint not null,
	UNIQUE			(id_especialidade,id_medico),
	FOREIGN KEY		(id_medico)		REFERENCES medicos (id),
	FOREIGN KEY		(id_especialidade)	REFERENCES especialidades (id)
);
/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
CREATE TABLE pacientes (
	id			bigserial not null,
	data_nascimento 	date not null,
	nome 			varchar(255) not null,
	id_usuario 		bigint default null,
	PRIMARY KEY 		(id),
	UNIQUE			(nome),
	FOREIGN KEY 		(id_usuario) REFERENCES usuarios (id)
); 
/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

CREATE TABLE horas (
	id		bigserial not null,
	hora_minuto	time not null,
	PRIMARY KEY	(id),
	UNIQUE		(hora_minuto)
);

CREATE INDEX idx_hora_minuto ON horas (hora_minuto);


/*-----------------------------------------------------------------------------------------------------*/
CREATE TABLE agendamentos (
	id			bigserial not null,
	data_consulta		date default null,
	id_especialidade	bigint default null,
	id_horario		bigint default null,
	id_medico		bigint default null,
	id_paciente		bigint default null,
	PRIMARY KEY		(id),
	FOREIGN KEY		(id_especialidade)	REFERENCES especialidades (id),
	FOREIGN KEY		(id_horario)		REFERENCES horas (id),
	FOREIGN KEY		(id_medico)		REFERENCES medicos (id),
	FOREIGN KEY		(id_paciente)		REFERENCES pacientes (id)
);
/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/



INSERT INTO perfis (id,descricao) VALUES (1,'ADMIN'),(2,'MEDICO'),(3,'PACIENTE');

INSERT INTO horas (id, hora_minuto) VALUES (1,'07:00:00'),(2,'07:30:00'),(3,'08:00:00'),(4,'08:30:00'),(5,'09:00:00'),(6,'09:30:00'),(7,'10:00:00'),(8,'10:30:00'),(9,'11:00:00'),(10,'11:30:00'),(11,'13:00:00'),(12,'13:30:00'),(13,'14:00:00'),(14,'14:30:00'),(15,'15:00:00'),(16,'15:30:00'),(17,'16:00:00'),(18,'16:30:00'),(19,'17:00:00'),(20,'17:30:00');


INSERT INTO usuarios (ativo, email, senha) VALUES (true, 'msergiost@hotmail.com', '$2a$10$GNDvn3lJHD4n6Z5YK3JoGeniH7bxshFzyxMXrxLbHPQ52ifZC7dUS');

INSERT INTO usuarios_tem_perfis (usuario_id, perfil_id) VALUES (1,1);


insert into especialidades (titulo, descricao) values 
('Alergia e Imunologia','Diagnóstico e tratamento das doenças alérgicas e do sistema imunológico'),
('Anestesiologia','Área da Medicina que envolve o tratamento da dor, a hipnose e o manejo intensivo do paciente sob intervenção cirúrgica ou procedimentos'),
('Angiologia','É a área da medicina que estuda o tratamento das doenças do aparelho circulatório'),
('Cardiologia','Aborda as doenças relacionadas com o coração e sistema vascular'),
('Cirurgia Cardiovascular','Tratamento cirúrgico de doenças do coração'),
('Cirurgia da Mão','Cuida das doenças das mãos e dos punhos, incluindo os ossos, articulações, tendões, músculos, nervos, vasos e pele'),
('Cirurgia de cabeça e pescoço','Tratamento cirúrgico de doenças da cabeça e do pescoço'),
('Cirurgia do Aparelho Digestivo','Tratamento clínico e cirúrgico dos órgãos do aparelho digestório, como o esôfago, estômago, intestinos, fígado e vias biliares, e pâncreas'),
('Cirurgia Geral','É a área que engloba todas as áreas cirúrgicas, sendo também subdividida'),
('Cirurgia Pediátrica','Cirurgia geral em crianças'),
('Cirurgia Plástica','Correção das deformidades, malformações ou lesões que comprometem funções dos órgãos através de cirurgia de caráter reparador ou cirurgias estéticas'),
('Cirurgia Torácica','Atua na cirurgia da caixa torácica e vias aéreas'),
('Cirurgia Vascular','Tratamento das veias e artérias, através de cirurgia, procedimentos endovasculares ou tratamentos clínicos'),
('Clínica Médica (Medicina interna)','É a área que engloba todas as áreas não cirúrgicas, sendo subdividida em várias outras especialidades'),
('Coloproctologia','É a parte da medicina que estuda e trata os problemas do intestino grosso (cólon), sigmoide e doenças do reto, canal anal e ânus'),
('Dermatologia','É o estudo da pele anexos (pelos, glândulas), tratamento e prevenção das doenças'),
('Endocrinologia e Metabologia','É a área da Medicina responsável pelo cuidados aos hormônios, crescimento e glândulas como adrenal, tireoide, hipófise, pâncreas endócrino e outros'),
('Endoscopia','Endoscopia: Esta especialidade médica ocupa-se do estudo dos mecanismo fisiopatológicos, diagnóstico e tratamento de enfermidades passíveis de abordagem por procedimentos endoscópicos e minimamente invasivos');








