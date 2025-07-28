package simuladorliga.persistencia;

import java.sql.*;
import java.util.*;
import simuladorliga.modelo.*;

/**
 * Clase encargada de gestionar la conexión y operaciones de persistencia con la base de datos MariaDB.
 * Contiene métodos CRUD para las entidades principales: Liga, Equipo, Jugador, Entrenador y Partido.
 */
public class GestorBD {
    private static final String SERVIDOR = "localhost";
    private static final String PUERTO = "3306";
    private static final String NOMBRE_BD = "SimuladorLigas";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
    
    public static final String URL_CONEXION="jdbc:mariadb://" + SERVIDOR + ":" + PUERTO + "/" + NOMBRE_BD + "?user=" + USUARIO + "&password=" + PASSWORD;
    
    /**
     * Establece una conexión con la base de datos.
     * 
     * @return Conexión activa con la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL_CONEXION);
    }
    
    /**
     * Constructor por defecto. No realiza ninguna acción al instanciarse.
     */
    public GestorBD() {
        
    }
    
    /**
     * Crea las tablas necesarias en la base de datos si no existen.
     */
    public void inicializaBD() {
        String tablaLiga="CREATE TABLE IF NOT EXISTS LIGA (id_liga INT PRIMARY KEY AUTO_INCREMENT,nombre VARCHAR(50) NOT NULL, ida_vuelta BOOLEAN NOT NULL);";
        String tablaEntrenador = "CREATE TABLE IF NOT EXISTS ENTRENADOR (id_entrenador INT PRIMARY KEY AUTO_INCREMENT,nombre VARCHAR(50) NOT NULL, estilo VARCHAR(50) NOT NULL);";
        String tablaEquipo="CREATE TABLE IF NOT EXISTS EQUIPO (id_equipo INT PRIMARY KEY AUTO_INCREMENT,nombre VARCHAR(50) NOT NULL, id_liga INT NOT NULL, presupuesto DECIMAL(12,2) DEFAULT 0,"+
                "id_entrenador INT,  FOREIGN KEY (id_liga) REFERENCES LIGA(id_liga) ON DELETE CASCADE, FOREIGN KEY (id_entrenador) REFERENCES ENTRENADOR(id_entrenador) ON DELETE SET NULL);";
        String tablaJugador="CREATE TABLE IF NOT EXISTS JUGADOR (id_jugador INT PRIMARY KEY AUTO_INCREMENT,nombre VARCHAR(50) NOT NULL, dorsal INT NOT NULL, posicion VARCHAR(20) NOT NULL, "+
            "media INT NOT NULL, id_equipo INT NOT NULL, FOREIGN KEY (id_equipo) REFERENCES EQUIPO(id_equipo) ON DELETE CASCADE);";
        String tablaPartido="CREATE TABLE IF NOT EXISTS PARTIDO (id_partido INT AUTO_INCREMENT PRIMARY KEY, id_liga INT NOT NULL, jornada INT NOT NULL, id_equipo_local INT NOT NULL, id_equipo_visitante INT NOT NULL,"+
                "goles_local INT DEFAULT 0, goles_visitante INT DEFAULT 0, simulado BOOLEAN DEFAULT FALSE, FOREIGN KEY (id_liga) REFERENCES LIGA(id_liga) ON DELETE CASCADE,"+
                "FOREIGN KEY (id_equipo_local) REFERENCES EQUIPO(id_equipo) ON DELETE CASCADE, FOREIGN KEY (id_equipo_visitante) REFERENCES EQUIPO(id_equipo) ON DELETE CASCADE);";
        
        
        try(Connection conn = GestorBD.conectar(); Statement miSt=conn.createStatement()){
            
            // Ejecuta SIEMPRE los CREATE TABLE IF NOT EXISTS (son inofensivos si ya existen)
            miSt.executeUpdate(tablaLiga);
            miSt.executeUpdate(tablaEntrenador);
            miSt.executeUpdate(tablaEquipo);
            miSt.executeUpdate(tablaJugador);
            miSt.executeUpdate(tablaPartido);
                   
        } catch(SQLException e){
            System.err.println("Error al inicializar la conexión con la base de datos");
            e.printStackTrace();
        }
    }
    
    public void insertarLiga(Liga liga) {
        String nombre = liga.getNombre();
        int jornadas = liga.getJornadas();
        boolean ida_vuelta = liga.isIda_vuelta();
        
        String sql = "INSERT INTO LIGA (nombre, ida_vuelta ) VALUES (?, ?)";
        
        try(Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)){
            
            // Sustituye ? por el orden de los parámetros que pasamos en cada índice
            ps.setString(1, nombre);
            ps.setBoolean(2, ida_vuelta);
            
            // Ejecuta la sentencia. Devuelve el número de filas afectadas por el INSERT
            int filas = ps.executeUpdate(); // Devuelve cuántas filas se añadieron
            
            if(filas > 0) {
                System.out.println("La liga " + liga.getNombre() + " se añadió correctamente.");
            } else {
              System.out.println("No se pudo añadir la liga.");  
            }
            
              
        } catch(SQLException e){
            System.err.println("Error al insertar la liga");
            e.printStackTrace();
        }
    }
    
    /**
     * Inserta una liga completa en la base de datos, incluyendo equipos, jugadores, entrenador y partidos.
     * Se ejecuta dentro de una transacción.
     *
     * @param liga Liga a insertar
     * @return true si la inserción fue exitosa, false en caso de error
     */
    public boolean insertarLigaCompleta(Liga liga) {
        String nombre = liga.getNombre();
        boolean ida_vuelta = liga.isIda_vuelta();

        String sql = "INSERT INTO LIGA (nombre, ida_vuelta ) VALUES (?, ?)";

        try (Connection conn = GestorBD.conectar()){
            conn.setAutoCommit(false);
            
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, nombre);
                ps.setBoolean(2, ida_vuelta);

                int filas = ps.executeUpdate();
                int idLiga = -1;  //la iniciamos en negativo

                if (filas > 0) {
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            idLiga = generatedKeys.getInt(1);
                            System.out.println("ID de liga generado: " + idLiga);
                        }
                    }

                    //Mapa para asociar cada objeto Equipo a su id real
                    Map<Equipo, Integer> mapaIdsEquipos = new HashMap<>();


                    for (Equipo equipo : liga.getEquipos()) {
                        Integer idEntrenador = obtenerIdEntrenadorPorNombre(equipo.getEntrenador().getNombre());
                        if (idEntrenador == null) {
                            insertarEntrenador(equipo.getEntrenador(), conn);
                            idEntrenador = obtenerIdEntrenadorPorNombre(equipo.getEntrenador().getNombre());
                        }
                        List<Jugador> jugadores = equipo.getPlantilla();

                        int idEquipoInsertado = insertarEquipo(equipo, idLiga, idEntrenador, conn);
                        mapaIdsEquipos.put(equipo, idEquipoInsertado);

                        if (idEquipoInsertado > 0) {
                            for (Jugador jugador : jugadores) {
                                insertarJugador(jugador, idEquipoInsertado, conn);
                            }
                        }
                    }

                    // Insertar partidos usando el mapa
                    for (Partido partido : liga.getPartidos()) {
                        int idEquipoLocal = mapaIdsEquipos.get(partido.getEquipoLocal());
                        int idEquipoVisitante = mapaIdsEquipos.get(partido.getEquipoVisitante());
                        insertarPartido(partido, idLiga, idEquipoLocal, idEquipoVisitante, conn);
                    }
                    conn.commit();
                    return true;
                } else {
                    System.out.println("No se pudo añadir la liga.");
                    return false;
                }

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error al insertar la liga");
                e.printStackTrace();
                return false;
                
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public void insertarEntrenador(Entrenador entrenador) {
        String nombre = entrenador.getNombre();
        String estilo = entrenador.getEstilo().name();
        
        String sql = "INSERT INTO ENTRENADOR (nombre, estilo ) VALUES (?, ?)";
        
        try(Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)){
            
            // Sustituye ? por el orden de los parámetros que pasamos en cada índice
            ps.setString(1, nombre);
            ps.setString(2, estilo);
            
            // Ejecuta la sentencia. Devuelve el número de filas afectadas por el INSERT
            int filas = ps.executeUpdate(); // Devuelve cuántas filas se añadieron
            
            if(filas > 0) {
                System.out.println("El entrenador " + entrenador.getNombre() + " se añadió correctamente.");
            } else {
              System.out.println("No se pudo añadir el entrenador.");  
            }
            
              
        } catch(SQLException e){
            System.err.println("Error al insertar enternador");
            e.printStackTrace();
        }
    }
    
    /**
     * Inserta un entrenador reutilizando una conexión activa para transacciones
     *
     * @param entrenador Entrenador a insertar
     * @param conn Conexión activa con la base de datos
     * @throws SQLException si ocurre un error durante la inserción
     */
    public void insertarEntrenador(Entrenador entrenador, Connection conn) throws SQLException {
        String nombre = entrenador.getNombre();
        String estilo = entrenador.getEstilo().name();
        
        String sql = "INSERT INTO ENTRENADOR (nombre, estilo ) VALUES (?, ?)";
        
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            
            // Sustituye ? por el orden de los parámetros que pasamos en cada índice
            ps.setString(1, nombre);
            ps.setString(2, estilo);
            
            // Ejecuta la sentencia. Devuelve el número de filas afectadas por el INSERT
            int filas = ps.executeUpdate(); // Devuelve cuántas filas se añadieron
            
            if(filas > 0) {
                System.out.println("El entrenador " + entrenador.getNombre() + " se añadió correctamente.");
            } else {
              System.out.println("No se pudo añadir el entrenador.");  
            }
            
              
        } catch(SQLException e){
            System.err.println("Error al insertar enternador");
            e.printStackTrace();
        }
    }
    
    public int insertarEquipo(Equipo equipo, int idLiga, Integer idEntrenador) {
        String nombre = equipo.getNombre();
        double presupuesto = equipo.getPresupuesto();

        String sql = "INSERT INTO EQUIPO (nombre, id_liga, presupuesto, id_entrenador) VALUES (?, ?, ?, ?)";

        try (Connection conn = GestorBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setInt(2, idLiga);
            ps.setDouble(3, presupuesto);
            if (idEntrenador != null) {
                ps.setInt(4, idEntrenador);
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idEquipo = generatedKeys.getInt(1);
                        System.out.println("Id del equipo insertado: " + idEquipo);
                        return idEquipo;
                    }
                }
            }
            System.out.println("No se pudo añadir el equipo.");
        } catch (SQLException e) {
            System.err.println("Error al insertar equipo");
            e.printStackTrace();
        }
        return -1; // Si algo falla, devuelve -1
    }
    
    /**
    * Inserta un equipo reutilizando una conexión activa para transacciones.
    *
    * @param equipo Equipo a insertar
    * @param idLiga ID de la liga
    * @param idEntrenador ID del entrenador
    * @param conn Conexión activa
    * @return ID del equipo insertado o -1 si falló
    */
    public int insertarEquipo(Equipo equipo, int idLiga, Integer idEntrenador, Connection conn) throws SQLException{
        String nombre = equipo.getNombre();
        double presupuesto = equipo.getPresupuesto();

        String sql = "INSERT INTO EQUIPO (nombre, id_liga, presupuesto, id_entrenador) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setInt(2, idLiga);
            ps.setDouble(3, presupuesto);
            if (idEntrenador != null) {
                ps.setInt(4, idEntrenador);
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idEquipo = generatedKeys.getInt(1);
                        System.out.println("Id del equipo insertado: " + idEquipo);
                        return idEquipo;
                    }
                }
            }
            System.out.println("No se pudo añadir el equipo.");
        } catch (SQLException e) {
            System.err.println("Error al insertar equipo");
            e.printStackTrace();
        }
        return -1; // Si algo falla, devuelve -1
    }
    
    public void insertarJugador(Jugador jugador, int idEquipo) {
        String nombre = jugador.getNombre();
        int dorsal = jugador.getDorsal();
        String posicion = jugador.getPosicion().name();
        int media = jugador.getMedia();

        String sql = "INSERT INTO JUGADOR (nombre, dorsal, posicion, media, id_equipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, dorsal);
            ps.setString(3, posicion);
            ps.setInt(4, media);
            ps.setInt(5, idEquipo);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("El jugador " + jugador.getNombre() + " se añadió correctamente.");
            } else {
                System.out.println("No se pudo añadir el jugador.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar jugador");
            e.printStackTrace();
        }
    }
    
    /**
    * Inserta un jugador reutilizando una conexión activa para transacciones.
    *
    * @param jugador Jugador a insertar
    * @param idEquipo ID del equipo
    * @param conn Conexión activa
    * @throws SQLException si ocurre un error
    */
    public void insertarJugador(Jugador jugador, int idEquipo, Connection conn) throws SQLException {
        String nombre = jugador.getNombre();
        int dorsal = jugador.getDorsal();
        String posicion = jugador.getPosicion().name();
        int media = jugador.getMedia();

        String sql = "INSERT INTO JUGADOR (nombre, dorsal, posicion, media, id_equipo) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, dorsal);
            ps.setString(3, posicion);
            ps.setInt(4, media);
            ps.setInt(5, idEquipo);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("El jugador " + jugador.getNombre() + " se añadió correctamente.");
            } else {
                System.out.println("No se pudo añadir el jugador.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar jugador");
            e.printStackTrace();
        }
    }
    
    public void insertarPartido(Partido partido, int idLiga, int idEquipoLocal, int idEquipoVisitante) {
        int jornada = partido.getJornada();
        int golesLocal = partido.getGolesLocal();
        int golesVisitante = partido.getGolesVisitante();
        boolean simulado = partido.isSimulado();

        String sql = "INSERT INTO PARTIDO (id_liga, jornada, id_equipo_local, id_equipo_visitante, goles_local, goles_visitante, simulado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLiga);
            ps.setInt(2, jornada);
            ps.setInt(3, idEquipoLocal);
            ps.setInt(4, idEquipoVisitante);
            ps.setInt(5, golesLocal);
            ps.setInt(6, golesVisitante);
            ps.setBoolean(7, simulado);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("El partido se añadió correctamente.");
            } else {
                System.out.println("No se pudo añadir el partido.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar partido");
            e.printStackTrace();
        }
    }
    
    /**
    * Inserta un partido reutilizando una conexión activa para transacciones.
    *
    * @param partido Partido a insertar
    * @param idLiga ID de la liga
    * @param idEquipoLocal ID del equipo local
    * @param idEquipoVisitante ID del equipo visitante
    * @param conn Conexión activa
    */
    public void insertarPartido(Partido partido, int idLiga, int idEquipoLocal, int idEquipoVisitante, Connection conn) throws SQLException {
        int jornada = partido.getJornada();
        int golesLocal = partido.getGolesLocal();
        int golesVisitante = partido.getGolesVisitante();
        boolean simulado = partido.isSimulado();

        String sql = "INSERT INTO PARTIDO (id_liga, jornada, id_equipo_local, id_equipo_visitante, goles_local, goles_visitante, simulado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLiga);
            ps.setInt(2, jornada);
            ps.setInt(3, idEquipoLocal);
            ps.setInt(4, idEquipoVisitante);
            ps.setInt(5, golesLocal);
            ps.setInt(6, golesVisitante);
            ps.setBoolean(7, simulado);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("El partido se añadió correctamente.");
            } else {
                System.out.println("No se pudo añadir el partido.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar partido");
            e.printStackTrace();
        }
    }
    
    /**
    * Obtiene el ID de una liga por su nombre.
    *
    * @param nombre Nombre de la liga
    * @return ID correspondiente o null si no se encuentra
    */
    public  Integer obtenerIdLigaPorNombre(String nombre) {
        String sql = "SELECT id_liga FROM LIGA WHERE nombre = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_liga");
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda de la liga.");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
    * Obtiene el ID de un equipo por su nombre.
    *
    * @param nombre Nombre del equipo
    * @return ID correspondiente o null si no se encuentra
    */
    public Integer obtenerIdEquipoPorNombre(String nombre) {
        String sql = "SELECT id_equipo FROM EQUIPO WHERE nombre = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_equipo");
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda del equipo.");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
    * Obtiene todas las jornadas distintas asociadas a una liga.
    *
    * @param idLiga ID de la liga
    * @return Lista de jornadas ordenadas
    */
    public List<Integer> obtenerJornadasDeLiga(int idLiga) {
        List<Integer> jornadas = new ArrayList<>();
        String sql = "SELECT DISTINCT jornada FROM PARTIDO WHERE id_liga = ? ORDER BY jornada";
        try (Connection conn = GestorBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLiga);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jornadas.add(rs.getInt("jornada"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las jornadas de la liga.");
            e.printStackTrace();
        }
        return jornadas;
    }
    
    /**
    * Recupera los partidos de una jornada concreta.
    *
    * @param idLiga ID de la liga
    * @param jornada Número de la jornada
    * @return Lista de partidos
    */
    public List<Partido> obtenerPartidosDeJornada(int idLiga, int jornada) {
        List<Partido> partidos = new ArrayList<>();
        String sql = "SELECT * FROM PARTIDO WHERE id_liga = ? AND jornada = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLiga);
            ps.setInt(2, jornada);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Equipo local = recuperarEquipoPorId(rs.getInt("id_equipo_local"));
                Equipo visitante = recuperarEquipoPorId(rs.getInt("id_equipo_visitante"));
                Partido partido = new Partido(
                    local, visitante, rs.getInt("goles_local"), rs.getInt("goles_visitante"), rs.getInt("jornada")
                );
                partido.setIdPartido(rs.getInt("id_partido")); // <-- ASIGNA idPartido
                partido.setSimulado(rs.getBoolean("simulado"));
                partidos.add(partido);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los partidos de la jornada.");
            e.printStackTrace();
        }
        return partidos;
    }



    public Integer obtenerIdEntrenadorPorNombre(String nombre) {
        String sql = "SELECT id_entrenador FROM ENTRENADOR WHERE nombre = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_entrenador");
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda del entrenador.");
            e.printStackTrace();
        }
        return null;
    }
    
    public Liga recuperarLigaPorId(int idLiga){
            String sql = "SELECT * FROM LIGA WHERE id_liga = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLiga);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Liga liga = new Liga(rs.getString("nombre"), rs.getBoolean("ida_vuelta"));
                return liga;
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar la liga.");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
    * Recupera todos los equipos asociados a una liga específica.
    *
    * @param idLiga ID de la liga
    * @return Lista de equipos con plantilla y entrenador asociados
    */
    public List<Equipo> recuperarEquiposPorLiga(int idLiga){
        List<Equipo> equipos=new ArrayList();
        String consultaEquipo = "SELECT * FROM EQUIPO WHERE id_liga = ?";
        int idEquipo=0;
               
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(consultaEquipo)) {
            ps.setInt(1, idLiga);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idEquipo=rs.getInt("id_equipo");
                Equipo equipo=null;
                List<Jugador> plantilla = new ArrayList<>();
                Entrenador entrenador = null;
                entrenador=recuperarEntrenadorPorId(rs.getInt("id_entrenador"));
                plantilla = recuperarJugadoresPorEquipo(idEquipo);
                equipo = new Equipo(rs.getString("nombre"));
                equipo.setId(idEquipo);
                equipo.setPresupuesto(rs.getDouble("presupuesto"));
                equipo.setPlantilla(plantilla);
                equipo.setEntrenador(entrenador);
                equipos.add(equipo);
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda del equipo.");
            e.printStackTrace();
        }
       
        return equipos;
    }
    
    public Entrenador recuperarEntrenadorPorId(int idEntrenador){
        String sql = "SELECT * FROM ENTRENADOR WHERE id_entrenador = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEntrenador);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Entrenador entrenador = new Entrenador(rs.getString("nombre"), EstiloEntrenador.valueOf(rs.getString("estilo")));
                return entrenador;
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda del entrenador.");
            e.printStackTrace();
        }
        return null;
    }   
    
    public Equipo recuperarEquipoPorId(int idEquipo){
        Equipo equipo=null;
        String consultaEquipo = "SELECT * FROM EQUIPO WHERE id_equipo = ?";
               
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(consultaEquipo)) {
            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                List<Jugador> plantilla = new ArrayList<>();
                Entrenador entrenador = null;
                entrenador=recuperarEntrenadorPorId(rs.getInt("id_entrenador"));
                plantilla = recuperarJugadoresPorEquipo(idEquipo);
                equipo = new Equipo(rs.getString("nombre"));
                equipo.setId(rs.getInt("id_equipo"));
                equipo.setPresupuesto(rs.getDouble("presupuesto"));
                equipo.setPlantilla(plantilla);
                equipo.setEntrenador(entrenador);
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda del equipo.");
            e.printStackTrace();
        }
       
        return equipo;
    }
    
    /**
     * Recupera una liga completa desde la base de datos, con equipos, entrenadores, jugadores y partidos ya asociados.
     * 
     * @param idLiga ID de la liga a recuperar
     * @return Objeto Liga reconstruido completamente, o null si no se encuentra
     */
    public Liga recuperarLigaCompleta(int idLiga) {
        Liga ligaRecuperada = recuperarLigaPorId(idLiga);
        if (ligaRecuperada == null) return null;
        
        List<Equipo> equipos = recuperarEquiposPorLiga(idLiga);
        List<Partido> partidos = recuperarPartidosPorLiga(idLiga, equipos);
        
        
        ligaRecuperada.setEquipos(equipos);
        ligaRecuperada.setPartidos(partidos);
        
        return ligaRecuperada;
    }

    
    public List<Liga> muestraTodasLigas() {
        List<Liga> ligas = new ArrayList<>();
        String sentencia = "SELECT * FROM LIGA";
        
        try(Connection conn = GestorBD.conectar(); Statement miSt=conn.createStatement()){
            
            //Creo Resulset y ejecuto la sentencia
            ResultSet miRs=miSt.executeQuery(sentencia);
            
            // LEER EL RESULTSET
            while(miRs.next()){
                Liga liga = new Liga(miRs.getString("nombre"), miRs.getBoolean("ida_vuelta"));
                ligas.add(liga);
            }
                 
        } catch(SQLException e){
            System.err.println("Error al mostrar la tabla de las ligas.");
            e.printStackTrace();
        }
        
        return ligas;
    }
    
    public List<String> obtenerNombresLigas() {
        List<String> nombresLigas = new ArrayList<>();
        String sentencia = "SELECT nombre FROM LIGA ORDER BY nombre";
        try (Connection conn = GestorBD.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sentencia)) {
            while (rs.next()) {
                nombresLigas.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los nombres de las ligas.");
            e.printStackTrace();
        }
        return nombresLigas;
    }
    
    

    public List<Equipo> muestraTodosEquipos() {
        List<Equipo> equipos = new ArrayList<>();
        String sentencia = "SELECT * FROM EQUIPO";
        
        try(Connection conn = GestorBD.conectar(); Statement miSt=conn.createStatement()){
            
            ResultSet miRs=miSt.executeQuery(sentencia);
            
            while(miRs.next()){
                Equipo equipo= new Equipo(miRs.getString("nombre"));
                equipos.add(equipo);
            }
                 
        } catch(SQLException e){
            System.err.println("Error al mostrar la tabla de equipos.");
            e.printStackTrace();
        }
        
        return equipos;
    }
    
    /**
    * Recupera los jugadores asociados a un equipo.
    *
    * @param idEquipo ID del equipo
    * @return Lista de jugadores
    */
    public List<Jugador> recuperarJugadoresPorEquipo(int idEquipo){
        List<Jugador> plantilla = new ArrayList<>();
        String sql = "SELECT * FROM JUGADOR WHERE id_equipo = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jugador jugador = new Jugador(rs.getString("nombre"), rs.getInt("dorsal"), Posicion.valueOf(rs.getString("posicion")), rs.getInt("media"));
                jugador.setId(rs.getInt("id_jugador"));
                plantilla.add(jugador);
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda de los jugadores.");
            e.printStackTrace();
        }
        return plantilla;
    }
    
    public List<Entrenador> recuperarTodosEntrenadores(){
        List<Entrenador> entrenadores = new ArrayList<>();
        String sql = "SELECT * FROM ENTRENADOR";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Entrenador entrenador = new Entrenador(rs.getString("nombre"), EstiloEntrenador.valueOf(rs.getString("estilo")));
                entrenadores.add(entrenador);
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda de los entrenadores.");
            e.printStackTrace();
        }
        return entrenadores; 
    }
    
    public List<Partido> recuperarPartidosPorLiga(int idLiga){
        List<Partido> partidos = new ArrayList<>();
        String consultaLiga = "SELECT * FROM PARTIDO WHERE id_liga = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(consultaLiga)) {
            ps.setInt(1, idLiga);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Equipo equipoLocal = recuperarEquipoPorId(rs.getInt("id_equipo_local"));
                Equipo equipoVisitante = recuperarEquipoPorId(rs.getInt("id_equipo_visitante"));
                Partido partido = new Partido(equipoLocal, equipoVisitante, rs.getInt("goles_local"), rs.getInt("goles_visitante"), rs.getInt("jornada"));
                partido.setIdPartido(rs.getInt("id_partido"));
                partido.setSimulado(rs.getBoolean("simulado"));
                partidos.add(partido);
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda de los partidos.");
            e.printStackTrace();
        }
        return partidos; 
    }
    
    /**
    * Recupera todos los partidos de una liga utilizando los objetos equipo ya reconstruidos (importante para mantener referencias).
    *
    * @param idLiga ID de la liga
    * @param equipos Lista de equipos ya reconstruidos
    * @return Lista de partidos con instancias coherentes de equipo
    */
    public List<Partido> recuperarPartidosPorLiga(int idLiga, List<Equipo> equipos) {
        List<Partido> partidos = new ArrayList<>();
        String consultaLiga = "SELECT * FROM PARTIDO WHERE id_liga = ?";

        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(consultaLiga)) {
            ps.setInt(1, idLiga);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idLocal = rs.getInt("id_equipo_local");
                int idVisitante = rs.getInt("id_equipo_visitante");

                // Buscar los equipos ya existentes por ID
                Equipo equipoLocal = buscarEquipoPorId(equipos, idLocal);
                Equipo equipoVisitante = buscarEquipoPorId(equipos, idVisitante);

                Partido partido = new Partido(equipoLocal, equipoVisitante, rs.getInt("goles_local"), rs.getInt("goles_visitante"), rs.getInt("jornada"));
                partido.setIdPartido(rs.getInt("id_partido"));
                partido.setSimulado(rs.getBoolean("simulado"));
                partidos.add(partido);
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda de los partidos.");
            e.printStackTrace();
        }
        return partidos;
    }
 
    private Equipo buscarEquipoPorId(List<Equipo> equipos, int id) {
        for (Equipo equipo : equipos) {
            if (equipo.getId() == id) return equipo;
        }
        return null;
    }


    
    // Actualiza LIGA (nombre)
    public void updateLiga(int idLiga, String nuevoNombre) {
        String sql = "UPDATE LIGA SET nombre = ? WHERE id_liga = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, idLiga);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("La liga con id " + idLiga + " fue modificada correctamente");
            } else {
                System.out.println("No existe la liga indicada con id: " + idLiga);
            }
        } catch (SQLException e) {
            System.err.println("Error en la modificación de la liga");
            e.printStackTrace();
        }
    }

    // Actualiza EQUIPO (nombre, presupuesto, entrenador, liga)
    public void updateEquipo(int idEquipo, String nuevoNombre, double nuevoPresupuesto, Integer idEntrenador, int idLiga) {
        String sql = "UPDATE EQUIPO SET nombre = ?, presupuesto = ?, id_entrenador = ?, id_liga = ? WHERE id_equipo = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setDouble(2, nuevoPresupuesto);
            // Si el entrenador es null, ponemos NULL en la base de datos
            if (idEntrenador != null) {
                ps.setInt(3, idEntrenador);
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setInt(4, idLiga);
            ps.setInt(5, idEquipo);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El equipo con id " + idEquipo + " fue modificado correctamente");
            } else {
                System.out.println("No existe ningún equipo con id: " + idEquipo);
            }
        } catch (SQLException e) {
            System.err.println("Error en la modificación del equipo");
            e.printStackTrace();
        }
    }

    // Actualiza ENTRENADOR (nombre y estilo)
    public void updateEntrenador(int idEntrenador, String nuevoNombre, String nuevoEstilo) {
        String sql = "UPDATE ENTRENADOR SET nombre = ?, estilo = ? WHERE id_entrenador = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setString(2, nuevoEstilo);
            ps.setInt(3, idEntrenador);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El entrenador con id " + idEntrenador + " fue modificado correctamente");
            } else {
                System.out.println("No existe ningún entrenador con id: " + idEntrenador);
            }
        } catch (SQLException e) {
            System.err.println("Error en la modificación del entrenador");
            e.printStackTrace();
        }
    }

    // Actualiza JUGADOR (nombre, dorsal, posicion, media, equipo)
    public void updateJugador(int idJugador, String nuevoNombre, int nuevoDorsal, String nuevaPosicion, int nuevaMedia, int idEquipo) {
        String sql = "UPDATE JUGADOR SET nombre = ?, dorsal = ?, posicion = ?, media = ?, id_equipo = ? WHERE id_jugador = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, nuevoDorsal);
            ps.setString(3, nuevaPosicion);
            ps.setInt(4, nuevaMedia);
            ps.setInt(5, idEquipo);
            ps.setInt(6, idJugador);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El jugador con id " + idJugador + " fue modificado correctamente");
            } else {
                System.out.println("No existe ningún jugador con id: " + idJugador);
            }
        } catch (SQLException e) {
            System.err.println("Error en la modificación del jugador");
            e.printStackTrace();
        }
    }
    
        public void actualizarIdJugador(int idJugador, int idEquipo) {
        String sql = "UPDATE JUGADOR SET id_equipo = ? WHERE id_jugador = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setInt(2, idJugador);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El jugador con id " + idJugador + " fue modificado correctamente");
            } else {
                System.out.println("No existe ningún jugador con id: " + idJugador);
            }
        } catch (SQLException e) {
            System.err.println("Error en la modificación del jugador");
            e.printStackTrace();
        }
    }

    // Actualiza PARTIDO (goles, simulado, jornada, equipos)
    public void updatePartido(int idPartido, int golesLocal, int golesVisitante, boolean simulado, int jornada, int idEquipoLocal, int idEquipoVisitante) {
        String sql = "UPDATE PARTIDO SET goles_local = ?, goles_visitante = ?, simulado = ?, jornada = ?, id_equipo_local = ?, id_equipo_visitante = ? WHERE id_partido = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, golesLocal);
            ps.setInt(2, golesVisitante);
            ps.setBoolean(3, simulado);
            ps.setInt(4, jornada);
            ps.setInt(5, idEquipoLocal);
            ps.setInt(6, idEquipoVisitante);
            ps.setInt(7, idPartido);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El partido con id " + idPartido + " fue modificado correctamente");
            } else {
                System.out.println("No existe ningún partido con id: " + idPartido);
            }
        } catch (SQLException e) {
            System.err.println("Error en la modificación del partido");
            e.printStackTrace();
        }
    }
    
    public void deleteLiga(int idLiga) {
        String sql = "DELETE FROM LIGA WHERE id_liga = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLiga);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("La liga con id " + idLiga + " fue eliminada correctamente");
            } else {
                System.out.println("No existe ninguna liga con id: " + idLiga);
            }
        } catch (SQLException e) {
            System.err.println("Error en la eliminación de la liga");
            e.printStackTrace();
        }
    }

    public void deleteEquipo(int idEquipo) {
        String sql = "DELETE FROM EQUIPO WHERE id_equipo = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El equipo con id " + idEquipo + " fue eliminado correctamente");
            } else {
                System.out.println("No existe ningún equipo con id: " + idEquipo);
            }
        } catch (SQLException e) {
            System.err.println("Error en la eliminación del equipo");
            e.printStackTrace();
        }
    }

    public void deleteJugador(int idJugador) {
        String sql = "DELETE FROM JUGADOR WHERE id_jugador = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El jugador con id " + idJugador + " fue eliminado correctamente");
            } else {
                System.out.println("No existe ningún jugador con id: " + idJugador);
            }
        } catch (SQLException e) {
            System.err.println("Error en la eliminación del jugador");
            e.printStackTrace();
        }
    }

    public void deleteEntrenador(int idEntrenador) {
        String sql = "DELETE FROM ENTRENADOR WHERE id_entrenador = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEntrenador);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El entrenador con id " + idEntrenador + " fue eliminado correctamente");
            } else {
                System.out.println("No existe ningún entrenador con id: " + idEntrenador);
            }
        } catch (SQLException e) {
            System.err.println("Error en la eliminación del entrenador");
            e.printStackTrace();
        }
    }

    public void deletePartido(int idPartido) {
        String sql = "DELETE FROM PARTIDO WHERE id_partido = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPartido);
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                System.out.println("El partido con id " + idPartido + " fue eliminado correctamente");
            } else {
                System.out.println("No existe ningún partido con id: " + idPartido);
            }
        } catch (SQLException e) {
            System.err.println("Error en la eliminación del partido");
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza el presupuesto de un equipo específico.
     * 
     * @param idEquipo ID del equipo a actualizar
     * @param nuevoPresupuesto Nuevo valor del presupuesto
     * @return true si la operación fue exitosa, false si no se modificó nada o falló
     */
    public boolean updatePresupuestoEquipo(int idEquipo, double nuevoPresupuesto) {
        String sql = "UPDATE equipo SET presupuesto = ? WHERE id_equipo = ?";
        try (Connection conn = GestorBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nuevoPresupuesto);
            ps.setInt(2, idEquipo);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el presupuesto del equipo con ID: " + idEquipo);
            e.printStackTrace();
            return false;
        }
}

}
