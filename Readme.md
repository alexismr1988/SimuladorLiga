# Simulador de Liga (v0.2)

Este proyecto es una simulación básica de una liga de fútbol en consola. Permite:

- Crear equipos con jugadores
- Definir alineaciones válidas
- Generar un calendario de partidos (ida y vuelta opcional)
- Simular jornadas y partidos
- Calcular estadísticas de la liga (goles, puntos, diferencia de goles)
- Generar una clasificación final
- Exportar los resultados a fichero
- Cargar resultados desde fichero
- Calcular y actualizar clasificación automáticamente desde los resultados

## Tecnologías utilizadas

- Java 17 (nivel de fuente)
- NetBeans IDE
- Programación orientada a objetos (POO)

## Estructura del proyecto

- `modelo/` — Clases principales: `Equipo`, `Jugador`, `Partido`, `Alineacion`, `Liga`, `Posicion`, etc.
- `servicio/` — Lógica de simulación (clase `Simulador`)
- `persistencia/` — Lectura y escritura de datos en ficheros
- `Main.java` — Punto de entrada que ejecuta la aplicación

## Próximas funcionalidades

- [ ] Registro de goleadores por partido
- [ ] Clase `Entrenador` y presupuesto por equipo
- [ ] Persistencia extendida (guardar toda la liga)
- [ ] Interfaz gráfica (GUI)
- [ ] Exportación de estadísticas a formato CSV/JSON

## Estado del proyecto

🟢 Versión **0.2** — Añadida persistencia de resultados, cálculo automático de estadísticas, y estructura modular de clases.

## Autor

Alexis M. — [GitHub](https://github.com/alexismr1988)

