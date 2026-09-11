# DeepBlue Rescue

## 1. Descripción breve
DeepBlue Rescue es una plataforma backend desarrollada para la gestión, seguimiento y control de centros de rescate de fauna, optimizando el registro de casos de rescate, el estado de los animales, sus historiales médicos y la asignación de especialistas en áreas específicas de tratamiento.

## 2. Modelo de datos
El sistema se compone de las siguientes entidades principales y sus respectivas tablas en la base de datos:
* **`rescue_centers`**: Almacena la información de los centros de rescate.
* **`rescue_cases`**: Registra los casos de rescate asociados a un centro específico.
* **`animals`**: Contiene la información individual de cada animal rescatado, vinculado a un caso de rescate y opcionalmente a un dispositivo de rastreo.
* **`medical_records`**: Almacena el expediente médico único de cada animal.
* **`specialists`**: Contiene los datos de los profesionales que laboran en el centro.
* **`expertise`**: Catálogo con las áreas de experiencia o especialidades médicas/técnicas.
* **`specialist_expertise`**: Tabla asociativa que implementa la relación muchos a muchos entre especialistas y áreas de experiencia.
* **`treatments`**: Registra los tratamientos aplicados a los animales por parte de los especialistas.

## 3. Relaciones implementadas
* **1:N (Uno a Muchos)**: 
  * `RescueCenter` a `RescueCase`: Un centro gestiona múltiples casos de rescate.
  * `RescueCase` a `Animal`: Un caso de rescate puede involucrar a uno o varios animales.
  * `Specialist` a `Treatment`: Un especialista puede realizar múltiples tratamientos.
* **1:1 (Uno a Uno)**:
  * `Animal` a `MedicalRecord`: Cada animal posee exactamente un registro médico único, garantizado mediante una restricción `UNIQUE` en la base de datos.
* **N:M (Muchos a Muchos)**:
  * `Specialist` a `Expertise`: Un especialista puede tener múltiples áreas de experiencia y una especialidad puede pertenecer a varios especialistas, gestionado a través de la tabla asociativa `specialist_expertise`.

## 4. Instrucciones para ejecutar
Para compilar y empaquetar el proyecto localmente utilizando Maven, ejecuta en la terminal:
```bash
mvn clean compile