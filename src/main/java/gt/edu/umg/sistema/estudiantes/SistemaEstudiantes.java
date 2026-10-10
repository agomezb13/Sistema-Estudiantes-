package gt.edu.umg.sistema.estudiantes;

import gt.edu.umg.sistema.estudiantes.config.ContenedorAplicacion;
import gt.edu.umg.sistema.estudiantes.vista.FrmInicio;
import javax.swing.UIManager;

/**
 * Clase principal del sistema.
 * Punto de entrada de la aplicacion (Metodo main).
 * 
 * Flujo de inicio:
 * 1. Configura el aspecto visual (LookAndFeel Nimbus).
 * 2. Instancia el ContenedorAplicacion (que inicializa DAOs y Controladores).
 * 3. Muestra la pantalla inicial (FrmInicio).
 */
public class SistemaEstudiantes {

    public static void main(String[] args) {
        // Establecer apariencia visual moderna (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        // Iniciar interfaz grafica en el hilo de eventos de Swing
        java.awt.EventQueue.invokeLater(() -> {
            ContenedorAplicacion contenedor = new ContenedorAplicacion();
            FrmInicio inicio = new FrmInicio(contenedor);
            inicio.setVisible(true);
        });
    }
}
