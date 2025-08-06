# Subir Projeto:
- Execulte o docker compose na pasta alvara-update/docker-compose.yml para que
  o sistema suba em LOCALHOST (Subirá back+front).


# ACTUATOR (logs, health, Metrics):
http://localhost:8080/actuator

# Logs:
http://localhost:8080/actuator/logfile

# Swagger:
http://localhost:8080/swagger-ui/index.html

# Obter token Oauth JTW:
localhost:8080/oauth/token

# Métricas:
- Conexões Ativas JDBC:
  http://localhost:8080/actuator/metrics/jdbc.connections.active

- Memória em uso:
  http://localhost:8080/actuator/metrics/jvm.memory.used

- FIM