# Sistema de Gestión Escolar - Arquitectura de Microservicios

## Descripción

Aplicación desarrollada para la gestión de una institución educativa,
implementada utilizando una arquitectura basada en microservicios.

El sistema permite gestionar:

- Alumnos
- Cursos
- Docentes

Cada dominio posee su propio microservicio y API REST.

La arquitectura incorpora un API Gateway para centralizar el acceso,
Eureka Server para el registro y descubrimiento de servicios y
Spring Cloud Config Server para la configuración centralizada.

Los microservicios se registran en Eureka Server :8761.

El Config Server :8888 proporciona la configuración centralizada.
