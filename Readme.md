# Simulador de Liga (v0.7.0)

Este proyecto es una simulación realista de una liga de fútbol, desarrollada como ejercicio de aprendizaje en Java. Incorpora principios de programación orientada a objetos, modelado de entidades futbolísticas, persistencia completa en base de datos y el inicio de una interfaz gráfica en Swing.

---

## Funcionalidades principales

- **Persistencia total en MariaDB/MySQL**  
  Clase `GestorBD` con todos los métodos CRUD (insert, update, delete, select) para cada entidad (`Liga`, `Equipo`, `Entrenador`, `Jugador`, `Partido`).
- **Reconstrucción y continuidad:**  
  Permite crear una liga, simular jornadas, cerrar el programa y continuar desde el mismo punto recuperando todos los datos de la base de datos.
- **Interfaz gráfica básica (Swing):**  
  - Pantalla principal de bienvenida.  
  - Gestión de equipos y selección de jornadas a simular desde interfaz gráfica.  
  - Selección dinámica de jornadas disponibles (comboBox) y simulación directa desde la GUI.
- **Simulación y actualización de resultados desde la interfaz:**  
  Ahora la simulación de jornadas y la actualización de resultados se hace de forma visual y amigable para el usuario.
- **Reconstrucción visual del estado de la liga:**  
  Puedes cerrar el programa y reabrirlo; los partidos/jornadas se mantienen y pueden visualizarse y simularse por la interfaz.
- **Importación masiva desde archivo CSV**  
  Método `importarEquiposDesdeCSV()` en `GestorFicheros`, para crear equipos completos desde `.csv` (incluyendo entrenador, presupuesto y plantilla).
    - Formato esperado:  
      `Equipo,Presupuesto,Entrenador,EstiloEntrenador,NombreJugador,Dorsal,Posicion,Media`
- **Creación de equipos y entrenadores personalizados**  
  Clase `Entrenador` con atributo `EstiloEntrenador` (Defensivo, Ofensivo, Posesión, Contraataque).
- **Alineaciones realistas y validadas**  
  Restricciones por posición y táctica (mínimo/máximo por línea).
- **Bonificaciones y penalizaciones en la simulación**  
  La táctica utilizada y la compatibilidad con el estilo del entrenador afectan al resultado del partido.
- **Gestión de presupuestos por equipo**
- **Cálculo automático del número de jornadas**  
  Según equipos y si hay ida/vuelta.
- **Generación automática del calendario**  
  Liga de ida o ida y vuelta.
- **Simulación avanzada de partidos**
- **Clasificación y estadísticas automáticas**  
  Resultados, goles, diferencia de goles, puntos.
- **Exportación y carga de resultados desde fichero**  
  Alternativa a la BD: persistencia usando archivos `.txt`.


---

## Lógica de simulación de partidos (`Simulador.java`)

Cada partido simulado tiene en cuenta los siguientes factores:

- **Ventaja Local:** El equipo local recibe un 10% extra en probabilidad de gol por ocasión.
- **Probabilidad Base de Gol:**  
  - Local: 18% por ocasión  
  - Visitante: 15% por ocasión  
- **Ocasiones de gol iniciales:** 12 para cada equipo, modificadas según la táctica y las medias.
- **Modificadores por alineación:**  
  - Menos de 4 defensas: el rival gana +15% probabilidad de gol.  
  - Más de 4 defensas: el rival pierde -15% probabilidad de gol.  
  - Medios: 3 medios restan 2 ocasiones, 5 medios suman 4, 6 medios suman 6.  
  - Delanteros: 1 delantero -15% probabilidad propia, 3 delanteros +10%.
- **Media de líneas (portero, defensas, medios, delanteros):**  
  - Mejor portero: reduce la probabilidad rival.
  - Mejor defensa: penaliza ocasiones rivales.
  - Mejor medio: suma ocasiones propias.
  - Mejor delantera: aumenta probabilidad propia.
- **Estilo del entrenador:**  
  - Si el estilo es compatible con la táctica,  
    - DEFENSIVO: penaliza al rival (-5%).
    - OFENSIVO/POSESIÓN/CONTRAATAQUE: bonifica probabilidad propia (+5%).
- **Simulación probabilística:**  
  - Cada ocasión se simula como un "disparo": si `Math.random() < ProbabilidadGol`, se marca gol.
- **Actualización automática:**  
  - Goles, puntos y estadísticas se actualizan tras cada partido.

*Esto permite que la táctica, las medias y la coherencia estratégica tengan impacto real en el resultado, logrando una simulación variada y creíble.*

---

## Tecnologías utilizadas

- **Java 17**
- **NetBeans IDE**
- **JDBC + MariaDB**
- **POO (Programación Orientada a Objetos)**

---

## Dependencias externas

- OpenCSV (`opencsv-5.7.1.jar`)
- Apache Commons Lang (`commons-lang3-3.12.0.jar`)
- MariaDB Java Client (`mariadb-java-client-3.5.3.jar`)

---

## Estructura del proyecto

- `/modelo` — Clases del dominio: `Equipo`, `Jugador`, `Entrenador`, etc.
- `/servicio` — Lógica principal: `Simulador.java`
- `/persistencia` — Persistencia en ficheros y base de datos: `GestorFicheros`, `GestorBD`
- `/vista` — Interfaz gráfica Swing (pantallas principales, gestión de equipos, simulación de jornadas)
- `Main.java` — Entrada principal para pruebas o demo

---

## Próximas funcionalidades

- Mejorar la interfaz de gestión y consulta de ligas y jornadas simuladas.
- Añadir edición/eliminación de ligas y equipos desde la GUI.
- Documentar la arquitectura de persistencia y la lógica de simulación en un archivo aparte (`ARQUITECTURA.md`).

---

## Estado del proyecto

Versión **0.7.0** —  
**Novedad:** Primeras pantallas funcionales en Swing.  
Ya se puede gestionar equipos, seleccionar jornadas y simular partidos desde la interfaz gráfica.  
La reconstrucción de partidos, equipos y clasificación desde la base de datos es completamente funcional.

### Novedades / Logros recientes

- **Persistencia total implementada**: 
  - Ahora toda la información de la liga (equipos, jugadores, entrenadores y partidos) se almacena y recupera desde la base de datos MariaDB/MySQL.
  - El calendario de partidos se guarda correctamente, asignando cada partido a sus equipos y jornadas reales mediante sus IDs en la base de datos.

- **Creación atómica de Ligas**:
  - El proceso de inserción de una nueva liga y todos sus elementos asociados (equipos, jugadores, entrenadores, partidos) se realiza en una única transacción. Si ocurre un error en cualquier paso, **no se añade nada** (rollback automático), garantizando la integridad de los datos.

- **Simulación continua**:
  - Al cerrar y volver a abrir el simulador, es posible continuar la simulación exactamente desde la jornada pendiente, gracias a la persistencia real y la recuperación completa de objetos desde la base de datos.

- **Control de errores mejorado**:
  - Ahora la interfaz informa correctamente si ocurre algún error durante la creación de una liga, permitiendo al usuario volver a intentarlo sin dejar datos inconsistentes.

- **Flujo de trabajo probado y estable**:
  - Probado el flujo: *crear liga* → *simular jornadas* → *persistir y reanudar* con múltiples equipos y jornadas.
  - Todos los cambios quedan reflejados automáticamente en la base de datos y la interfaz Swing.

---


## Autor

**Alexis M.** — [GitHub](https://github.com/alexismr1988)
