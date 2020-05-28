package pruebaPrevio;	
	import DAO.ClienteDAO;
	import DAO.Conexion;
	import DAO.ServicioDAO;
	import DAO.TiendaDAO;
	import DAO.exceptions.NonexistentEntityException;
	import DTO.Cliente;
	import DTO.Servicio;
	import DTO.Tienda;
	import java.util.List;
	public class pruebaPrevio {

 public static void main(String[] args) throws NonexistentEntityException, Exception {
	      
	        
	        System.out.println("Tu tienda Online");
	        ClienteDAO cliente = new ClienteDAO();
	        Cliente c = new Cliente("Martha", "macmora90@gmail.com", "123");
	        cliente.create(c);
	        
	        System.out.println("Tu tienda Online");
	        TiendaDAO tienda = new TiendaDAO();
	        Integer n = 1;
	        Tienda t = new Tienda(n,"tienda pastelina","venta de pasteles 24 horas ","macmora90@gmail.com",
	        "123 ", "propietario", "facebook", "web site", "imagen insertada");
	        tienda.create(t);
	        
	        Tienda nueva = tienda.findTienda(1);
	        System.out.println("Tu tienda Online");
	        ServicioDAO servicio = new ServicioDAO();
	        Servicio s = new Servicio(1, "servicio nuevo", "descripcion de servicio", nueva);
	        servicio.create(s);
	        
	        System.out.println("Seguir tienda-Cliente");
	        
	        System.out.println("Actulizar tienda Cliente");
	        Tienda t1 = tienda.findTienda(4);
	        t1.setDescripcion("xxx-xxxxx-xxxxxx");
	        tienda.edit(t1);
	        
	    }
	}
	
