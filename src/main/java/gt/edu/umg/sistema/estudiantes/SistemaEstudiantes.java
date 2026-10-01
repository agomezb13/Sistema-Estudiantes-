package gt.edu.umg.sistema.estudiantes;

import gt.edu.umg.sistema.estudiantes.config.ContenedorAplicacion;
import gt.edu.umg.sistema.estudiantes.vista.FrmInicio;
import javax.swing.UIManager;

public class SistemaEstudiantes {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        java.awt.EventQueue.invokeLater(() -> {
            ContenedorAplicacion contenedor = new ContenedorAplicacion();
            FrmInicio inicio = new FrmInicio(contenedor);
            inicio.setVisible(true);
        });
    }
}
