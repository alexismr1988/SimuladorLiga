# Simulador de Liga (v0.4.0)

Este proyecto es una simulación realista de una liga de fútbol desde consola, orientado al aprendizaje de programación en Java mediante principios de programación orientada a objetos y modelado táctico.

## Funcionalidades principales

-Persistencia completa en base de datos MariaDB/MySQL:
  Nueva clase GestorBD con todos los métodos CRUD (insert, update, delete, select) para las entidades principales. 
- Creación de equipos con jugadores y entrenadores personalizados.
- Implementación de la clase `Entrenador` con un atributo `EstiloEntrenador` (defensivo, ofensivo, posesión, contraataque).
- Alineaciones válidas con restricciones realistas por línea (defensas, medios, delanteros).
- Bonus o penalización en la simulación si el estilo del entrenador encaja o no con la táctica utilizada.
- Asignación y gestión de presupuesto por equipo (`presupuesto` en la clase `Equipo`).
-Cálculo automático del número de jornadas:
  Eliminado el atributo jornadas en la clase Liga. Ahora el número de jornadas se calcula dinámicamente según el número de equipos y si hay ida y vuelta.
- Generación de calendario (ida y vuelta opcional).
- Simulación detallada de partidos con lógica avanzada:
  - Cálculo de ocasiones y probabilidad de gol influido por:
    - Calidad media por líneas (defensas, medios, delanteros).
    - Formación táctica (ej. 433, 442, etc.).
    - Estilo del entrenador y su compatibilidad con la táctica.
    - Impacto del portero sobre la efectividad rival.
- Clasificación automática y estadísticas acumuladas por equipo.
- Exportación y carga de resultados desde fichero.

## Tecnologías utilizadas

- Java 17
- NetBeans IDE
- Programación orientada a objetos (POO)

## Estructura del proyecto

- `modelo/` — Clases principales: `Equipo`, `Jugador`, `Entrenador`, `EstiloEntrenador`, `Partido`, `Alineacion`, `Liga`, `Posicion`
- `servicio/` — Lógica principal de simulación (`Simulador.java`)
- `persistencia/` — Lectura/escritura de datos en fichero
- `Main.java` — Entrada de ejecución para pruebas o demostración

## Próximas funcionalidades

- Registro de goleadores y estadísticas individuales.
- Persistencia completa del estado de la liga.
- Interfaz gráfica (Swing o JavaFX).
- Exportación a formatos CSV o JSON.

## Estado del proyecto

Versión 0.3.0 — Lógica de simulación avanzada, clases nuevas (`Entrenador`, `EstiloEntrenador`, presupuesto en `Equipo`), integración táctica y preparación para extensiones futuras.

## Autor

Alexis M. — [GitHub](https://github.com/alexismr1988)
