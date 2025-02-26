FitQuest Gym API

📌 Descripción

FitQuest Gym es una API REST diseñada para la gestión de clientes en un gimnasio gamificado. Permite a los clientes realizar entrenamientos que contienen ejercicios, desbloquear logros y mejorar su experiencia fitness mediante un sistema de misiones y recompensas.

📋 Características

Gestión de clientes y entrenadores.

Creación y seguimiento de entrenamientos.

Administración de ejercicios dentro de los entrenamientos.

Sistema de logros y recompensas.

Integración con notificaciones por correo electrónico.

📌 Requisitos Previos

Antes de iniciar el proyecto, asegúrate de configurar las propiedades de correo en application.properties:

spring.mail.username=<TU_EMAIL>
spring.mail.password=<TU_CONTRASEÑA_GENERADA>

Nota: La contraseña debe ser una generada para aplicaciones en la configuración de seguridad de tu cuenta de Google.

📂 Diagrama UML

El diseño de la API sigue la siguiente estructura:

![Diagrama UML](./documentos/UML_FitQuestGym.png)




🚀 Instalación y Configuración

1️⃣ Clonar el repositorio

git clone https://github.com/tuusuario/fitquest-gym.git
cd fitquest-gym

2️⃣ Configurar base de datos

Asegúrate de que tu base de datos esté configurada en application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/fitquest
driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=tu_password

3️⃣ Construir y ejecutar la API

mvn clean install
mvn spring-boot:run

📡 Endpoints Principales

Método

Endpoint

Descripción

POST

/clientes

Registrar un nuevo cliente

GET

/entrenamientos

Obtener lista de entrenamientos

POST

/ejercicios

Agregar un nuevo ejercicio



📬 Contacto

Si tienes dudas o sugerencias, contacta con nosotros en teyssiere.lupab.@triana.salesianos.edu.