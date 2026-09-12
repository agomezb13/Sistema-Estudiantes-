/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/**kdskdksddkfsksdfskfdskfasdkfasdkfasfkasfkasdkfad
 *
 * @author maorozco
 */
package gt.edu.umg.sistema.estudiantes;

import gt.edu.umg.sistema.estudiantes.vista.FrmMenuPrincipal;
import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import java.sql.Connection;

public class SistemaEstudiantes {

    public static void main(String[] args) {
        // 1. Probar conexión
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            System.out.println("CONEXION EXITOSA A LA BASE DE DATOS");
        } else {
            System.out.println("ERROR: NO SE PUDO CONECTAR A LA BASE DE DATOS");
        }

        // 2. Lanzar el Menú Principal
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMenuPrincipal().setVisible(true);
            }
        });
    }
}
//       JFrame ventana = new JFrame();
//        
//       ventana.setTitle("Mi primera ventana");
//       ventana.setSize(500, 300);
//       ventana.setLocationRelativeTo(null);
//       ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//       ventana.setVisible(true);
       //System.out.println("Hello World!");
       
       


