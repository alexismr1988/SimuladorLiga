# Simulador de Liga (v0.5.0)

Este proyecto es una simulación realista de una liga de fútbol desde consola, orientado al aprendizaje de programación en Java mediante principios de programación orientada a objetos y modelado táctico.

## Funcionalidades principales

- **Persistencia completa en base de datos MariaDB/MySQL**  
  Clase `GestorBD` con todos los métodos CRUD (insert, update, delete, select) para las entidades principales.

- **Importación masiva desde archivo CSV**  
  Nuevo método `importarEquiposDesdeCSV()` en `GestorFicheros`, que permite crear equipos completos desde un archivo `.csv` (entrenadores, presupuesto y plantilla incluidos).

  ### Estructura del CSV (`plantillas/plantillas.csv`):
  Equipo,Presupuesto,Entrenador,EstiloEntrenador,NombreJugador,Dorsal,Posicion,Media
  
- Cada fila representa un jugador, pero también puede crear automáticamente equipos y entrenadores únicos.
- Facilita la creación de ligas personalizadas sin tocar el código fuente.

- **Creación de equipos con jugadores y entrenadores personalizados**  
Clase `Entrenador` con atributo `EstiloEntrenador` (defensivo, ofensivo, posesión, contraataque).

- **Alineaciones válidas con restricciones realistas por línea**  
Verificación de número mínimo por posición y formación táctica básica.

- **Bonus o penalización en la simulación**  
Si el estilo del entrenador se adapta o no a la táctica utilizada, se influye en el resultado del partido.

- **Gestión de presupuestos por equipo**  
Atributo `presupuesto` en la clase `Equipo`.

- **Cálculo automático del número de jornadas**  
Ya no es un atributo manual. Se calcula en función del número de equipos y si hay ida/vuelta.

- **Generación del calendario**  
Posibilidad de elegir entre liga de ida o ida y vuelta.

- **Simulación detallada de partidos con lógica avanzada**
- Impacto del portero en ocasiones rivales.
- Media por líneas (defensa, mediocampo, ataque).
- Tácticas utilizadas.
- Estilo del entrenador.

- **Clasificación automática y estadísticas acumuladas**  
Resultados, goles a favor, en contra, y diferencia de goles.

- **Exportación y carga de resultados desde fichero**  
Alternativa a la base de datos, usando archivos `.txt`.

## Tecnologías utilizadas

- Java 17
- NetBeans IDE
- JDBC + MariaDB
- Programación orientada a objetos (POO)

## Dependencias externas 

- OpenCSV (ejemplo: opencsv-5.7.1.jar)
- Apache Commons Lang (ejemplo: commons-lang3-3.12.0.jar)
- MariaDB Java Client (ejemplo: mariadb-java-client-3.5.3.jar)s)

## Estructura del proyecto

- `modelo/` — Clases del dominio (`Equipo`, `Jugador`, `Entrenador`, etc.)
- `servicio/` — Lógica principal de simulación (`Simulador.java`)
- `persistencia/` — Persistencia en ficheros o base de datos (`GestorFicheros`, `GestorBD`)
- `Main.java` — Entrada principal para pruebas o demostraciones

## Próximas funcionalidades

- Registro de goleadores y estadísticas individuales.
- Persistencia completa del estado de la liga.
- Interfaz gráfica (Swing o JavaFX).
- Exportación a formatos CSV o JSON.

## Estado del proyecto

Versión **0.5.0** — Incorporada la importación desde CSV, ampliando la flexibilidad de creación de ligas y simplificando la configuración. Consolidación del sistema de persistencia múltiple (BD y fichero).

## Autor

Alexis M. — [GitHub](https://github.com/alexismr1988)
