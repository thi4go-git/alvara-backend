# Subir Projeto:
- Execulte o docker compose na pasta alvara-update/docker-compose.yml para que
  o sistema suba em LOCALHOST (Subirá back+front).

-Após subir, acesse o banco e crie um usuário inical administrador:
insert into usuario (ativo,cpf,nome,password,role,username)
values (true,'12345678901','admin','admin','ADMIN','admin')
