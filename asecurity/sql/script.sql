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

CREATE TABLE agendamentos (
  id serial NOT NULL ,
  data_consulta date DEFAULT NULL,
  id_especialidade bigint(20) DEFAULT NULL,
  id_horario bigint(20) DEFAULT NULL,
  id_medico bigint(20) DEFAULT NULL,
  id_paciente bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_ESPECIALIDADE_ID (id_especialidade),
  KEY FK_HORA_ID (id_horario),
  KEY FK_MEDICO_ID (id_medico),
  KEY FK_PACIENTE_ID (id_paciente),
  CONSTRAINT FK_ESPECIALIDADE_ID FOREIGN KEY (id_especialidade) REFERENCES especialidades (id),
  CONSTRAINT FK_HORA_ID FOREIGN KEY (id_horario) REFERENCES horas (id),
  CONSTRAINT FK_MEDICO_ID FOREIGN KEY (id_medico) REFERENCES medicos (id),
  CONSTRAINT FK_PACIENTE_ID FOREIGN KEY (id_paciente) REFERENCES pacientes (id)
)

CREATE TABLE especialidades (
  id serial bigint(20) NOT NULL ,
  descricao text,
  titulo varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_TITULO (titulo),
  KEY IDX_ESPECIALIDADE_TITULO (titulo)
) 

CREATE TABLE horas (
  id serial bigint(20) NOT NULL ,
  hora_minuto time NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_HORA_MINUTO (hora_minuto),
  KEY IDX_HORA_MINUTO (hora_minuto)
) 

CREATE TABLE medicos (
  id serial bigint(20) NOT NULL,
  crm int(11) NOT NULL,
  data_inscricao date NOT NULL,
  nome varchar(255) NOT NULL,
  id_usuario bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_CRM (crm),
  UNIQUE KEY UK_NOME (nome),
  UNIQUE KEY UK_USUARIO_ID (id_usuario),
  CONSTRAINT FK_USUARIO_ID FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
) 

CREATE TABLE medicos_tem_especialidades (
  id_especialidade bigint(20) NOT NULL,
  id_medico bigint(20) NOT NULL,
  UNIQUE KEY MEDICO_UNIQUE_ESPECIALIZACAO (id_especialidade,id_medico),
  KEY FK_ESPECIALIDADE_MEDICO_ID (id_medico),
  CONSTRAINT FK_ESPECIALIDADE_MEDICO_ID FOREIGN KEY (id_medico) REFERENCES medicos (id),
  CONSTRAINT FK_MEDICO_ESPECIALIDADE_ID FOREIGN KEY (id_especialidade) REFERENCES especialidades (id)
) 

CREATE TABLE pacientes (
  id serial bigint(20) NOT NULL ,
  data_nascimento date NOT NULL,
  nome varchar(255) NOT NULL,
  id_usuario bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_PACIENTE_NOME (nome),
  KEY FK_PACIENTE_USUARIO_ID (id_usuario),
  CONSTRAINT FK_PACIENTE_USUARIO_ID FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
) 

CREATE TABLE perfis (
  id serial bigint(20) NOT NULL,
  descricao varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_PERFIL_DESCRICAO (descricao)
)

CREATE TABLE usuarios (
  id serial bigint(20) NOT NULL ,
  ativo tinyint(1) NOT NULL,
  email varchar(255) NOT NULL,
  senha varchar(255) NOT NULL,
  codigo_verificador varchar(6) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_USUARIO_EMAIL (email),
  KEY IDX_USUARIO_EMAIL (email)
) 

CREATE TABLE usuarios_tem_perfis (
  usuario_id bigint(20) NOT NULL,
  perfil_id bigint(20) NOT NULL,
  PRIMARY KEY (usuario_id,perfil_id),
  KEY FK_USUARIO_TEM_PERFIL_ID (perfil_id),
  KEY FK_PERFIL_TEM_USUARIO_ID (usuario_id),
  CONSTRAINT FK_PERFIL_TEM_USUARIO_ID FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
  CONSTRAINT FK_USUARIO_TEM_PERFIL_ID FOREIGN KEY (perfil_id) REFERENCES perfis (id)
) 
