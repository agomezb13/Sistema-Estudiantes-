package gt.edu.umg.sistema.estudiantes;

import gt.edu.umg.sistema.estudiantes.vista.FrmMenuPrincipal;
import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import java.sql.Connection;

public class SistemaEstudiantes {

    public static void main(String[] args) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            System.out.println("CONEXION EXITOSA A LA BASE DE DATOS");
        } else {
            System.out.println("ERROR: NO SE PUDO CONECTAR A LA BASE DE DATOS");
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMenuPrincipal().setVisible(true);
            }
        });
    }
}
