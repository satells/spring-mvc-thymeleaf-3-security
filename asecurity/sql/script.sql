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










