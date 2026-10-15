import org.iesalandalus.programacion.utilidades.Entrada;

import java.io.IOException;
import java.io.OutputStream;


public class Lector {
    public static void main(String[] args) {
        System.out.print("Introduce un número entero: ");
        int numero = Entrada.entero();

        ProcessBuilder constructor = new ProcessBuilder(
                "java", "-cp", System.getProperty("java.class.path"), "Escritor");
        constructor.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        constructor.redirectError(ProcessBuilder.Redirect.INHERIT);

        try {
            Process escritor = constructor.start();
            OutputStream entradaEscritor = escritor.getOutputStream();
            entradaEscritor.write((numero + "\n").getBytes());
            entradaEscritor.flush();
            entradaEscritor.close();

            int codigoSalida = escritor.waitFor();
            if (codigoSalida != 0) {
                System.err.println("El proceso Escritor terminó con error (código " + codigoSalida + ").");
            }
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el proceso Escritor: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("La ejecución fue interrumpida.");
        }
    }
}
