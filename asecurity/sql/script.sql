DROP TABLE IF EXISTS agendamentos;
DROP TABLE IF EXISTS especialidades;
DROP TABLE IF EXISTS horas;
DROP TABLE IF EXISTS medicos;
DROP TABLE IF EXISTS medicos_tem_especialidades;
DROP TABLE IF EXISTS pacientes;
DROP TABLE IF EXISTS perfis;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS usuarios_tem_perfis;

/*=====================================================================================================*/
CREATE TABLE especialidades (
  id bigserial NOT NULL ,
  descricao text,
  titulo varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE  (titulo)
);



CREATE TABLE horas (
  id bigserial NOT NULL ,
  hora_minuto time NOT NULL,
  PRIMARY KEY (id),
  UNIQUE  (hora_minuto)
);


CREATE TABLE medicos_tem_especialidades (
  id_especialidade bigint NOT NULL,
  id_medico bigint NOT NULL,
  UNIQUE  (id_especialidade,id_medico),
  CONSTRAINT FK_ESPECIALIDADE_MEDICO_ID FOREIGN KEY (id_medico) REFERENCES medicos (id),
  CONSTRAINT FK_MEDICO_ESPECIALIDADE_ID FOREIGN KEY (id_especialidade) REFERENCES especialidades (id)
);


CREATE TABLE perfis (
  id bigserial NOT NULL,
  descricao varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE  (descricao)
);

CREATE TABLE usuarios_tem_perfis (
  usuario_id bigint NOT NULL,
  perfil_id bigint NOT NULL,
  PRIMARY KEY (usuario_id,perfil_id),
  KEY FK_USUARIO_TEM_PERFIL_ID (perfil_id),
  KEY FK_PERFIL_TEM_USUARIO_ID (usuario_id),
  CONSTRAINT FK_PERFIL_TEM_USUARIO_ID FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
  CONSTRAINT FK_USUARIO_TEM_PERFIL_ID FOREIGN KEY (perfil_id) REFERENCES perfis (id)
) ;

CREATE TABLE usuarios (
  id bigserial NOT NULL ,
  ativo tinyint(1) NOT NULL,
  email varchar(255) NOT NULL,
  senha varchar(255) NOT NULL,
  codigo_verificador varchar(6) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE  (email),
  KEY IDX_USUARIO_EMAIL (email)
);

CREATE TABLE agendamentos (
  id bigserial NOT NULL ,
  data_consulta date DEFAULT NULL,
  id_especialidade bigint DEFAULT NULL,
  id_horario bigint DEFAULT NULL,
  id_medico bigint DEFAULT NULL,
  id_paciente bigint DEFAULT NULL,
  PRIMARY KEY (id),
  
  CONSTRAINT FK_ESPECIALIDADE_ID FOREIGN KEY (id_especialidade) REFERENCES especialidades (id),
  CONSTRAINT FK_HORA_ID FOREIGN KEY (id_horario) REFERENCES horas (id),
  CONSTRAINT FK_MEDICO_ID FOREIGN KEY (id_medico) REFERENCES medicos (id),
  CONSTRAINT FK_PACIENTE_ID FOREIGN KEY (id_paciente) REFERENCES pacientes (id)
);
CREATE TABLE pacientes (
  id bigserial NOT NULL ,
  data_nascimento date NOT NULL,
  nome varchar(255) NOT NULL,
  id_usuario bigint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE (nome),
  KEY FK_PACIENTE_USUARIO_ID (id_usuario),
  CONSTRAINT FK_PACIENTE_USUARIO_ID FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
); 
CREATE TABLE medicos (
  id bigserial NOT NULL,
  crm int NOT NULL,
  data_inscricao date NOT NULL,
  nome varchar(255) NOT NULL,
  id_usuario bigint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE  (crm),
  UNIQUE  (nome),
  UNIQUE  (id_usuario),
  CONSTRAINT FK_USUARIO_ID FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
);
