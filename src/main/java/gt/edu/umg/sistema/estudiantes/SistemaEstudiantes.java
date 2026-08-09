/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gt.edu.umg.sistema.estudiantes;

import gt.edu.umg.sistema.estudiantes.vista.FrmEstudiante;
import javax.swing.JFrame;
import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import java.sql.Connection;

/**kdskdksddkfsksdfskfdskfasdkfasdkfasfkasfkasdkfad
 *
 * @author maorozco
 */
public class SistemaEstudiantes {

    public static void main(String[] args) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            System.out.println("CONEXION EXITOSA");
        }else {
            System.out.println("NO SE PUDO CONECTAR");
        }
        FrmEstudiante ventana = new FrmEstudiante();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
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
       
       


