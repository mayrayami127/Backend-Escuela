Fronted-Escuela# Sistema de Gestión Escolar — Microservicios con Spring Cloud

Aplicación para la gestión de un colegio (alumnos, cursos, notas, personal y usuarios con roles),
construida con arquitectura de microservicios y un frontend Angular.

## Tecnologías

- **Backend:** Java 17, Spring Boot 3.3, Spring Cloud (Gateway, Eureka, Config Server), Spring Web (API REST), Spring Security + JWT, Spring Data JPA / Hibernate, RabbitMQ, Swagger / OpenAPI.
- **Base de datos:** H2 en desarrollo, MySQL 8 en producción.
- **Frontend:** Angular + Bootstrap.
- **Despliegue:** Docker y Docker Compose.

## Arquitectura

| Servicio | Puerto | Función |
|---|---|---|
| `registry` | 8761 | Eureka Server: registro y descubrimiento de servicios |
| `springconfigserver` | 8888 | Config Server: configuración centralizada |
| `gateway` | 8080 | API Gateway: único punto de entrada, valida el JWT y reenvía a cada servicio |
| `Security` | 8085 | Usuarios, login y emisión del JWT |
| `Cursos` | 8081 | Cursos, notas e inscripciones (control de cupo) |
| `Alumno` | 8082 | Alumnos (nunca se borran: se deshabilitan) |
| `Administracion` | 8083 | Personal administrativo |
| RabbitMQ | 5672 / 15672 | Mensajería asincrónica (15672 = panel web) |
| MySQL | 3306 | Base de datos en producción |
| Frontend (repo `fronted-Escuela`) | 4200 | Aplicación Angular |

Cada microservicio sigue **3 capas** (controller → service → repository), el controller trabaja con **DTO**,
los errores se manejan con `@RestControllerAdvice` y todos exponen **Swagger** (`/swagger-ui/index.html` en su puerto).

### Seguridad

1. El usuario inicia sesión en `POST /auth/authenticate` y recibe un JWT.
2. El gateway valida el token en cada pedido y agrega los headers internos `X-User`, `X-Roles` y `X-Gateway-Secret`.
3. Cada microservicio solo acepta pedidos que vengan del gateway y aplica las restricciones por rol con `@PreAuthorize`.

### Comunicación asincrónica (RabbitMQ)

Cuando se crea, modifica o deshabilita un alumno, el servicio **Alumno** publica un evento en el exchange `escuela.exchange`.

- **Cursos** (`escuela.cursos.alumnos.queue`) guarda la inscripción del alumno y controla el cupo: si el curso está lleno, el alumno no se inscribe (queda registrado en el log). Si el alumno se deshabilita o cambia de curso, se libera el lugar.
- **Administración** (`escuela.admin.alumnos.queue`) recibe el mismo evento y lo registra.

## Roles

| Rol | Qué puede hacer |
|---|---|
| **DOCENTE** | Ve solo los cursos que tiene asignados y agrega, modifica y elimina notas de esos cursos. No ve alumnos. |
| **DIRECTOR** | Ve todos los cursos, las notas y los alumnos. No puede modificar notas. |
| **ROOT** | Crea usuarios con su perfil y crea cursos. No puede agregar alumnos ni ve la pantalla de alumnos. |
| **PRECEPTOR** | Agrega y modifica alumnos, los deshabilita (nunca los borra) y crea cursos. |
| **ADMINISTRATIVO** | Gestiona el personal administrativo y consulta alumnos y cursos. |

Un docente ve un curso cuando el campo **email del docente** del curso coincide con el email con el que inicia sesión.

El frontend filtra por rol el menú, las rutas (guards) y los botones; el backend vuelve a validar cada pedido.

### Usuarios de prueba

| Email | Contraseña | Rol |
|---|---|---|
| `root@escuela.com` | `root123` | ROOT |
| `preceptor@escuela.com` | `prec123` | PRECEPTOR |
| `docente@escuela.com` | `doc123` | DOCENTE |
| `director@escuela.com` | `dir123` | DIRECTOR |
| `admin@escuela.com` | `adm123` | ADMINISTRATIVO |

Root puede crear más usuarios desde la pantalla **Usuarios**.

## Repositorios

El proyecto está dividido en **dos repositorios independientes** que deben quedar en carpetas hermanas:

```
proyecto/
├── Escuela-main/        backend (este repositorio)
└── fronted-Escuela/     frontend Angular + Bootstrap
```

## Ejecución en desarrollo (H2)

1. **RabbitMQ** (Docker). En la máquina donde corre Docker:

   ```
   docker run -d --name rabbitmq-escuela -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   ```

   Panel web: `http://<IP-de-Docker>:15672` (usuario `guest`, contraseña `guest`).
   Si Docker corre en una máquina virtual (o en otra PC), hay que indicarle a Alumno, Cursos y Administración dónde está RabbitMQ
   con la variable de entorno `SPRING_RABBITMQ_HOST=<IP-de-la-máquina-virtual>`
   (en Eclipse: *Run Configurations → Environment*). Con Docker en la misma PC no hace falta, el valor por defecto es `localhost`.
2. Levantar los servicios del backend en este orden:
   1. `registry`
   2. `springconfigserver`
   3. `Security`
   4. `Alumno`, `Cursos` y `Administracion`
   5. `gateway`
3. Verificar en http://localhost:8761 que todos estén registrados.
4. **Frontend**, dentro de `fronted-Escuela`:

   ```
   npm install
   ng serve
   ```

   Abrir http://localhost:4200. El frontend pide la API al mismo origen (`/api` y `/auth`) y `ng serve` la reenvía
   al gateway en `http://localhost:8080` mediante `proxy.conf.json`, por eso el gateway tiene que estar levantado.

## Ejecución en producción (Docker + MySQL)

Con las dos carpetas hermanas (`Escuela-main` y `fronted-Escuela`) en la máquina donde corre Docker:

```
cd Escuela-main
docker compose -f docker-compose.prod.yml up --build -d
```

- Aplicación: `http://<IP-de-Docker>:4200`
- Gateway (API): `http://<IP-de-Docker>:8080`
- Eureka: `http://<IP-de-Docker>:8761`
- RabbitMQ: `http://<IP-de-Docker>:15672` (usuario `guest`, contraseña `guest`)

Detalles:

- Cada microservicio y el frontend se compilan dentro de Docker (build multi-stage): no hace falta compilar antes con Maven ni con npm.
- El contenedor del frontend usa **nginx**: sirve la aplicación en el puerto 80 (publicado como 4200) y reenvía `/api` y `/auth` al gateway por la red interna de Docker.
- Si ya hay un RabbitMQ levantado a mano (`docker run`), eliminarlo antes: `docker rm -f rabbitmq-escuela`.
- La primera construcción tarda varios minutos (descarga las dependencias de Maven). Se necesitan unos 6 GB de RAM libres para correr los 7 servicios Java, MySQL y RabbitMQ.
- Ver el estado: `docker compose -f docker-compose.prod.yml ps`. Detener todo: `docker compose -f docker-compose.prod.yml down`.

## Cómo probar RabbitMQ

1. Iniciar sesión como `preceptor@escuela.com` (desde el frontend o con `POST /auth/authenticate`).
2. Crear un curso con cupo 1 (pantalla **Cursos**, o `POST /api/cursos/agregar`):
   `{"materia":"Matematica","nombreMaestro":"Perez","numSalon":1,"cupo":1,"horario":"Lunes 8:00","docenteEmail":"docente@escuela.com"}`
3. Crear un alumno en ese curso (pantalla **Alumnos**). En las consolas de Cursos y Administración aparece el evento recibido y, en Cursos, "Alumno inscripto".
4. Consultar `GET /api/cursos/{id}/inscripciones` (preceptor, director o root): devuelve los IDs de los alumnos inscriptos.
5. Crear un segundo alumno en el mismo curso: Cursos informa "Cupo lleno" y no lo inscribe.
6. Deshabilitar el primer alumno: se libera el lugar y la lista de inscriptos queda vacía.

En http://localhost:15672, pestaña **Queues**, se ven las colas y los mensajes.

## Estructura del backend

```
Escuela-main/
├── Administracion/        ms de personal administrativo
├── Alumno/                ms de alumnos
├── Cursos/                ms de cursos, notas e inscripciones
├── Security/              ms de usuarios y JWT
├── gateway/               API Gateway
├── registry/              Eureka Server
├── springconfigserver/    Config Server
├── docker-compose.yml
└── docker-compose.prod.yml
```
