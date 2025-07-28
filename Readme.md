# Simulador de Liga (v0.7.0)

Este proyecto es una simulación realista de una liga de fútbol, desarrollada como ejercicio de aprendizaje en Java. Incorpora principios de programación orientada a objetos, modelado de entidades futbolísticas, persistencia completa en base de datos y el inicio de una interfaz gráfica en Swing.

---

## Funcionalidades principales

- **Persistencia total en MariaDB/MySQL**  
  Clase `GestorBD` con todos los métodos CRUD (`insert`, `update`, `delete`, `select`) para cada entidad (`Liga`, `Equipo`, `Entrenador`, `Jugador`, `Partido`).

- **Reconstrucción y continuidad**  
  Permite crear una liga, simular jornadas, cerrar el programa y continuar desde el mismo punto, recuperando todos los datos (incluyendo alineaciones, entrenadores y resultados) desde la base de datos.

- **Interfaz gráfica con Swing (versión 0.8 mejorada)**  
  - Pantalla principal de bienvenida.  
  - Panel de gestión (`GestionPanel`) con múltiples opciones:  
    - Simular jornadas  
    - Mostrar clasificación  
    - Consultar plantilla de cada equipo  
    - Visualizar partidos por jornada  
    - Consultar información general de los equipos  
  - `JTable` para mostrar resultados, plantillas y clasificación con formato mejorado.
  - Estética básica aplicada (colores alternos, alineación de columnas, fuentes legibles).

- **Gestión de presupuestos desde la GUI**  
  - Selección de equipo y campo numérico para añadir o restar presupuesto.  
  - Validación para evitar entradas no numéricas o negativas.  
  - Actualización automática en la tabla visual y en la base de datos.

- **Traspasos entre equipos (interfaz gráfica)**  
  - Selección del equipo de origen, jugador y equipo de destino.  
  - Actualización tanto en memoria como en base de datos (`id_equipo`).  
  - ComboBox dinámico que actualiza automáticamente los jugadores disponibles.  
  - Confirmación visual del traspaso y recarga en tabla.

- **Importación masiva desde archivo CSV**  
  Método `importarEquiposDesdeCSV()` en `GestorFicheros`, que permite cargar equipos completos (entrenador, plantilla, presupuesto) desde archivos `.csv`.

  - Formato esperado:  
    ```
    Equipo,Presupuesto,Entrenador,EstiloEntrenador,NombreJugador,Dorsal,Posicion,Media
    ```

- **Creación de equipos y entrenadores personalizados**  
  La clase `Entrenador` incluye un atributo `EstiloEntrenador` que afecta directamente a la simulación táctica (Defensivo, Ofensivo, Posesión, Contraataque).

- **Alineaciones automáticas realistas y validadas**  
  Selección automática de 11 titulares válidos según posición, media y estilo táctico.

- **Bonificaciones y penalizaciones en la simulación**  
  Según alineación, estilo del entrenador, y diferencias de media en cada línea (defensa, medio, delantera...).

- **Cálculo automático del número de jornadas**  
  Se adapta dinámicamente al número de equipos y a si la liga es de ida o ida/vuelta.

- **Generación completa del calendario**  
  Algoritmo de emparejamientos que garantiza un calendario realista y justo, sin repeticiones indebidas.

- **Simulación avanzada de partidos (`Simulador.java`)**  
  Tácticas, estilos y valores medios se traducen en probabilidades, ocasiones y goles simulados.

- **Clasificación y estadísticas automáticas**  
  Incluye: puntos, goles a favor, goles en contra y diferencia de goles, todo visible en tabla.

- **Exportación y carga de resultados desde fichero (`GestorFicheros`)**  
  Como alternativa a la base de datos, se puede usar persistencia en archivos `.txt` (modo offline o backup).


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
- `/vista` — Interfaz Swing: `MainFrame`, `GestionPanel`, `MenuPanel`, `CrearPanel`.
- `Main.java` — Entrada principal para pruebas o demo

---

## Próximas funcionalidades

- **Botón para reiniciar la liga** completamente (reset de partidos, jornadas y puntos).
- **Aplicación del estilo visual `FlatLaf`** para modernizar la interfaz Swing.
- **Validaciones extra**: evitar plantillas inválidas, evitar presupuestos negativos, etc.
- **Paneles separados para gestión de equipos, traspasos y partidos**.
- **Reubicar la lógica del controlador para una estructura MVC total.

---

## Estado del proyecto

**Versión 0.8.0** —  
**Novedades:** Versión funcional. Gestión de presupuesto y traspasos desde la GUI, integración visual de plantillas con `JTable`.  
Todo el ciclo de simulación, visualización y modificación de datos puede hacerse desde la GUI sin necesidad de reiniciar.

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
