package simuladorliga.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase que carga parámetros de conexión desde un archivo externo .properties
 */
public class ConfigBD {
    private static final String RUTA_CONFIG = "conexion.properties";
    private static Properties propierties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(RUTA_CONFIG)) {
            propierties.load(fis);
        } catch (IOException e) {
            System.err.println("Error al cargar la configuración desde " + RUTA_CONFIG);
        }
    }

    /**
     * Devuelve el valor asociado a una clave del archivo de configuración.
     *
     * @param clave La clave a buscar (por ejemplo "servidor")
     * @return El valor correspondiente o null si no se encontró
     */
    public static String get(String clave) {
        return propierties.getProperty(clave);
    }
}
