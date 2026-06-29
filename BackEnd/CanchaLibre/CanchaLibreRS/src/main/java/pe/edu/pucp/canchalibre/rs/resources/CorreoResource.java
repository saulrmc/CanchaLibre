package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class CorreoResource {
    private final String correoEmisor = "TU_CORREO@gmail.com";
    private final String claveCorreo = "TU_CLAVE_DE_APLICACION";

    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        try {
            Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoEmisor, claveCorreo);
                }
            });

            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(correoEmisor));
            mensaje.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatario)
            );
            mensaje.setSubject(asunto);
            mensaje.setText(contenido);

            Transport.send(mensaje);

            System.out.println("Correo enviado correctamente a: " + destinatario);

        } catch (Exception e) {
            System.out.println("Error al enviar correo");
            e.printStackTrace();
        }
    }
    public void enviarCorreoBienvenida(String destinatario, String nombre) {
        String asunto = "Bienvenido a CanchaLibre";

        String contenido =
                "Hola " + nombre + ",\n\n" +
                        "Tu cuenta en CanchaLibre fue creada correctamente.\n\n" +
                        "Ya puedes iniciar sesión, buscar canchas y realizar reservas.\n\n" +
                        "Saludos,\n" +
                        "Equipo CanchaLibre";

        enviarCorreo(destinatario, asunto, contenido);
    }

    public void enviarCorreoPago(String destinatario, String nombre, String cancha, String detalleHorario, double monto) {
        String asunto = "Confirmación de pago - CanchaLibre";

        String contenido =
                "Hola " + nombre + ",\n\n" +
                        "Tu pago fue registrado correctamente.\n\n" +
                        "Detalle de la reserva:\n" +
                        "Cancha: " + cancha + "\n" +
                        "Horario: " + detalleHorario + "\n" +
                        "Monto pagado: S/ " + monto + "\n\n" +
                        "Gracias por usar CanchaLibre.\n\n" +
                        "Saludos,\n" +
                        "Equipo CanchaLibre";

        enviarCorreo(destinatario, asunto, contenido);
    }
}
